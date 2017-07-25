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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import engine.api.IView;
import engine.core.mvc.controller.BaseController;
import game.ChessEntity;
import game.player.Player;
import game.player.Player.PlayerTeam;

/**
 * Handles interactions will all controllable players in the game
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class PlayerController extends BaseController {
	
	
	/**
	 * The list of players within the game
	 */
	private final List<Player> _players = new ArrayList();
	
	/**
	 * The player queue of the game
	 */
	private final Queue<Player> _playerQueue = new LinkedList();
	
	/**
	 * Constructs a new instance of this class type
	 */
	public <T extends IView> PlayerController() {
		super(null);
	}
	
	/**
	 * Adds a new player to the game
	 * 
	 * @param team The team of the player
	 * @param dataValues The data values of the player
	 */
	public void addPlayer(PlayerTeam team, Enum[] dataValues) {
		Player player = new Player(team, dataValues);
		player.loadItems();
		
		_players.add(player);
		_playerQueue.add(player);
	}
	
	/**
	 * Gets the specified player
	 * 
	 * @param team The team associated to the player
	 * 
	 * @return The player of the specified team
	 */
	private Player getPlayer(PlayerTeam team) {
		
		for(int i = 0; i < _players.size(); ++i) {
			if(_players.get(i).getTeam() == team) {
				return _players.get(i);
			}
		}
		
		return null;
	}

	/**
	 * Gets the list of entities of the specified type from the given player
	 * 
	 * @param team The team associated to the player
	 * @param entityClass The type of entities to get
	 * 
	 * @return A list of entities
	 */
	public <T extends ChessEntity> List<T> getEntities(PlayerTeam team, Class<T> entityClass) {
		return getPlayer(team).getEntities(entityClass);
	}
}