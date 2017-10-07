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

import engine.communication.internal.signal.arguments.SignalEventArgs;
import game.components.MovementComponent;
import game.components.MovementComponent.EntityMovements;
import game.events.EntityEventArgs;
import generated.DataLookup.DataLayerName;
import models.TileModel;

/**
 * This class represents a pawn in the chess game
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
class PawnEntity extends AbstractChessEntity {
    
	/**
	 * Flag indicating if this pawn is exposed to an en-passent move
	 */
    private boolean _canReceiveEnPassent;

    /**
     * Constructs a new instance of this class type
     */
    public PawnEntity() {
        super(DataLayerName.PAWN);
    }
    
    /**
     * Gets the double movement offered by pawns
     * 
     * @return The double movement
     */
    private EntityMovements[] getDoubleMovement() {
    	return new EntityMovements[] { EntityMovements.UP, EntityMovements.UP };
    }

    @Override public List<EntityMovements[]> getMovements() {
        return new ArrayList<EntityMovements[]>() {{
            
            // This is the unit movement that all pawns support
            add(new EntityMovements[] { EntityMovements.UP });
            
            // If this pawn has not moved yet, then expose the double unit movement
            if(!hasMovedOnce()) {
                add(getDoubleMovement());    
            }
        }};
    }
    
    @Override public List<EntityMovements[]> getCapturableBoardMovements() {
        return new ArrayList<EntityMovements[]>() {{
            // This is the unit movement that all pawns support
            add(new EntityMovements[] { EntityMovements.RIGHT, EntityMovements.UP });
            add(new EntityMovements[] { EntityMovements.LEFT, EntityMovements.UP });
        }};
    }
    
    @Override public boolean isEnPassent() {
    	return true;
    }
    
    @Override public boolean isEnPassentCapturable() {
    	return _canReceiveEnPassent;
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
    
    @Override public void update(SignalEventArgs signalEvent) {
        super.update(signalEvent);
        
        if(signalEvent instanceof EntityEventArgs) {
            EntityEventArgs<TileModel> modelEventArgs = (EntityEventArgs) signalEvent;

            // Remove the en-passent flag, the chance is lost
            if(_canReceiveEnPassent) {
                _canReceiveEnPassent = false;
            }
            else if(modelEventArgs.getSource().getEntity() == this && MovementComponent.compareMovements(modelEventArgs.movements, getDoubleMovement())) {
            	// Indicate that this pawn can receive en-passent
                _canReceiveEnPassent = true;
            }
        }
    }
}