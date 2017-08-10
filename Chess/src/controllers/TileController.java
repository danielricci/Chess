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

import java.util.Objects;
import java.util.logging.Level;

import engine.core.factories.AbstractFactory;
import engine.core.factories.ControllerFactory;
import engine.core.factories.ModelFactory;
import engine.core.mvc.controller.BaseController;
import engine.utils.io.logging.Tracelog;
import game.entities.ChessEntity;
import game.player.Player.PlayerTeam;
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
		
		/*
		 	1. Player clicks on their piece first 
		 		1. Player clicks on the same piece (unselects their piece)
		 		2. Player clicks on one of their other pieces (unselect old, select new)
		 		3. Player clicks on an empty tile
		 		4. Player clicks on an enemy piece
		 */
		
		
		// Get a reference to the player controller
		PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class);
		
		// 1. Player clicks on their piece first
		if(boardController.getSelectedTile() == null && playerController.getCurrentPlayerTeam() == getTileTeam()) {
		    _tile.setSelected(true);
		}
		// 2. Player clicks on the same piece, so unselect it
		else if(Objects.equals(boardController.getSelectedTile(), this._tile) && playerController.getCurrentPlayerTeam() == getTileTeam()) {
            _tile.setSelected(false);
        }
        // 3. Player clicks on one of their other pieces, so unselect the selected one and 
        //    make this tile selected
        else if(!Objects.equals(boardController.getSelectedTile(), this._tile) && playerController.getCurrentPlayerTeam() == getTileTeam()) {
            boardController.getSelectedTile().setSelected(false);
            _tile.setSelected(true);
        }
	}

	/**
	 * @return If a chess entity occupies this tile
	 */
	public boolean hasEntity() {
		return _tile.getEntity() != null;
	}
	
	public PlayerTeam getTileTeam() {
		ChessEntity entity = _tile.getEntity();
		return entity != null ? entity.getTeam() : null;
	}
}