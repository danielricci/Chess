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

package game.events;

import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.arguments.SignalEventArgs;
import game.components.MovementComponent.EntityMovements;
import game.components.MovementComponent.PlayerActions;
import game.entities.concrete.AbstractChessEntity;

/**
 * This event handles the storage of entities based on an event that has occurred
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class EntityEventArgs<T extends ISignalListener> extends SignalEventArgs<T> {

	/**
	 * The chess entity for this event
	 */
	public final AbstractChessEntity entity;
	
	/**
	 * Indicates if the entity is to be highlighted
	 */
	public boolean isHighlighted;
	
	/**
	 * The player action that has occurred 
	 */
	public PlayerActions playerAction;

	/**
	 * The entity movements that has occurred based on this event existing
	 */
    public EntityMovements[] movements;
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param sender The sender source
	 * @param operationName The name of the operation being performed
	 * @param entity The entity
	 */
	public EntityEventArgs(T sender, String operationName, AbstractChessEntity entity) {
		super(sender, operationName);
		this.entity = entity;
	}
}