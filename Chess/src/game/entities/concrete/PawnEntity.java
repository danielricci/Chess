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

package game.entities.concrete;

import java.util.ArrayList;
import java.util.List;

import game.components.MovementComponent.EntityMovements;
import generated.DataLookup.DataLayerName;

/**
 * This class represents a pawn in the chess game
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
class PawnEntity extends AbstractChessEntity {
    
    /**
     * Constructs a new instance of this class type
     */
    public PawnEntity() {
        super(DataLayerName.PAWN);
    }

    @Override public List<EntityMovements[]> getMovements() {
        return new ArrayList<EntityMovements[]>() {{
            
            // This is the unit movement that all pawns support
            add(new EntityMovements[] { EntityMovements.UP });
            
            // If this pawn has not moved yet, then expose the double unit movement
            if(!hasMovedOnce()) {
                add(new EntityMovements[] { EntityMovements.UP, EntityMovements.UP });    
            }
        }};
    }
    
    @Override public List<EntityMovements[]> getCapturableBoardMovements() {
        return new ArrayList<EntityMovements[]>() {{
            // This is the unit movement that all pawns support
            add(new EntityMovements[] { EntityMovements.UP, EntityMovements.RIGHT  });
            add(new EntityMovements[] { EntityMovements.UP, EntityMovements.LEFT  });
        }};
    }
    
    @Override public boolean isMovementContinuous() {
        return false;
    }

    @Override public boolean isMovementCapturable() {
        return false;
    }
    
    @Override public boolean isPromotable() {
    	return true;
    }
}