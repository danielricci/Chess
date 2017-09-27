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
import java.util.Map;
import java.util.logging.Level;

import engine.api.IModel;
import engine.communication.internal.signal.ISignalReceiver;
import engine.communication.internal.signal.arguments.ModelEventArgs;
import engine.core.factories.AbstractFactory;
import engine.core.factories.ControllerFactory;
import engine.core.factories.ModelFactory;
import engine.core.factories.ViewFactory;
import engine.core.mvc.controller.BaseController;
import engine.utils.io.logging.Tracelog;
import game.components.BoardComponent;
import game.components.MovementComponent;
import game.components.MovementComponent.EntityMovements;
import game.components.MovementComponent.PlayerActions;
import game.entities.concrete.AbstractChessEntity;
import game.events.EntityEventArgs;
import generated.DataLookup;
import models.PlayerModel;
import models.PlayerModel.PlayerTeam;
import models.TileModel;
import views.BoardView;
import views.DebuggerView;
import views.PromotionView;

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
     * The board component of the board game 
     */
    private final BoardComponent _boardComponent;
    
    /**
     * The dimensions of the board game
     */
    private final Dimension _dimensions = new Dimension(8, 8);
    
	/**
	 * This flag indicates if the game is running
	 */
	private boolean _isGameRunning;
	
	/**
	 * This flag indicates if the game is in inspection mode
	 */
	private boolean _isGameInspecting;
	
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
	    
		// Instantiate the board component
		_boardComponent = new BoardComponent(_dimensions);
		
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
	public BoardController(DebuggerView view) {

		super(view);

	      // Instantiate the board component
        _boardComponent = new BoardComponent(_dimensions);
		
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
	 *	Clears the board of all chess pieces
	 */
	public void clearBoard() {
		
		// Send a signal to all tile models to clear their entity
		AbstractFactory.getFactory(ModelFactory.class).multicastSignal(
			TileModel.class, 
			new EntityEventArgs(this, TileModel.EVENT_ENTITY_CHANGED, null)
		);
		
		// Clear the previously selected tile if any
		_previouslySelectedTile = null;

		// Clear all the entity references held by all the players
		PlayerController player = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class);
		for(PlayerTeam team : PlayerTeam.values()) {
			player.getPlayer(team).clearEntities();
		}
	}
	
	/**
	 * Clears the board of the specified tile
	 * 
	 * @param tile The tile to clear
	 */
	public void clearBoard(TileModel tile) {
		
		// Get the tile's entity
		AbstractChessEntity entity = tile.getEntity();
		if(entity == null) {
			return;
		}
		// Clear the entity from the model
		tile.setEntity(null);
		
		// Clear the entity from the player
		PlayerController player = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class);
		player.getPlayer(entity.getTeam()).removeEntity(entity);
		
		// Clear the previously selected tile if any
		_previouslySelectedTile = null;
	}
	
	/**
	 * Gets all the neighbors associated to the particular model
	 * 
	 * @param tileModel The tile model to use as a search for neighbors around it
	 * 
	 * @return The list of tile models that neighbor the passed in tile model
	 */
	public List<TileModel> getAllNeighbors(TileModel tileModel) {
		return _boardComponent.getAllNeighbors(tileModel);
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
     * @return The previously selected tile
     */
    public TileModel getPreviouslySelectedTile() {
    	return _previouslySelectedTile;
    }

    /**
     * @return If the game is running
     */
    public boolean isGameRunning() {
    	return _isGameRunning;
    }
    
    /**
     * Starts the board game
     */
    public void startGame() {
    	_isGameRunning = true;
    	Tracelog.log(Level.INFO, true, "The game is now starting");
    	
    	PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class, true); 
    	playerController.queuePlayers();
    }

    /**
     * Stops the board game
     */
    public void stopGame() {
    	_isGameRunning = false;
    	Tracelog.log(Level.INFO, true, "The game is now stopped");
    }
    
    public void clearBoardHighlights() {
    	// Create the entity event, however simply clear the highlighting, we still want to 
    	// keep the board pieces on the board
    	EntityEventArgs event = new EntityEventArgs(this, TileModel.EVENT_HIGHLIGHT_CHANGED, null);
    	event.isHighlighted = false;
    	
    	// Send a message to all the tile models to stop highlighting themselves
    	AbstractFactory.getFactory(ModelFactory.class).multicastSignal(
    		TileModel.class, event
    	);
    	
    	// If there is a previously set tile then remove it's selection 
    	if(_previouslySelectedTile != null) {
    		_previouslySelectedTile.setSelected(false);
    		_previouslySelectedTile = null;
    	}
    }
    
	/**
	 * Sets the inspecting state of the debugger
	 *
	 * @param isGameInspecting The state of the inspector
	 */
	public void setIsInspecting(boolean isGameInspecting) {
		_isGameInspecting = isGameInspecting;
	}
	
	/**
	 * Gets if the game is in inspection state
	 *
	 * @return TRUE if the game is in inspection mode
	 */
	public boolean getIsInspecting() {
		return !isGameRunning() && _isGameInspecting;
	}	

    @Override public void registerSignalListeners() {
		
		// Register to when this controller is added as a listener
		registerSignalListener(IModel.EVENT_LISTENER_ADDED, new ISignalReceiver<ModelEventArgs<TileModel>>() {
			@Override public void signalReceived(ModelEventArgs<TileModel> event) {
			    // Add the received entity to the board movement blueprint
				_boardComponent.addTileEntity(event.getSource());
			}
		});
	
		// Register to when a tile is selected
		registerSignalListener(TileModel.EVENT_SELECTION_CHANGED, new ISignalReceiver<ModelEventArgs<TileModel>>() {
			
			@Override public void signalReceived(ModelEventArgs<TileModel> event) {
		
				// Unregister the listener to avoid cyclic loops when deselecting a tile
				String listenerIdentifier = unregisterSignalListener(this);
				
				// Get the tile model that fired the event
				TileModel currentlySelectedTile = event.getSource();

                // Create the entity event to be sent out.  
                EntityEventArgs entityEventArgs = new EntityEventArgs(
            		event.getSource(), 
            		event.getOperationName(), 
            		currentlySelectedTile.getEntity()
        		);
				
				// Get the list of current movements for the previously selected tile
				PlayerActions currentMovement = PlayerActions.INVALID;
				
				// Determine if the movement is an en-passent movement, therefore overriding the current movement found
				if(_previouslySelectedTile != currentlySelectedTile && _boardComponent.getEnPassentBoardPositions(_previouslySelectedTile).keySet().contains(currentlySelectedTile)) {
					currentMovement = PlayerActions.MOVE_2_CAPTURE;
				}
				else {
					currentMovement = currentlySelectedTile.getMovementComponent().getBoardMovement(_previouslySelectedTile);
				}
				
				// This flag holds if the operation being done was successful
				boolean isSuccessful = true;
				
				switch(currentMovement) {
					case INVALID:
						currentlySelectedTile.setSelected(false);
						break;
					case MOVE_1_SELECT: {

	                    // Set the previously selected tile from what was just selected
                        _previouslySelectedTile = currentlySelectedTile;
					    
					    // Get the list of positions that can be moved to
                        Map<TileModel, EntityMovements[]> availablePositions = _boardComponent.getBoardPositions(_previouslySelectedTile);
                        
                        // Go through each path and mark the tiles as highlighted
                        availablePositions.entrySet().stream().forEach(z -> z.getKey().setHighlighted(true));
					    
					    break;
					}
					case MOVE_2_SELECT: {
				    	
						// Remove the highlighted tiles from the previous selection, and highlight the
						// new selection based on what was currently selected
						_boardComponent.getBoardPositions(_previouslySelectedTile).entrySet().stream().forEach(z -> z.getKey().setHighlighted(false));
						_boardComponent.getBoardPositions(currentlySelectedTile).entrySet().stream().forEach(z -> z.getKey().setHighlighted(true));

	                      // Remove the selection from what was previously selected
                        _previouslySelectedTile.setSelected(false);
						
				        // Set the previously selected tile to be what was just selected
				        _previouslySelectedTile = currentlySelectedTile;
						
	                	break;
					}
					case MOVE_2_CAPTURE: {
						// Get the list of available board positions of the previously selected tile
						// and if the currently selected tile is a valid position then proceed
					    Map<TileModel, EntityMovements[]> availablePositions = _boardComponent.getBoardPositions(_previouslySelectedTile);
				        if(availablePositions.containsKey(currentlySelectedTile)) {
					    
					    	// This flag indicates if the capture being performed is an en-passent capture
					    	boolean isEnPassentCapture = _boardComponent.getEnPassentBoardPositions(_previouslySelectedTile).keySet().contains(currentlySelectedTile);
					
					    	// Remove the entity being captured from the enemy player
					    	PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class, true);
					    	if(isEnPassentCapture) {
					    		TileModel enemyTile = _boardComponent.getEnPassentEnemy(_previouslySelectedTile);
					    		playerController.getPlayer(enemyTile.getEntity().getTeam()).removeEntity(enemyTile.getEntity());
					    		enemyTile.setEntity(null);
					    	}
					    	else {
					    		playerController.getPlayer(currentlySelectedTile.getEntity().getTeam()).removeEntity(currentlySelectedTile.getEntity());	
					    	}
					    	
					        // Replace the entity in the currently selected tile with the one from
					    	// the previously selected tile, and then remove the entity from the
					    	// previously selected tile
					    	AbstractChessEntity entity = _previouslySelectedTile.getEntity();
					    	_previouslySelectedTile.setEntity(null);
					    	currentlySelectedTile.setEntity(entity);
					    	
					        
					        // Go through the list of positions available and remove their highlight guides
					        for(Map.Entry<TileModel, EntityMovements[]> kvp : availablePositions.entrySet()) {
					            kvp.getKey().setHighlighted(false);
					        }
					        
					        // Remove the selection from tiles selected in this operation
					        currentlySelectedTile.setSelected(false);
					        _previouslySelectedTile.setSelected(false);
					        _previouslySelectedTile = null;
				        }
				        else {
				        	currentlySelectedTile.setSelected(false);
				        	isSuccessful = false;
				        }
				        
				        break;
					}

					case MOVE_2_EMPTY: {
						
						// Get the list of available board positions of the previously selected tile
						// and if the currently selected tile is a valid position then proceed
					    Map<TileModel, EntityMovements[]> availablePositions = _boardComponent.getBoardPositions(_previouslySelectedTile);
					    if(availablePositions.containsKey(currentlySelectedTile)) {

					    	// Record the movement that occurred
					    	entityEventArgs.movements = availablePositions.get(currentlySelectedTile);

					    	// Verify if the movement is a castling movement
					    	if(_previouslySelectedTile.getEntity().getIsCastlableFromCandidate()) {
    	                        for(EntityMovements[] movement : _previouslySelectedTile.getEntity().getCastlingBoardMovements()) {
    	                            if(MovementComponent.compareMovements(movement, entityEventArgs.movements)) {
    	                                // Transfer the previous entity to the new location
    	                                TileModel castleTile = _boardComponent.getCastlableToEntity(_previouslySelectedTile, entityEventArgs.movements[0]);
    	                                _boardComponent.setCastlableMovement(currentlySelectedTile, castleTile, entityEventArgs.movements[0]);                                    	                            	
    	                            }
    	                        }
					    	}
					    	
							// Go through the list of positions available and remove their highlight guides						
							availablePositions.entrySet().stream().forEach(z -> z.getKey().setHighlighted(false));
					    	
							// Remove the selections from both the previous and currently selected tile
							_previouslySelectedTile.setSelected(false);
							currentlySelectedTile.setSelected(false);
							
							// Transfer the previous entity to the new location
							AbstractChessEntity entity = _previouslySelectedTile.getEntity();
                            _previouslySelectedTile.setEntity(null);
                            _previouslySelectedTile = null;
                            currentlySelectedTile.setEntity(entity);
					    }
					    else {
					    	currentlySelectedTile.setSelected(false);
						    isSuccessful = false;	
					    }
					    
					    break;
					}
					case MOVE_2_UNSELECT: {
						
						// Unselect the previously selected tile.  I do a check to avoid a needless update call
						// if the tile is already in the proper state
						if(_previouslySelectedTile.getIsSelected()) {
							_previouslySelectedTile.setSelected(false);	
						}
						
	                    _previouslySelectedTile = null;

	                    // Go through each path and mark the tiles as highlighted
	                    _boardComponent.getBoardPositions(currentlySelectedTile).entrySet().stream().forEach(z -> z.getKey().setHighlighted(false));
						
						break;
					}
				}
				
				if(isSuccessful && currentMovement.isMoveFinal) {
				
					// If the tile has reached the end of the board then display the promotion view
					if(currentlySelectedTile.getEntity().isPromotable() && !_boardComponent.canMoveForward(currentlySelectedTile)) {
						PromotionView view = AbstractFactory.getFactory(ViewFactory.class).get(PromotionView.class, true);
						view.getViewProperties().getEntity(PromotionController.class).setTile(currentlySelectedTile);
						view.render();
					}
					
				    // Indicate that the tile has moved at least once
				    currentlySelectedTile.getEntity().setHasMoved(true);

				    // Set the player action of the event to be sent out
					entityEventArgs.playerAction = currentMovement;
					
                    // Get the player controller and send the entity event to it
                    PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class, true);
				    playerController.update(entityEventArgs);
					
                    // Update the checked/checkmate/stalemate states of all the players
                    for(PlayerModel player : playerController.getPlayers()) {

                    	// Check if there is a checkmate
                    	List<TileModel> checkedPositions = _boardComponent.getCheckedPositions(player);
                        for(AbstractChessEntity entity : player.getCheckableEntities()) {
                            entity.setChecked(checkedPositions.stream().anyMatch(z -> entity.equals(z.getEntity())));
                            if(entity.getIsChecked() && _boardComponent.getBoardPositions(entity.getTile()).isEmpty()) {
                            	entity.setCheckMate(true);
                            	stopGame();
                            }
                            entity.refresh();
                        }
                        
                        // Go through the list of entities owned by the player and sum up the number of positions that 
                        // can be played in total.  Note that these positions do not include any checkable entities
                        boolean canMoveOtherPieces = player.getEntities().stream().filter(z -> !z.getIsCheckable()).mapToInt(z -> _boardComponent.getBoardPositions(z.getTile()).size()).sum() > 0;
                        
		                // Go through the list of checkable entities, and if they aren't
		                // in a checkable state, ensure that they aren't in a stalemate state
		                for(AbstractChessEntity entity : player.getCheckableEntities()) {
		                	if(
		                			
			                	// 1. If there are places where the entity can move
	                			!_boardComponent.getBoardPositionsImpl(entity.getTile()).isEmpty() &&
		                		// 2. if the board positions (after filtering) is in fact empty	                			
	                			_boardComponent.getBoardPositions(entity.getTile()).isEmpty() &&
			                	// 3. if the player does not have any other pieces that can move	                			
	                			!canMoveOtherPieces &&
	                			// 4. if the player did not just perform the move
	                			playerController.getCurrentPlayer() != player
                			) {
		                    	entity.setCheckMate(true);
		                    	stopGame();
		                    	entity.refresh();
		                	}
		                }
                    }
                    
					// Switch to the next player in turn
					playerController.nextPlayer();
				}
				
				// Register back this listener
				registerSignalListener(listenerIdentifier, this);
			}			
		});
	}	
}