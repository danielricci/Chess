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

package game.controllers.factory;

import java.util.Collections;
import java.util.Observer;
import java.util.SortedSet;
import java.util.Vector;

import game.models.PlayerModel;
import game.models.TileModel;

public class PlayerController extends BaseController {

	private final Vector<PlayerModel> _players = new Vector<>();
	private boolean _playerSelected = false;	
	
	public void populatePlayers(Observer observer) {
		PlayerModel player1 = new PlayerModel(observer);
		PlayerModel player2 = new PlayerModel(observer);

		_players.add(player1);
		_players.add(player2);
		
		System.out.println(player1);
		System.out.println(player2);
	}
	
	public PlayerModel getCurrentPlayer() {
		return _playerSelected ? _players.firstElement() : null;
	}
	
	public Vector<PlayerModel> getPlayers() {
		return new Vector<>(_players);
	}

	public PlayerModel getPlayer(int index) {
		for(PlayerModel player : _players) {
			if(player != null && player.getTileCoordinate() == index) {
				return player;
			}
		}
		return null;
	}
	
	public boolean canContinueChain(TileModel tile) {
		if(tile.getPlayer() == null || tile.getPlayer() != getCurrentPlayer()) {
			System.out.println("Warning: Trying to call canContinuePlaying on a tile that is either null or doesnt belong to the player currently playing!");
			return false;
		}
		
		SortedSet<TileModel> neighbors = tile.getForwardNeighbors();
		if(tile.getPlayer().getPlayerPiece(tile).getIsKinged()) {
			neighbors.addAll(tile.getBackwardNeighbors());
		}
		
		for(TileModel neighbor : neighbors) {
			if(neighbor.getCapturableNeighbors(tile).size() > 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public void moveFinished() {
		_players.add(_players.firstElement());
		_players.removeElement(_players.firstElement());
	}
	
	public void setCurrentPlayer(PlayerModel player) {
		if(_players.contains(player) && player != null) {
			PlayerModel firstPlayer = _players.get(_players.indexOf(player));
			_players.remove(player);
			_players.add(firstPlayer);
			Collections.reverse(_players);
			_playerSelected = true;
		}
	}

	@Override
	public void destroy() {
		_players.clear();
		PlayerModel.TEAM_INDEX = 0;
		_playerSelected = false;
	}
}