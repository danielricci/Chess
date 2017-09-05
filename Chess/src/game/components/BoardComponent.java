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

package game.components;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import game.components.MovementComponent.EntityMovements;
import game.components.MovementComponent.PlayerDirection;
import game.entities.concrete.AbstractChessEntity;
import models.PlayerModel;
import models.TileModel;

/**
 * This class represents the instructions for how the board game should operate
 *
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class BoardComponent {
 
    /**
     * The dimensions of the board game
     */
    private final Dimension _dimensions;
    
    /**
     * The list of neighbors logically associated to a specified controller
     * 
     * Key1: The tile model center
     * Key2: The TOP, BOTTOM, LEFT, RIGHT neighboring tiles of the specified key1
     */
    private final Map<TileModel, Map<EntityMovements, TileModel>> _neighbors = new LinkedHashMap();
    
    /**
     * Constructs a new instance of this class type
     * 
     * @param dimensions The board game dimensions
     */
    public BoardComponent(Dimension dimensions) {
        _dimensions = dimensions;
    }
    
    /**
     * Adds the specified tile model into the neighbors list
     * 
     * @param tileModel The tile model
     */
    public void addTileEntity(TileModel tileModel) {
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
    
    /**
     * Indicates if the specified tile contains an entity that has the ability to move forward
     * 
     * @param tileModel The tile model in question
     * 
     * @return TRUE if the specified tile contains an entity that has the ability to move forward
     */
    public boolean canMoveForward(TileModel tileModel) {
    	
    	AbstractChessEntity entity = tileModel.getEntity();
    	if(entity != null) {
    		return _neighbors.get(tileModel).get(PlayerDirection.getNormalizedMovement(entity.getPlayer().getTeam().DIRECTION, EntityMovements.UP)) != null;	
    	}
    	
    	return false;
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
            Map<EntityMovements, TileModel> neighbors = new HashMap<EntityMovements, TileModel>();
                    
            // Populate the neighbors structure with the movement elements
            // Note: Diagonals can be fetched using these primitives
            neighbors.put(EntityMovements.UP, topRow == null ? null : topRow[i]);
            neighbors.put(EntityMovements.LEFT, i - 1 < 0 ? null : neutralRow[i - 1]);
            neighbors.put(EntityMovements.RIGHT, i + 1 >= columns ? null : neutralRow[i + 1]);
            neighbors.put(EntityMovements.DOWN, bottomRow == null ? null : bottomRow[i]);
                       
            // Assign the mappings where we reference the neutral-neutral tile as the key
            _neighbors.put(neutralRow[i], neighbors);
        }
    }

    /**
     * Logically attaches the list of tiles together by sub-dividing the list of tiles.
     * Note: Order matters in cases such as this, which is why insertion order was important
     *       when I chose the data structure for the neighbors map 
     */
    private void generateLogicalTileLinks() {
                
        // Get the array representation of our tile models.
        // Note: This is done because it is easier to get a subset of an array
        //       and because the neighbor data structure tracks the insertion 
        //       order at runtime which is what is important here.
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
     * Gets the list of board positions that the specified tile can move on
     * 
     * Note: This is done with respect to conditions of the current player that is playing
     * 
     * @param tileModel The tile model being played
     * 
     * @return The list of tiles to move towards
     */
    // TODO - this method depends on the current player, fix that?
    public Map<TileModel, EntityMovements[]> getBoardPositions(TileModel tileModel) {
        
        Map<TileModel, EntityMovements[]> allMoves = new HashMap();
        
        // Attempt to get the en-passent moves of the specified tile
        for(Map.Entry<TileModel, EntityMovements[]> kvp : getEnPassentBoardPositions(tileModel).entrySet()) {
			allMoves.putIfAbsent(kvp.getKey(), kvp.getValue());
		}
        
        // If tile model does not has a chess entity then there are no moves to get
        AbstractChessEntity entity = tileModel.getEntity();
        if(entity == null) {
            return allMoves;
        }
        
        // The flag indicating if the entity can capture enemy pieces with the same
        // movement as their allowed list of movements or if they have a separate list for that
        final boolean isMovementCapturable = entity.isMovementCapturable();

        // Holds the list of regular movements and capturable movements which
        // are movements outside the general scope of a normal movement
        List<EntityMovements[]> movements = entity.getMovements();
        List<EntityMovements[]> capturableMovements = new ArrayList();
        
        // If this entity is an entity where its movements are different
        // from it's actual capture movements then add those into a separate 
        // list so we can differentiate between both of them
        if(!isMovementCapturable) {
        	capturableMovements.addAll(entity.getCapturableBoardMovements());
        	movements.addAll(capturableMovements);
        }
        
        // Go through the list of path movements for the entity
        for(EntityMovements[] movementPath : movements) {
            
        	TileModel traverser = tileModel;
        	
        	do {
        		// Temporary list to hold our entire path, and from there I 
        		// pick the last element
	        	List<TileModel> tiles = new ArrayList();
	        	
	        	for(EntityMovements movement : movementPath) {
	        		
	        		// Normalize the movement w.r.t the player of the entity
	        		movement = PlayerDirection.getNormalizedMovement(entity.getPlayer().getTeam().DIRECTION, movement);
	        		
	        		// Get the movements of the tile from our position
	        		traverser = _neighbors.get(traverser).get(movement);
	        		
	        		// If the position is outside the board dimensions then clear the movement to get 
	        		// to that location and exit the loop
	        		if(traverser == null) {
	        			tiles.clear();
	        			break;
	        		}
	        		
	        		// If the entity contains movements that differ from their capture movements
	        		// and we are trying to use a regular movement then this is not legal.  
	        		// As an example, without this code a pawn could do a capture on it's two move
	        		// movement, or it could hop over another enemy pawn
	        		if(!isMovementCapturable && MovementComponent.isTileEnemyPlayer(traverser) && !capturableMovements.contains(movementPath)) {
	        			tiles.clear();
	        			break;
	        		}
	        		
	        		// Add the traversed tile to our constructed path
	        		tiles.add(traverser);
	        	}
	        	
	        	if(tiles.isEmpty()) {
	        		break;
	        	}
	        	
	        	// Get the last tile within our path (the endpoint)
	        	TileModel destinationTile = tiles.get(tiles.size() - 1);
	        	
        		// If the entity contains movements that differ from their capture movements
	        	// then ensure that if we are trying to capture an enemy player that it only
	        	// ever is allowed if the movement is within the capture movement list 
	        	// that the entity holds
	        	// Note: This line of code looks a little similar to the one above in the second for-each, however
	        	// they both do completely different things however they are both related to differences of 
	        	// how a piece moves and captures
	            if(!isMovementCapturable && !MovementComponent.isTileEnemyPlayer(destinationTile) && capturableMovements.contains(movementPath)) {
	        		break;
	        	}

	            // Add the last tile into our list of valid tiles
	        	allMoves.put(destinationTile, movementPath);

	        	
	        	// If the tile has an enemy player on it then stop here
	        	// If the tile is not continuous then we do not need to 
	        	// continue with the same move
	        	if(MovementComponent.isTileEnemyPlayer(destinationTile) || !entity.isMovementContinuous()) {
	        		break;
	        	}
	        	
        	} while(true);
        }
            
        return allMoves;
    }
    
    /**
     * Gets the list of checked positions of the specified player
     * 
     * @param player The player to check for checked positions
     *  
     * @return The list of checked positions
     */
    public List<TileModel> getCheckedPositions(PlayerModel player) {
        
    	
    	//change this so that it looks at the player checked positions that is passed in
    	
        // The list of enemy owned tiles that are checked by the specified player
        List<TileModel> checkedEntities = new ArrayList();
        
    	// Get the list of checkable entities owned by the enemy player
    	List<AbstractChessEntity> checkableEntities = player.getCheckableEntities();
    	
    	// Go through the list of player owned entities and see if any of them can
    	// move to the checkable entity of the enemy
    	for(AbstractChessEntity playerEntity : player.getEntities()) {
    	    for(Map.Entry<TileModel, EntityMovements[]> movement : getBoardPositions(playerEntity.getTile()).entrySet()) {
                if(checkableEntities.contains(movement.getKey().getEntity())) {
                    checkedEntities.add(movement.getKey());
                }
            }
    	}
    	
    	// return the list of entities
    	return checkedEntities;
    }
    
    
    public boolean isMoveChecked(PlayerModel player, TileModel from, TileModel to) {
    	
    	AbstractChessEntity tempEntityFrom = from.getEntity();
    	AbstractChessEntity tempEntityTo = to.getEntity();
    	
    	from.setSuppressUpdates(true);
    	to.setSuppressUpdates(true);
    	to.setEntity(from.getEntity());
    	from.setEntity(null);
    	
    	// Check for any checked positions
    	List<TileModel> checkedPositions = getCheckedPositions(player);

    	from.setEntity(tempEntityFrom);
    	to.setEntity(tempEntityTo);
    	from.setSuppressUpdates(false);
    	to.setSuppressUpdates(false);
    	
        return false;
    }

    /**
     * Gets the En-Passent movements of the specified tile
     * 
     * @param source The tile to get the moves from
     * 
     * @return The mappings of available moves for the specified tile
     */
    public Map<TileModel, EntityMovements[]> getEnPassentBoardPositions(TileModel source) {
    	Map<TileModel, EntityMovements[]> movements = new HashMap();
    	
    	// Verify if the passed in source is a valid tile for performing an en-passent move
    	if(source == null || source.getEntity() == null || !source.getEntity().isEnPassent()) {
    		return movements;
    	}
    	
    	AbstractChessEntity entity = source.getEntity();
    	
    	// Left En-Passent
    	EntityMovements enPassentLeft = PlayerDirection.getNormalizedMovement(entity.getPlayer().getTeam().DIRECTION, EntityMovements.LEFT);
    	TileModel capturableEnPassentLeft = _neighbors.get(source).get(enPassentLeft);
    	if(capturableEnPassentLeft != null && MovementComponent.isTileEnemyPlayer(capturableEnPassentLeft) && capturableEnPassentLeft.getEntity().isEnPassentCapturable()) {
    		
    		EntityMovements enPassentBackwards = PlayerDirection.getNormalizedMovement(capturableEnPassentLeft.getEntity().getPlayer().getTeam().DIRECTION, EntityMovements.DOWN);
			TileModel capturableEnPassentPosition = _neighbors.get(capturableEnPassentLeft).get(enPassentBackwards); 
    		
			movements.put(
    			capturableEnPassentPosition,
				PlayerDirection.getNormalizedMovement(entity.getPlayer().getTeam().DIRECTION, new EntityMovements[] { EntityMovements.UP, EntityMovements.LEFT })
			);
    	}
    	
    	// Right En-Passent
    	EntityMovements enPassentRight = PlayerDirection.getNormalizedMovement(entity.getPlayer().getTeam().DIRECTION, EntityMovements.RIGHT);
    	TileModel capturableEnPassentRight = _neighbors.get(source).get(enPassentRight);
    	if(capturableEnPassentRight != null && MovementComponent.isTileEnemyPlayer(capturableEnPassentRight) && capturableEnPassentRight.getEntity().isEnPassentCapturable()) {
    		
    		EntityMovements enPassentBackwards = PlayerDirection.getNormalizedMovement(capturableEnPassentRight.getEntity().getPlayer().getTeam().DIRECTION, EntityMovements.DOWN);
			TileModel capturableEnPassentPosition = _neighbors.get(capturableEnPassentRight).get(enPassentBackwards); 
    		
			movements.put(
    			capturableEnPassentPosition,
				PlayerDirection.getNormalizedMovement(entity.getPlayer().getTeam().DIRECTION, new EntityMovements[] { EntityMovements.UP, EntityMovements.RIGHT })
			);
    	}

    	return movements;
    }
    
    /**
     * Gets the enemy tile associated to the specified tile
     * 
     * @param tile The tile of the entity doing the en-passent
     * 
     * @return The enemy tile
     */
    public TileModel getEnPassentEnemy(TileModel tile) {
    	for(Map.Entry<TileModel, EntityMovements[]> kvp : getEnPassentBoardPositions(tile).entrySet()) {
    		TileModel enemy = _neighbors.get(kvp.getKey()).get(PlayerDirection.getNormalizedMovement(tile.getEntity().getPlayer().getTeam().DIRECTION, EntityMovements.DOWN));
    		if(enemy.getEntity() != null && enemy.getEntity().isEnPassentCapturable()) {
    			return enemy;
    		}
    	}
    	return null;
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
        Map<EntityMovements, TileModel> tileModelNeighbors = _neighbors.get(tileModel);
        
        // This collection holds the list of all the neighbors
        List<TileModel> allNeighbors = new ArrayList();
        
        // Go through every entry set in our structure
        for(Map.Entry<EntityMovements, TileModel> entry : tileModelNeighbors.entrySet()) {
            
            // Get the tile model
            TileModel tile = entry.getValue();
            if(tile == null) {
                continue;
            }
            
            // Add our tile to the list
            allNeighbors.add(tile);
            
            // To get the diagonals, make sure that if we are on the top or bottom tile
            // that we fetch their respective tiles, and provided that they are valid
            // add them to our list.
            switch(entry.getKey()) {
            case UP:
            case DOWN:
                TileModel left = _neighbors.get(tile).get(EntityMovements.LEFT);
                if(left != null) {
                    allNeighbors.add(left);
                }
                
                TileModel right = _neighbors.get(tile).get(EntityMovements.RIGHT);
                if(right != null) {
                    allNeighbors.add(right);
                }
                break;
            }
        }
        
        // return the list of neighbors
        return allNeighbors;
    }
}