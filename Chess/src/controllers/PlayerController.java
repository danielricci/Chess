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
import java.util.logging.Level;

import engine.core.mvc.controller.BaseController;
import engine.utils.io.logging.Tracelog;
import game.entities.ChessEntity;
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
	private final Queue<Player> _playerTurnQueue = new LinkedList();
	
	/**
	 * Constructs a new instance of this class type
	 */
	public PlayerController() {
		super(null);
	}
	
	/**
	 * Adds a new player to the game
	 * 
	 * @param player The player to add
	 * 
	 */
	public void addPlayer(Player player) {
		
		if(_players.contains(player)) {
			Tracelog.log(Level.WARNING, true, "Attempting to add a player to the player controller that already exists.");
			return;
		}
		
		// Add the player to both the player turn queue
		// and the regular player list
		_players.add(player);
		_playerTurnQueue.add(player);
	}
	
	/**
	 * Creates a chess entity for the specified player
	 * 
	 * @param team The team of the player
	 * @param name The name of the entity
	 * @return The newly created entity
	 */
	public ChessEntity createEntity(PlayerTeam team, String name) {
		Player player = getPlayer(team);
		return player.createEntity(name);
	}
	
	/**
	 * Finishes the current players turns and gives the turn to the next player
	 */
	public void nextPlayer() {
		// Swap the players
		_playerTurnQueue.add(_playerTurnQueue.poll());
		Tracelog.log(Level.INFO, true, "Player " + _playerTurnQueue.peek().toString() + " is now playing");
	}
	
	/**
	 * @return The team of the player currently playing
	 */
	public PlayerTeam getCurrentPlayerTeam() {
		return _playerTurnQueue.peek().getTeam();
	}

	/**
	 * Gets the entities of the specified layer name
	 * 
	 * @param team The player team
	 * @param layerName The name of the layer
	 * 
	 * @return The list of entities associated to the specified layer that the player still owns
	 */
	public List<ChessEntity> getEntities(PlayerTeam team, String layerName) {
		
		// Get the list of entities associated to the player of the specified layer name
		return getPlayer(team).getEntities(layerName);
	}
	
	/**
	 * Gets the specified player
	 * 
	 * @param team The team associated to the player
	 * 
	 * @return The player of the specified team
	 */
	private Player getPlayer(PlayerTeam team) {
		
		// Go through the list of player and find the 
		// player that matches the specified player team
		for(int i = 0; i < _players.size(); ++i) {
			if(_players.get(i).getTeam() == team) {
				return _players.get(i);
			}
		}
		
		return null;
	}
}