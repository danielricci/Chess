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

import engine.communication.internal.signal.ISignalListener;
import engine.core.mvc.model.BaseModel;

/**
 * The model representation of a tile 
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class TileModel extends BaseModel {
	
	/**
	 * Index counter for all the created tiles
	 */
	private static int INDEX = 0;
	
	/**
	 * Index of this tile
	 */
	private final int _index = ++INDEX;
	
	/**
	 * Property indicating if this tile is selected
	 */
	private boolean _selected = false;
	
	/**
	 * Constructs a new instance of this type
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
		
	@Override public String toString() {
		return String.valueOf(_index);
	}

	/**
	 * Sets the selected state of this model
	 * 
	 * @param selected If the tile model is selected
	 */
	public void setSelected(boolean selected) {
		_selected = selected;
		
		// Note: I do not set an operation status here
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
} 