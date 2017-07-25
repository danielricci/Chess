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

package controllers;

import engine.core.factories.AbstractFactory;
import engine.core.factories.ControllerFactory;
import engine.core.factories.ModelFactory;
import engine.core.mvc.controller.BaseController;
import models.TileModel;
import views.TileView;

/**
 * Manages a particular tile and all of its states
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public final class TileController extends BaseController {
	
	/**
	 * The tile model that represents a single tile in the game
	 */
	private final TileModel _tile;
	
	/**
	 * Constructs a new instance of this class
	 * 
	 * @param viewClass The view to instantiate this controller with
	 */
	public TileController(TileView viewClass) {
		super(viewClass);

		// Create the instance of our tile model
		_tile = AbstractFactory.getFactory(ModelFactory.class).get(TileModel.class, false);
		
		// Assign the listeners to the newly created model
		_tile.addListener(viewClass, AbstractFactory.getFactory(ControllerFactory.class).get(BoardController.class));
	}
	
	/**
	 * Sets the tile neighbors to a selected state, this is used for debugging
	 * purposes
	 * 
	 * @param selected If the neighbors should be in a selected state or not
	 */
	public void showTileNeighborsDebug(boolean selected) {
		// Go through the list of tile model neighbors
		for(TileModel tile : AbstractFactory.getFactory(ControllerFactory.class).get(BoardController.class).getAllNeighbors(_tile)) {
			tile.setSelected(selected);
		}
	}
}