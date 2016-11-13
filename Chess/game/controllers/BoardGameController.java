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

import java.util.Vector;

import api.IDebuggable;
import factories.ControllerFactory;
import models.GameModel.Operation;
import models.TileModel;
import models.TileModel.Selection;
import views.BaseView;
import views.BoardGameView;
import views.TileView;

public class BoardGameController extends BaseController implements IDebuggable {

	private final Vector<TileModel> _tiles = new Vector<>();		
	private TileModel _previouslySelectedTile;
	
	public BoardGameController(BoardGameView view) {
		super(view);
	}
	
	public <T extends BaseView> BoardGameController(Class<T> viewClass) {
		super(viewClass, true);
	}
	
	
	public TileView createTile() {
		
		// Create a new tile controller that is linked to a corresponding view
		TileController controller = ControllerFactory.instance().get(TileController.class, true, TileView.class);
		
		// Reference the tile model that was created
		_tiles.add(controller.getTile());
		
		// Return the associated view that was created
		return controller.getView(TileView.class);
	}
	
	public void link(Vector<TileView> tileRow) {		
	}

	public void link(Vector<TileView> elementAt, Vector<TileView> lastElement) {
	}
	
	@Override public void dispose() {
		_tiles.clear();
	}	
	
	
  	@Override public void debuggerSelection(DebugOption optionx, boolean selected) {
  		// TODO - implement me
  		//for(TileModel tile : _tiles) {
  		//	tile.addCachedData(operation, selected);
  		//}
  	}
  	
  	public void processTileSelected(TileModel tile) {
		if(_previouslySelectedTile != null) {
			_previouslySelectedTile.setSelected(Operation.PlayerPieceMoveCancel, Selection.None, true);			
			if(_previouslySelectedTile != tile) {
				tile.setSelected(Operation.PlayerPieceSelected, Selection.MoveSelected);
				_previouslySelectedTile = tile;
			}
			else {
				_previouslySelectedTile = null;				
			}
		} 
		else {
			_previouslySelectedTile = tile;	
		}
  	} 	

	public void processTileMove(TileModel captureTile) {

		PlayerController controller = ControllerFactory.instance().get(PlayerController.class, false);
		boolean tileCaptured = false;
		
		for(TileModel model : _previouslySelectedTile.getAllNeighbors()) {
			if(model.getSelectionType() == Selection.CaptureSelected && model.getAllNeighbors().contains(captureTile)) {
				tileCaptured = true;
				
				// If the tile we are capturing is a king then the player performing
				// the capture assumes control over the piece
				if(model.getPlayer().getPlayerPiece(model).getIsKinged()) {
					model.updateOwner(controller.getCurrentPlayer());
				}
				else {
					model.removeTile();					
				}
			}
		}
		
		// Removes all guides from the board
		processTileHideAllGuides();
		
		_previouslySelectedTile.swapWith(captureTile);
		_previouslySelectedTile = null;
		
		if(!tileCaptured) {
			controller.moveFinished();
		}
		else{
			System.out.println("Player can still continue playing.");
			captureTile.setSelected(Operation.PlayerPieceSelected, Selection.MoveSelected, true);	
		}
	}
	
	public void processTileHideAllGuides() {
		for(TileModel model : _tiles) {
			model.setSelected(Operation.HideGuides, Selection.None, true);
		}
	}
	
	public void processTileCancel(TileModel tileModel) {
		for(TileModel model : _previouslySelectedTile.getForwardNeighbors()) {
			model.setSelected(Operation.HideGuides, Selection.None, true);
		}	
		_previouslySelectedTile = null;
	}

	// TODO - Should Models be disposed as well?



	
}