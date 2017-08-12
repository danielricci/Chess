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

import java.util.logging.Level;

import engine.core.factories.AbstractFactory;
import engine.core.factories.ControllerFactory;
import engine.core.factories.ModelFactory;
import engine.core.mvc.controller.BaseController;
import engine.utils.io.logging.Tracelog;
import game.entities.ChessEntity;
import models.TileModel;
import views.TileView;

/**
 * Manages a particular tile and all of its states
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
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
		_tile.addListener(
			viewClass, 
			AbstractFactory.getFactory(ControllerFactory.class).get(BoardController.class)
		);
	}
	
	/**
	 * Sets the tile neighbors to a selected state, this is used for debugging
	 * purposes
	 * 
	 * @param selected If the neighbors should be in a selected state or not
	 */
	// TODO - can we make this private and remove the view dependency
	public void showTileNeighborsDebug(boolean selected) {
		// Go through the list of tile model neighbors
		for(TileModel tile : AbstractFactory.getFactory(ControllerFactory.class).get(BoardController.class).getAllNeighbors(_tile)) {
			tile.setSelected(selected);
		}
	}
	
	/**
	 * Sets the specifies chess entity to the this controller
	 * 
	 * @param entity The chess entity to add
	 */
	// TODO - can we make this private and remove the view dependency
	public void setChessEntity(ChessEntity entity) {
		_tile.setEntity(entity);
	}

	/**
	 * Performs a selection on the tile
	 */
	public void performSelect() {
		
		// Get a reference to the board controller
		BoardController boardController = AbstractFactory.getFactory(ControllerFactory.class).get(BoardController.class);
		
		// Make sure that the game is running before continuing
		if(!boardController.IsGameRunning()) {
			Tracelog.log(Level.WARNING, true, "Game is not running yet, cannot select any tiles");
			return;
		}
		
		// Toggle the tile
		_tile.setSelected(!_tile.getIsSelected());	
	}
	
	/**
	 * @return If a chess entity occupies this tile
	 */
	// TODO - can we make this private and remove the view dependency
	public boolean hasEntity() {
		return _tile.getEntity() != null;
	}
}