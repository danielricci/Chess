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

package game.entities.interfaces;

import java.util.ArrayList;
import java.util.List;

import game.components.MovementComponent.EntityMovements;

/**
 * This interface defines functionality that must be implemented 
 * by all chess pieces in the game
 *
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public interface IChessEntity {
    
    /**
     * Gets the list of available movements
     * 
     * @return The list of movements
     */
    public List<EntityMovements[]> getMovements();

    /**
     * Indicates if the entity moves in a continuous manner
     * 
     * @return TRUE If the movement of this entity is continuous
     */
    public boolean isMovementContinuous();
        
    /**
     * Gets the list of board movements with results in captures
     * 
     * Note: For almost all chess pieces this method call is identical to {@link #getMovements()}
     * 
     * @return All the capturable board movements of this chess entity
     */
    default public List<EntityMovements[]> getCapturableBoardMovements() {
    	return getMovements();
    }
    
    /**
     * Gets the list of movements ass
     * 
     * @return
     */
    default public List<EntityMovements[]> getCastlingBoardMovements() {
    	return new ArrayList<EntityMovements[]>();
    }
    
    /**
     * Indicates if the moves performed by the entity are also used to capture opponent pieces
     * 
     * @return TRUE if the movements performed by this piece results in a capture
     */
    default public boolean isMovementCapturable() {
    	return true;
    }
    
    /**
     * Indicates if the chess entity can be promoted
     * 
     * @return If the chess piece can be promoted
     */
    default public boolean isPromotable() {
    	return false;
    }
    
    /**
     * Indicates if the chess entity can perform an en-passent move as one of its possible moves
     * 
     * Note: This method does not mean you can actually do the move, but simply
     * indicates if the chess entity is geared for doing the en-passent move
     */
    default public boolean isEnPassent() {
    	return false;
    }
    
    /**
     * Indicates if the chess entity can be captured with an en-passent move
     * 
     * @return TRUE if the chess entity can be captured with en-passent
     */
    default public boolean isEnPassentCapturable() {
    	return false;
    }
    
    /**
     * Gets if the chess entity is a checkable entity
     * 
     * @return TRUE if the chess entity can be put into check
     */
    default public boolean getIsCheckable() {
        return false;
    }
    
    /**
     * Gets if the chess entity can perform the start of the castling move
     * 
     * @return TRUE if the chess piece can start the castling move
     */
    default public boolean getIsCastlableFromCandidate() {
    	return false;
    }
   
    /**
     * Gets if the chess entity can perform the end of the castling move
     * 
     * @return TRUE if the chess piece can start the castling move
     */
    default public boolean getIsCastlableToCandidate() {
    	return false;
    }
}