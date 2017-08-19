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

import java.util.Objects;
import java.util.logging.Level;

import controllers.PlayerController;
import engine.core.factories.AbstractFactory;
import engine.core.factories.ControllerFactory;
import engine.utils.io.logging.Tracelog;
import game.entities.concrete.AbstractChessEntity;
import models.PlayerModel.PlayerTeam;
import models.TileModel;

/**
 * This class represents the instructions for how a particular chess piece should move
 *
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class MovementComponent {
 
	/**
	 * The tile model of this movement component
	 */
    private final TileModel _tile;
    
    /**
     * Represents the available movement positions
     * 
     * @author Daniel Ricci <thedanny09@gmail.com>
     *
     */
    public enum EntityMovements {
        /**
         * Movement is done towards the left
         */
        LEFT,
        /**
         * Movement is done towards the right
         */
        RIGHT,
        /**
         * Movement is done towards the top
         */
        TOP,
        /**
         * Movement is done towards the bottom
         */
        BOTTOM,
    }
    
    /**
     * The list of available board movements
     * 
     * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
     *
     */
    public enum PlayerMovements {
        /**
         * An invalid move
         */
        INVALID(false),
        /**
         * The first piece of the current player is being selected
         */
        MOVE_1_SELECT(false),
        /**
         * Another piece of the current player is being selected
         */
        MOVE_2_SELECT(false),
        /**
         * The first piece that was last selected is being selected again
         */
        MOVE_2_UNSELECT(false),
        /**
         * The piece that was selected last is trying to move to an empty tile
         */
        MOVE_2_EMPTY(true),
        /**
         * The piece that was selected last is trying to move to a tile that has an enemy piece
         */
        MOVE_2_CAPTURE(true);
    	
    	/**
    	 * Indicates if the movement is a 'finishing move' where the player is done their turn
    	 */
    	public final boolean isMoveFinal;
    	
    	/**
    	 * Constructs a new instance of this type
    	 * 
    	 * @param isMoveFinal if the movement is a 'finishing move' where the player is done their turn
    	 */
    	PlayerMovements(boolean isMoveFinal) {
    		this.isMoveFinal = isMoveFinal;
    	}
    }
    
    /**
     * Constructs a new instance of this class type
     * 
     * @param tile The tile associated to the movement
     */
    public MovementComponent(TileModel tile) {
    	if(tile == null) {
    		IllegalArgumentException exception = new IllegalArgumentException("Null tile");
    		Tracelog.log(Level.SEVERE, true, exception);
    		throw exception;
    	}
    	_tile = tile;
    }
    
    /**
     * Gets the current board movement based on the 
     * 
     * @param newlySelectedTile The newly selected tile
     * 
     * @return The board movement
     */
    public PlayerMovements getBoardMovement(TileModel previouslySelectedTile) {
        
        // If the tile belongs to the current player playing
        if(isTileCurrentPlayer(_tile)) {
            // If there is no currently selected tile
            if(previouslySelectedTile == null) {
                return PlayerMovements.MOVE_1_SELECT;
            }
            // If the currently selected tile was selected again
            else if(Objects.equals(previouslySelectedTile, _tile)) {
                return PlayerMovements.MOVE_2_UNSELECT;
            }
            // If the currently selected is also mine (then both selected and this one are mine)
            else if(isTileCurrentPlayer(previouslySelectedTile)){
                return PlayerMovements.MOVE_2_SELECT;
            }
        }
        // If the tile has no team then it is an empty tile
        else if(getTileTeam(_tile) == null) {
        	if(previouslySelectedTile != null && isTileCurrentPlayer(previouslySelectedTile)) {
        		return PlayerMovements.MOVE_2_EMPTY;
        	}
        }
        // If the tile occupied by the opposing team
        else if(isTileEnemyPlayer(_tile)) {
            // If the previously selected tile exists
            if(previouslySelectedTile != null && isTileCurrentPlayer(previouslySelectedTile)) {
            	return PlayerMovements.MOVE_2_CAPTURE;
            }
        }
        
        return PlayerMovements.INVALID;
    }
    
    /**
     * Indicates if the specified tile belongs to the person currently playing
     * 
     * @param tile The tile
     * 
     * @return If the tile is that of the person currently playing
     */
    public static boolean isTileCurrentPlayer(TileModel tile) {
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
    public static boolean isTileEnemyPlayer(TileModel tile) {
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
    public static PlayerTeam getTileTeam(TileModel model) {
        AbstractChessEntity entity = model.getEntity();
        return entity != null ? entity.getTeam() : null;
    }  
}