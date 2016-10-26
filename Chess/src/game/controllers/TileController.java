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

package game.controllers;

import java.awt.Color;
import java.awt.Image;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;

import engine.factories.ControllerFactory;
import game.models.GameModel.Operation;
import game.models.PlayerModel;
import game.models.TileModel;
import game.models.TileModel.NeighborPosition;
import game.models.TileModel.Selection;


public class TileController extends BaseController {
	
	private TileModel _tile;
	
	public TileController(TileModel tile) {
		_tile = tile;
		_tile.setController(this);
	}
	
	public TileModel tileSelected() {
		return _tile;
	}
	
	public void setNeighbors(NeighborPosition neighborPosition, TileModel... neighborTiles) {	
		_tile.setNeighbors(neighborPosition, neighborTiles);
	}

	public void processTileSelected() {
		
		PlayerController playerController = ControllerFactory.instance().get(PlayerController.class);
		
		// Makes it so that when we start a new game the first selected
		// player will start the game
		if(playerController.getCurrentPlayer() == null) {
			PlayerModel player = _tile.getPlayer();
			
			// Prevents exceptions from being thrown when determining the 
			// player at the start of the game when the tile selected has
			// no player
			if(player == null) {
				return;
			}
			
			// This ensures that the first player to start the game is based on
			// the team of the first piece that is selected for play
			playerController.setCurrentPlayer(_tile.getPlayer());
		}
		
		
		// Check if the player tries to select the piece of our opponent
		if(_tile.getPlayer() != null && _tile.getPlayer() != playerController.getCurrentPlayer()) {
			System.out.println("Cannot select other players pieces!");	
			return;
		}
		
		if(_tile.isSelected() || _tile.isGuideSelected() && _tile.getPlayer() != playerController.getCurrentPlayer()) {
			if(!_tile.isMovableTo()) {
				_tile.setSelected(Operation.PlayerPieceMoveCancel, Selection.None);
			} 
			else {
				_tile.setSelected(Operation.PlayerPieceMoveAccepted, Selection.None, true);				
			}				
		}
		else {
			if(_tile.getPlayer() == playerController.getCurrentPlayer()) {
				if(hasMoves())
				{
					_tile.setSelected(Operation.PlayerPieceSelected, Selection.MoveSelected, true);	
				}
				else 
				{
					System.out.println("Player is selecting his own tile but there are no moves");
				}
			}
			else {
				System.out.println("Player is selecting a tile that is not a valid move");
			}
		}
	}
	
	public boolean hasMoves() {
		
		Set<TileModel> neighbors = _tile.getForwardNeighbors();
		if(_tile.getIsKingTile()) {
			neighbors.addAll(_tile.getBackwardNeighbors());
		}
		
		if(neighbors.size() == 0) {
			return false;
		}

		for(TileModel neighbor : neighbors) {
			if(neighbor.isMovableTo() || neighbor.getCapturableNeighbors(_tile).size() > 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public Image getTileImage() {
		PlayerModel model = _tile.getPlayer();
		if(model != null) {
			return model.getPieceData(_tile);
		}
		
		return null;
	}
	
	public boolean hasCapturePosition() {
		
		SortedSet<TileModel> neighbors = _tile.getForwardNeighbors();
		if(_tile.getIsKingTile() || _tile.getPlayer().getPlayerPiece(_tile).getIsKinged()) {
			neighbors.addAll(_tile.getBackwardNeighbors());
		}

		for(TileModel neighbor : neighbors) {
			if(neighbor.getPlayer() != null && neighbor.getPlayer() != _tile.getPlayer()){
				Vector<TileModel> capturablePositions = neighbor.getCapturableNeighbors(_tile);
				if(capturablePositions.size() > 0) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void tileGuidesCommand(Operation operation) {
		Selection selection = operation == Operation.ShowGuides ? Selection.GuideSelected : Selection.None;
		
		SortedSet<TileModel> tileNeighbors = _tile.getForwardNeighbors();
		if(_tile.getIsKingTile() || _tile.getPlayer().getPlayerPiece(_tile).getIsKinged()) {
			tileNeighbors.addAll(_tile.getBackwardNeighbors());
		}
		
		boolean captureExists = false;
		
		// Check to see locally if a capture exists
		for(TileModel neighbor : tileNeighbors) {
			if(neighbor.getPlayer() != _tile.getPlayer()){
				Vector<TileModel> capturablePositions = neighbor.getCapturableNeighbors(_tile);
				if(capturablePositions.size() > 0) {
					for(TileModel capturablePosition : capturablePositions) {
						capturablePosition.setSelected(operation, selection);
					}
					neighbor.setSelected(operation, Selection.CaptureSelected);
					captureExists = true;
				}
			}
		}

		if(!captureExists) {
			// Before going forward, make sure that no other captures exist on the board
			if(_tile.getPlayer().hasCaptures()) {
				System.out.println("Capture exists elsewhere, please perform that!");
			}
			else {
				for(TileModel neighbor : tileNeighbors) {
					if(neighbor.isMovableTo()) {
						neighbor.setSelected(operation, selection);				
					}
				}		
			}
		}		
  	}

	public int getTileCoordinate() {
		return _tile.getIdentifier();
	}

	public Color getTileColor() {
		Color color = null;
		if(_tile != null) {
			PlayerModel player = _tile.getPlayer();
			if(player != null) {
				color = player.getTeamColor();
			}
		}
		
		return color;
	}

	@Override public void destroy() {
		_tile = null;		
	}
}
