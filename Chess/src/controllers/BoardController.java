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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

import controllers.enums.NeighborXPosition;
import controllers.enums.NeighborYPosition;
import engine.api.IModel;
import engine.communication.internal.signal.ISignalReceiver;
import engine.communication.internal.signal.types.ModelEvent;
import engine.core.factories.AbstractFactory;
import engine.core.factories.ControllerFactory;
import engine.core.mvc.controller.BaseController;
import engine.utils.io.logging.Tracelog;
import game.entities.ChessEntity;
import game.player.Player;
import game.player.Player.PlayerTeam;
import generated.DataLookup;
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
	 * The list of neighbors logically associated to a specified controller
	 * 
	 * Key1: A Controller that maps to its association of neighbors
	 * Key2: The Y-Axis of the neighbor
	 * Key3: The X-Axis of the neighbor
	 */
	private final Map<TileModel, Map<NeighborYPosition, Map<NeighborXPosition, TileModel>>> _neighbors;
	
	/**
	 * The dimensions of the board game
	 */
	private final Dimension _dimensions = new Dimension(8, 8);

	/**
	 * The list of available board movements
	 * 
	 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
	 *
	 */
	public enum BoardMovement {
		/**
		 * An invalid move
		 */
		INVALID,
		/**
		 * The first piece of the current player is being selected
		 */
		MOVE_1_SELECT,
		/**
		 * Another piece of the current player is being selected
		 */
		MOVE_2_SELECT,
		/**
		 * The first piece that was last selected is being selected again
		 */
		MOVE_2_UNSELECT,
		/**
		 * The piece that was selected last is trying to move to an empty tile
		 */
		MOVE_2_EMPTY,
		/**
		 * The piece that was selected last is trying to move to a tile that has an enemy piece
		 */
		MOVE_2_CAPTURE;
	}
	
	/**
	 * Constructs a new instance of this class
	 * 
	 * @param view The view to link with this controller
	 */
	public BoardController(BoardView view) {
		
		super(view);
		
		// Initialize the neighbor structure to the initial size of the board
		_neighbors = new LinkedHashMap<>(_dimensions.width * _dimensions.height);
		
		// Register the signal listeners, we don't want to wait until rendering is done for this to occur
		// because this class will miss important events before hand
		registerSignalListeners();	
			
		// Get the player controller
		PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class, true); 

		// Create player white and populate its pieces
		playerController.addPlayer(new Player(Player.PlayerTeam.WHITE, Arrays.asList(DataLookup.DataLayerWhite.values()), true));
		
		// Create player black and populate its pieces
		playerController.addPlayer(new Player(Player.PlayerTeam.BLACK, Arrays.asList(DataLookup.DataLayerBlack.values()), true));
	}
	
	/**
	 * Constructs a new instance of this class type
	 *
	 * @param view The view to link with this controller
	 */
	public BoardController(BoardViewTester view) {

		super(view);
		
		// Initialize the neighbor structure to the initial size of the board
		_neighbors = new LinkedHashMap<>(_dimensions.width * _dimensions.height);
		
		// Register the signal listeners, we don't want to wait until rendering is done for this to occur
		// because this class will miss important events before hand
		registerSignalListeners();	
		
		// Get the player controller
		PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class, true); 
		
		// Create player white
		playerController.addPlayer(new Player(Player.PlayerTeam.WHITE, Arrays.asList(DataLookup.DataLayerWhite.values()), false));
		
		// Create player black 
		playerController.addPlayer(new Player(Player.PlayerTeam.BLACK, Arrays.asList(DataLookup.DataLayerBlack.values()), false));
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
		
		// Get the list of neighbors associated to our tile model
		Map<NeighborYPosition, Map<NeighborXPosition, TileModel>> tileModelNeighbors = _neighbors.get(tileModel);
		
		// This collection holds the list of all the neighbors
		List<TileModel> allNeighbors = new ArrayList<>();
		
		// Go through every entry set in our structure
		for(Map.Entry<NeighborYPosition, Map<NeighborXPosition, TileModel>> entry : tileModelNeighbors.entrySet()) {
			
			// Add all the neighbors in our entry set that does not have a null tile model
			allNeighbors.addAll(entry.getValue().values().stream().filter(a -> a != null && a != tileModel).collect(Collectors.toList()));
		}
		
		// return the list of neighbors
		return allNeighbors;
	}
	
	/**
	 * Gets the board dimensions of the game
	 * 
	 * @return The board game dimensions
	 */
	public Dimension getBoardDimensions() {
		return _dimensions;
	}
		
	/**
	 * Gets the current board movement based on the 
	 * 
	 * @param newlySelectedTile The newly selected tile
	 * 
	 * @return The board movement
	 */
	public BoardMovement getBoardMovement(TileModel newlySelectedTile) {
	
		// Make sure that the game is running before continuing
		if(!IsGameRunning()) {
			Tracelog.log(Level.WARNING, true, "Game is not running yet, cannot select any tiles");
			return BoardMovement.INVALID; 
		}
				
		// If the tile belongs to the current player playing
		if(isTileCurrentPlayer(newlySelectedTile)) {
			// If there is no currently selected tile
			if(getPreviouslySelectedTile() == null) {
				return BoardMovement.MOVE_1_SELECT;
			}
			// If the currently selected tile was selected again
			else if(Objects.equals(getPreviouslySelectedTile(), newlySelectedTile)) {
				return BoardMovement.MOVE_2_UNSELECT;
			}
			// If the currently selected is also mine (then both selected and this one are mine)
			else if(isTileCurrentPlayer(getPreviouslySelectedTile())){
				return BoardMovement.MOVE_2_SELECT;
			}
		}
		else if(getTileTeam(newlySelectedTile) == null) {
			
		}
		else if(isTileEnemyPlayer(newlySelectedTile)) {
		
		}
		
		return BoardMovement.INVALID;
	}

	/**
	 * Logically attaches the list of tiles together by sub-dividing the list of tiles.
	 * Note: Order matters in cases such as this, which is why insertion order was important
	 * 		 when I chose the data structure for the neighbors map 
	 */
	private void generateLogicalTileLinks() {
		
		// Get the array representation of our tile models.
		// Note: This is done because it is easier to get a subset of an array
		// 		 and because the neighbor data structure tracks the insertion 
		//		 order at runtime which is what is important here.
		TileModel[] tiles = _neighbors.keySet().toArray(new TileModel[0]);
				
		// For every row that exists within our setup model
		for(int i = 0, rows = _dimensions.height, columns = _dimensions.width; i < rows; ++i) {
			
			// Link the tile rows together
			linkTiles(
				// Previous row
				i - 1 >= 0 ? Arrays.copyOfRange(tiles, (i - 1) * columns, ((i - 1) * columns) + columns) : null,
				// Current Row
				Arrays.copyOfRange(tiles, i * columns, (i * columns) + columns),
				// Next Row
				i + 1 >= 0 ? Arrays.copyOfRange(tiles, (i + 1) * columns, ((i + 1) * columns) + columns) : null
			);
		}
	}

	/**
	 * Links together the passed in rows
	 *  
	 * @param top The top row
	 * @param neutral the neutral row
	 * @param bottom The bottom row
	 */
	private void linkTiles(TileModel[] topRow, TileModel[] neutralRow, TileModel[] bottomRow) {
		
		for(int i = 0, columns = _dimensions.width; i < columns; ++i) {
			
			// Represents the structural view of a particular tile
			Map<NeighborYPosition, Map<NeighborXPosition, TileModel>> neighbors = new HashMap<NeighborYPosition, Map<NeighborXPosition, TileModel>>(){{
				put(NeighborYPosition.TOP, new HashMap<NeighborXPosition, TileModel>());
				put(NeighborYPosition.NEUTRAL, new HashMap<NeighborXPosition, TileModel>());
				put(NeighborYPosition.BOTTOM, new HashMap<NeighborXPosition, TileModel>());
			}};
					
			// Top Neighbors
			neighbors.get(NeighborYPosition.TOP).put(NeighborXPosition.LEFT, i - 1 < 0 || topRow == null ? null : topRow[i - 1]);
			neighbors.get(NeighborYPosition.TOP).put(NeighborXPosition.NEUTRAL, topRow == null ? null : topRow[i]);
			neighbors.get(NeighborYPosition.TOP).put(NeighborXPosition.RIGHT, i + 1 >= columns || topRow == null ? null : topRow[i + 1]);
			
			// Neutral Neighbors
			neighbors.get(NeighborYPosition.NEUTRAL).put(NeighborXPosition.LEFT, i - 1 < 0 ? null : neutralRow[i - 1]);
			neighbors.get(NeighborYPosition.NEUTRAL).put(NeighborXPosition.NEUTRAL, neutralRow[i]);
			neighbors.get(NeighborYPosition.NEUTRAL).put(NeighborXPosition.RIGHT, i + 1 >= columns ? null : neutralRow[i + 1]);
				
			// Bottom Neighbors
			neighbors.get(NeighborYPosition.BOTTOM).put(NeighborXPosition.LEFT, i - 1 < 0 || bottomRow == null ? null : bottomRow[i - 1]);
			neighbors.get(NeighborYPosition.BOTTOM).put(NeighborXPosition.NEUTRAL, bottomRow == null ? null : bottomRow[i]);
			neighbors.get(NeighborYPosition.BOTTOM).put(NeighborXPosition.RIGHT, i + 1 >= columns || bottomRow == null ? null : bottomRow[i + 1]);
			
			// Assign the mappings where we reference the neutral-neutral tile as the key
			_neighbors.put(neutralRow[i], neighbors);
		}
	}

	/**
	 * Indicates if the specified tile belongs to the person currently playing
	 * 
	 * @param tile The tile
	 * 
	 * @return If the tile is that of the person currently playing
	 */
	private boolean isTileCurrentPlayer(TileModel tile) {
		PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class);
		return playerController.getCurrentPlayerTeam() == getTileTeam(tile);
	}

	/**
	 * Indicates if the specified tile belongs to an opposing player
	 * 
	 * @param tile The tile
	 * 
	 * @return If the tile belongs to an opposing player
	 */
	private boolean isTileEnemyPlayer(TileModel tile) {
		PlayerTeam team = getTileTeam(tile);
		PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class);
		return team != null && team != playerController.getCurrentPlayerTeam();
	}
	
	/**
	 * Gets the team associated to the specified tile model
	 * 
	 * @param model The tile model
	 *
	 * @return The team associated to the tile model if any
	 */
	private PlayerTeam getTileTeam(TileModel model) {
		ChessEntity entity = model.getEntity();
		return entity != null ? entity.getTeam() : null;
	}
	
	@Override public void registerSignalListeners() {
		
		// Register to when this controller is added as a listener
		registerSignalListener(IModel.EVENT_LISTENER_ADDED, new ISignalReceiver<ModelEvent<TileModel>>() {
			@Override public void signalReceived(ModelEvent<TileModel> event) {
				
				// Get the tile model from the event object
				TileModel tileModel = event.getSource();
					
				// If what are trying to insert has already been inserted then something went wrong
				if(_neighbors.putIfAbsent(tileModel, null) != null) {
					System.out.println("Error: Tile model already exists in the list... cannot add this one in");
					System.out.println(java.util.Arrays.toString((new Throwable()).getStackTrace()));
				}

				// If we have enough neighboring elements, then its time to link them together
				if(_dimensions.width * _dimensions.height == _neighbors.size()) {
					generateLogicalTileLinks();
				}
			}
		});
	
		// Register to when a tile is selected
		registerSignalListener(TileModel.EVENT_SELECTION_CHANGED, new ISignalReceiver<ModelEvent<TileModel>>() {
			@Override public void signalReceived(ModelEvent<TileModel> event) {
				
				// Unregister the listener to avoid cyclic loops when deselecting a tile
				String listenerIdentifier = unregisterSignalListener(this);
				
				// Get the tile model that fired the event
				TileModel tile = event.getSource();
				
				switch(getBoardMovement(tile)) {
				case INVALID:
					Tracelog.log(Level.WARNING, true, "Invalid board move, cannot perform a move using that tile");
					tile.setSelected(false);
					break;
				case MOVE_1_SELECT:
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