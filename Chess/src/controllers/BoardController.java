/**
* Daniel Ricci <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package controllers;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import engine.api.IModel;
import engine.communication.internal.signal.ISignalReceiver;
import engine.communication.internal.signal.types.ModelEvent;
import engine.core.factories.AbstractFactory;
import engine.core.factories.ControllerFactory;
import engine.core.mvc.controller.BaseController;
import engine.utils.io.logging.Tracelog;
import game.compositions.BoardComposition;
import generated.DataLookup;
import models.PlayerModel;
import models.TileModel;
import views.BoardView;
import views.BoardViewTester;

/**
 * This controller is in charge of the overall board game.  As far as tiles are concerned, they
 * do not know about their neighbors or the rules of the game, they just know that they want to
 * go somewhere.  This is how a tile controller would differ from a board controller.
 * 
 * This controller should receive messages from tile models whenever there is a change in event, and
 * based on that event, the board controller should intervene and do something with respect to the logic
 * of the game rules.
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class BoardController extends BaseController {

    /**
     * The dimensions of the board game
     */
    private final Dimension _dimensions = new Dimension(8, 8);
    
    /**
     * The board composition of the board game 
     */
    private final BoardComposition _boardComposition = new BoardComposition(_dimensions);
    
	/**
	 * This flag indicates if the game is running
	 */
	private boolean _isGameRunning;
	
	/**
	 * The tile model associated to the previously selected tile
	 * 
	 * Note: This tile gets updated when a new tile is selected (when the {@link TileModel#setSelected(boolean)} method is called
	 */
	private TileModel _previouslySelectedTile;
	
	/**
	 * Constructs a new instance of this class
	 * 
	 * @param view The view to link with this controller
	 */
	public BoardController(BoardView view) {
		
		super(view);
	    
	    // Register the signal listeners, we don't want to wait until rendering is done for this to occur
		// because this class will miss important events before hand
		registerSignalListeners();	
			
		// Get the player controller
		PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class, true); 

		// Create player white and populate its pieces
		playerController.addPlayer(new PlayerModel(PlayerModel.PlayerTeam.WHITE, Arrays.asList(DataLookup.DataLayerWhite.values()), true));
		
		// Create player black and populate its pieces
		playerController.addPlayer(new PlayerModel(PlayerModel.PlayerTeam.BLACK, Arrays.asList(DataLookup.DataLayerBlack.values()), true));
	}
	
	/**
	 * Constructs a new instance of this class type
	 *
	 * @param view The view to link with this controller
	 */
	public BoardController(BoardViewTester view) {

		super(view);
		
		// Register the signal listeners, we don't want to wait until rendering is done for this to occur
		// because this class will miss important events before hand
		registerSignalListeners();	
		
		// Get the player controller
		PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class, true); 
		
		// Create player white
		playerController.addPlayer(new PlayerModel(PlayerModel.PlayerTeam.WHITE, Arrays.asList(DataLookup.DataLayerWhite.values()), false));
		
		// Create player black 
		playerController.addPlayer(new PlayerModel(PlayerModel.PlayerTeam.BLACK, Arrays.asList(DataLookup.DataLayerBlack.values()), false));
	}
	
	/**
	 * @return The previously selected tile
	 */
	public TileModel getPreviouslySelectedTile() {
		return _previouslySelectedTile;
	}
	
	/**
	 * @return If the game is running
	 */
	public boolean IsGameRunning() {
		return _isGameRunning;
	}
	
	/**
	 * Starts the board game
	 */
	public void startGame() {
		_isGameRunning = true;
		Tracelog.log(Level.INFO, true, "The game is now running");
	}
		
	/**
	 * Gets all the neighbors associated to the particular model
	 * 
	 * @param tileModel The tile model to use as a search for neighbors around it
	 * 
	 * @return The list of tile models that neighbor the passed in tile model
	 */
	public List<TileModel> getAllNeighbors(TileModel tileModel) {
		return _boardComposition.getAllNeighbors(tileModel);
	}
	
	/**
	 * Gets the board dimensions of the game
	 * 
	 * @return The board game dimensions
	 */
	public Dimension getBoardDimensions() {
		return _dimensions;
	}
		
	
	@Override public void registerSignalListeners() {
		
		// Register to when this controller is added as a listener
		registerSignalListener(IModel.EVENT_LISTENER_ADDED, new ISignalReceiver<ModelEvent<TileModel>>() {
			@Override public void signalReceived(ModelEvent<TileModel> event) {
			    // Add the received entity to the board movement blueprint
				_boardComposition.addTileEntity(event.getSource());
			}
		});
	
		// Register to when a tile is selected
		registerSignalListener(TileModel.EVENT_SELECTION_CHANGED, new ISignalReceiver<ModelEvent<TileModel>>() {
			@Override public void signalReceived(ModelEvent<TileModel> event) {
				
				// Unregister the listener to avoid cyclic loops when deselecting a tile
				String listenerIdentifier = unregisterSignalListener(this);
				
				// Get the tile model that fired the event
				TileModel tile = event.getSource();
				
				switch(tile.getMovementComposition().getBoardMovement(_previouslySelectedTile)) {
				case INVALID:
					Tracelog.log(Level.WARNING, true, "Invalid board move, cannot perform a move using that tile");
					tile.setSelected(false);
					break;
				case MOVE_1_SELECT:
				    // 
				    _boardComposition.getBoardPositions(tile);
				    _previouslySelectedTile = tile;
                    
				    
					Tracelog.log(Level.INFO, true, _previouslySelectedTile.toString() + " is now selected");
					break;
				case MOVE_2_SELECT:
					_previouslySelectedTile.setSelected(false);
					Tracelog.log(Level.INFO, true, _previouslySelectedTile.toString() + " is now deselected");
					_previouslySelectedTile = tile;
					Tracelog.log(Level.INFO, true, _previouslySelectedTile.toString() + " is now selected");
					break;
				case MOVE_2_CAPTURE:
					break;
				case MOVE_2_UNSELECT:
					_previouslySelectedTile.setSelected(false);
					Tracelog.log(Level.INFO, true, _previouslySelectedTile.toString() + " is now deselected");
					_previouslySelectedTile = null;
					break;
				}
				
				// Register back this listener
				registerSignalListener(listenerIdentifier, this);
			}			
		});
	}
}