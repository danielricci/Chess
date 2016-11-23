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

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Vector;

import api.IDebuggable;
import factories.ControllerFactory;
import models.GameModel.Operation;
import models.TileModel;
import models.TileModel.NeighborXPosition;
import models.TileModel.NeighborYPosition;
import models.TileModel.Selection;
import views.BoardView;
import views.TileView;

public class BoardGameController extends BaseController implements IDebuggable {

	private static final int _dimension = 8;

	private Vector<Vector<TileModel>> _tiles = new Vector<>(Arrays.asList(new Vector<TileModel>()));		
	private TileModel _previouslySelectedTile;
	
	public BoardGameController(BoardView view) {
		super(view);
	}
	
	public TileView createTile() {
		
		// Create a new tile controller that is linked to a corresponding view
		TileController controller = ControllerFactory.instance().get(TileController.class, true, TileView.class);
		
		// Populate the tile model and observer it with our view
		TileModel model = controller.populateTileModel(getView(BoardView.class));
		
		// Add the row to the end of the list of tiles
		_tiles.lastElement().add(model);
		
		// If we reached our dimensions limit then buffer in a new row
		if(_tiles.lastElement().size() == _dimension) {
			link();
			_tiles.addElement(new Vector<TileModel>());
		}
		
		// Return the associated view that was created
		return controller.getView(TileView.class);
	}

	private void link() {		
		
		Vector<TileModel> tilesRow = _tiles.lastElement();
		for(int i = 0; i < tilesRow.size(); ++i) {
			TileModel tile = tilesRow.get(i);
			tile.setNeighbors(
				NeighborYPosition.NEUTRAL,
				new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.LEFT, i - 1 < 0 ? null : tilesRow.get(i - 1)),
				new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.NEUTRAL, tile),
				new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.RIGHT, i + 1 == tilesRow.size() ? null : tilesRow.get(i + 1))
			);
		}
		
		if(_tiles.size() > 1) {

			// Grab the most recently populated two elements to populate their neighbors
			Vector<TileModel> firstRow = _tiles.get(_tiles.size() - 2);
			Vector<TileModel> secondRow = _tiles.lastElement();
			
			// Populate both rows neighbors
			for(int i = 0; i < firstRow.size(); ++i) {
				TileModel firstRowElement = firstRow.get(i);
				TileModel secondRowElement = secondRow.get(i);
			
				// Set the neighbors of the first rows bottom neighbors
				firstRowElement.setNeighbors(NeighborYPosition.BOTTOM, 
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.LEFT, i > 0 ? secondRow.get(i - 1) : null),
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.NEUTRAL, secondRowElement),
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.RIGHT, i + 1 < firstRow.size() ? secondRow.get(i + 1) : null)
				);
				
				// Set the neighbors of the second rows top neighbors
				secondRowElement.setNeighbors(NeighborYPosition.TOP, 
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.LEFT, i > 0 ? firstRow.get(i - 1) : null),
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.NEUTRAL, firstRowElement),
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.RIGHT, i + 1 < firstRow.size() ? firstRow.get(i + 1) : null)
				);					
			}
		}
	}
	

	public int getDimensions() {		
		return _dimension;
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
		//for(TileModel model : _tiles) {
		//	model.setSelected(Operation.HideGuides, Selection.None, true);
		//}
	}
	
	public void processTileCancel(TileModel tileModel) {
		for(TileModel model : _previouslySelectedTile.getForwardNeighbors()) {
			model.setSelected(Operation.HideGuides, Selection.None, true);
		}	
		_previouslySelectedTile = null;
	}

	// TODO - Should Models be disposed as well?



	
}