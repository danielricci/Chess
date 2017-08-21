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
     * Gets the list of board movements with results in captures
     * 
     * Note: For almost all chess pieces this method call is identical to {@link #getMovements()}
     * 
     * @return All the capturable board movements of this chess entity
     */
    public List<EntityMovements[]> getCapturableBoardMovements();
    
    /**
     * Indicates if the entity moves in a continuous manner
     * 
     * @return TRUE If the movement of this entity is continuous
     */
    public boolean isMovementContinuous();
    
    /**
     * Indicates if the moves performed by the entity are also used 
     * to capture opponent pieces
     * 
     * @return TRUE if the movements performed by this piece results in a capture
     */
    public boolean isMovementCapturable();
}
