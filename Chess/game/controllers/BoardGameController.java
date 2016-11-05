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

import java.awt.Color;
import java.util.Observer;
import java.util.Vector;

import api.IView;
import factories.ControllerFactory;
import models.GameModel.Operation;
import models.PlayerModel;
import models.PlayerPiece;
import models.TileModel;
import models.TileModel.Selection;
import views.BoardGameView;

public class BoardGameController extends BaseController {

	public static final int Rows = 8;
	
	private final Vector<TileModel> _tiles = new Vector<>();		
	private TileModel _previouslySelectedTile;
	
	public BoardGameController() {
		PlayerController playerController = ControllerFactory.instance().get(PlayerController.class);
		IView view = new BoardGameView(this, playerController);
		playerController.populatePlayers(view);
		view.render();
	}
	
	
	
	
	
	
	
	
	
	
	
  	public void createTile(Color tileViewColor, PlayerModel player, boolean isKingTile, Observer... observers) {		

  		// TODO - can we get rid of this disgusting crap
		TileController tileController = ControllerFactory.instance().getUnique(
			TileController.class, 
			player, 
			isKingTile,
			observers
		);
		
		_tiles.addElement(tileController.getTile());
	}

  	public void debuggerSelection(Operation operation, boolean selected) {
  		for(TileModel tile : _tiles) {
  			tile.addCachedData(operation, selected);
  		}
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

		PlayerController controller = ControllerFactory.instance().get(PlayerController.class);
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
			if(isGameOver()) {
				System.out.println("Game over");
			}
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

	private boolean isGameOver() {
		PlayerController controller = ControllerFactory.instance().get(PlayerController.class);
		Vector<PlayerModel> players = controller.getPlayers();
		
		return 
			isGameOverOneKingRemains(players) || 
			isGameOverPlayerHasNoPieces(players) ||
			isGameOverNoMoreMoves(players);	
	}
	
	private boolean isGameOverOneKingRemains(Vector<PlayerModel> players) {
		
		for(PlayerModel player : players) {
			Vector<PlayerPiece> playerPieces = player.getPlayerPieces();
			if(!(playerPieces.size() == 1 && playerPieces.firstElement().getIsKinged())) {
				return false;
			}
		}
		
		System.out.println("DRAW: Both players have only one king remaining");
		return true;	
	}
	
	private boolean isGameOverPlayerHasNoPieces(Vector<PlayerModel> players) {
		for(PlayerModel player : players) {
			Vector<PlayerPiece> playerPieces = player.getPlayerPieces();
			if(playerPieces.isEmpty()) {
				System.out.println("GAME OVER: Player has no more pieces left");
				return true;
			}
		}
		return false;
	}
	
	private boolean isGameOverNoMoreMoves(Vector<PlayerModel> players) {
		
		// TODO - implement this crap nicer than how it is done here!
		/*
		for(PlayerModel player : players) {
			Vector<TileModel> ownedTiles = player.getPlayerOwnedTiles();
			for(TileModel ownedTile : ownedTiles) {
				if(ownedTile.<TileController>getController().hasMoves()) {
					return false;
				}
			}
		}
		System.out.println("GAME OVER: Player has no more moves to make");
		*/
		return false;
	}

	@Override public void dispose() {
		_tiles.clear();
	}
}