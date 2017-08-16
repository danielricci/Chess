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

package models;

import java.util.UUID;

import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.ISignalReceiver;
import engine.core.mvc.model.BaseModel;
import game.compositions.MovementComposition;
import game.entities.concrete.AbstractChessEntity;
import game.events.EntityEvent;

/**
 * The model representation of a tile 
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class TileModel extends BaseModel {
	
	/**
	 * Signal indicating that this model's selection state has changed
	 */
	public static final String EVENT_SELECTION_CHANGED = UUID.randomUUID().toString();
	
	/**
	 * Signal indicating that this model's highlight state has changed
	 */
	public static final String EVENT_HIGHLIGHT_CHANGED = UUID.randomUUID().toString();

	/**
	 * The entity associated to the tile model
	 */
	private AbstractChessEntity _entity;
	
	/**
	 * The movement composition of this tile model
	 */
	private final MovementComposition _movementComposition = new MovementComposition(this);
	
	/**
	 * Property indicating if this tile is selected
	 */
	private boolean _selected;

	/**
	 * Property indicating this tile is highlighted
	 */
    private boolean _highlighted;
	
	/**
	 * Constructs a new instance of this class type
	 */
	public TileModel() {
	}
	
	/**
	 * Constructs a new instance of this type
	 * 
	 * @param receivers The array of receivers that will listen in on changes
	 * 					made to this object
	 */
	public <T extends ISignalListener> TileModel(T... receivers) {
		super(receivers);
	}

	/**
	 * Sets the selected state of this model
	 * 
	 * @param selected If the tile model should be selected
	 */
	public void setSelected(boolean selected) {
		_selected = selected;
		setOperation(EVENT_SELECTION_CHANGED);
		doneUpdating();
	}
	
	/**
	 * Sets the highlight state of this model
	 * 
	 * @param highlighted If the tile model should be highlighted
	 */
	public void setHighlighted(boolean highlighted) {
	    _highlighted = highlighted;
        setOperation(EVENT_HIGHLIGHT_CHANGED);
        doneUpdating();
	}
		
	/**
	 * Gets if the tile model is in a selected state
	 * 
	 * @return If the tile model is selected
	 */
	public boolean getIsSelected() {
		return _selected;
	}
	
	/**
	 * Gets if the tile model is in a highlighted state
	 * 
	 * @return If the tile model is highlighted
	 */
	public boolean getIsHighlighted() {
	    return _highlighted;
	}
	
	/**
	 * Sets the entity of this tile model
	 * 
	 * @param entity The entity to associate to this tile model
	 */
	public void setEntity(AbstractChessEntity entity) {
		_entity = entity;
		doneUpdating();
	}
	
	/**
	 * @return The movement composition of this tile model
	 */
	public MovementComposition getMovementComposition() {
	    return _movementComposition;
	}
	
	/**
	 * Gets the entity associated to this tile model
	 * 
	 * @return The entity associated to this tile model
	 */
	public AbstractChessEntity getEntity() {
		return _entity;
	}
	
	@Override public void registerSignalListeners() {
		super.registerSignalListeners();
		
		// Listen to the remove layer event
		registerSignalListener(TileModel.EVENT_SELECTION_CHANGED, new ISignalReceiver<EntityEvent<AbstractChessEntity>>() {
			@Override public void signalReceived(EntityEvent<AbstractChessEntity> event) {
				setEntity(event.entity);
			}
		});
	}
}