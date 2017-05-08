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

import java.awt.Dimension;

import engine.communication.internal.signal.ISignalListener;
import engine.core.mvc.model.BaseModel;

/**
 * The model representation of the boards properties 
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class BoardModel extends BaseModel {
	
	/**
	 * Represents the dimensions of the board being played on
	 */
	public static final Dimension DIMENSIONS;
	
	/**
	 * Represents the area of our dimensions
	 */
	public static final int AREA;
	
	/**
	 * Static constructor to initialize our 
	 */
	static {
		// Set the dimensions of the board model
		DIMENSIONS = new Dimension(8, 8);
		
		// Set the area (as a helper) to the dimensions
		AREA = DIMENSIONS.width * DIMENSIONS.height;
	}
	
	/**
	 * Constructs a new instance of this type
	 */
	public BoardModel() {
	}
	
	/**
	 * Constructs a new instance of this type
	 * 
	 * @param receivers The array of receivers that will listen in on changes
	 * 					made to this object
	 */
	public <T extends ISignalListener> BoardModel(T... receivers) {
		super(receivers);
	}
}