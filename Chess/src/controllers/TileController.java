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

import java.util.List;

import engine.core.factories.AbstractFactory;
import engine.core.factories.ViewFactory;
import engine.core.mvc.controller.BaseController;
import models.TileModel;
import views.BoardView;
import views.TileView;

/**
 * TileController is the controller in charge of managing events fired from TileView actions 
 */
public class TileController extends BaseController {
	
	/**
	 * Global counter
	 */
	private static int counter = 0;
	
	/**
	 * The identifier numeral of this controller
	 */
	private final int identifier = ++counter;
	
	/**
	 * The Model associated to this controller
	 */
	private TileModel _tile;
	
	/**
	 * Constructs a new instance of this class
	 * 
	 * @param viewClass The view to instantiate this controller with
	 */
	public TileController(TileView viewClass) {
		super(viewClass);
		
		System.out.println("Tile Created: " + counter);
		
		// Create our model and assign to it the receivers
		_tile = new TileModel(
			getControllerProperties().getView(TileView.class), 
			AbstractFactory.getFactory(ViewFactory.class).get(BoardView.class, true)
		);
	}
	
	/**
	 * Performs a highlight command on the surrounding cells
	 *  
	 * @param enabled If it should be enabled or not
	 */
	public void highlightNeighbors(boolean enabled) {
		//_tile.getAllNeighbors();
	}
	
	@Override public String toString() {
		return String.valueOf(identifier);
	}

	public List<TileView> getNeighbors() {
		return null;
		//List<TileController> neighbors = ControllerFactory.instance().get(BoardController.class, true).getNeighbors(this);
		//return neighbors.stream().map(z -> z.getView(TileView.class)).collect(Collectors.toList());
	}
}