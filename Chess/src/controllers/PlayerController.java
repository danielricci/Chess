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

import engine.communication.internal.signal.arguments.SignalEventArgs;
import engine.core.mvc.controller.BaseController;
import engine.utils.io.logging.Tracelog;
import game.entities.concrete.AbstractChessEntity;
import generated.DataLookup.DataLayerName;
import models.PlayerModel;
import models.PlayerModel.PlayerTeam;

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
	private final List<PlayerModel> _players = new ArrayList();
	
	/**
	 * The player queue of the game
	 */
	private final Queue<PlayerModel> _playerTurnQueue = new LinkedList();
	
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
	public void addPlayer(PlayerModel player) {
		
		if(_players.contains(player)) {
			Tracelog.log(Level.WARNING, true, "Attempting to add a player to the player controller that already exists.");
			return;
		}

		// Add the player to the list of players that are playing
		// Note: Do not add the players into the queue until the game starts
		_players.add(player);
	}
	
	/**
	 * Creates a chess entity for the specified player
	 * 
	 * @param team The team of the player
	 * @param dataLayerName The name of the entity
	 * 
	 * @return The newly created entity
	 */
	public AbstractChessEntity createEntity(PlayerTeam team, DataLayerName dataLayerName) {
	
		// Get the player model of the specified team
		PlayerModel player = getPlayer(team);
		
		// Create the specified entity and return it
		return player.createEntity(dataLayerName);
	}
	
	/**
	 * Finishes the current players turn and gives the turn to the next player
	 */
	public void nextPlayer() {
		// Swap the players
	    PlayerModel player = _playerTurnQueue.poll();
		_playerTurnQueue.add(player);
		
		// Log player turn swapping taking place
		Tracelog.log(
	        Level.INFO, 
	        true, 
	        String.format("Player %s has stopped playing and it is now %s's turn", player.toString(), _playerTurnQueue.peek().toString())
        );
	}
	
	/**
	 * Gets the player that is currently playing 
	 * 
	 * @return The current player that is playing
	 */
	public PlayerModel getCurrentPlayer() {
		return _playerTurnQueue.peek();
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
	public List<AbstractChessEntity> getEntities(PlayerTeam team, DataLayerName layerName) {
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
	public PlayerModel getPlayer(PlayerTeam team) {
		
		// Go through the list of player and find the 
		// player that matches the specified player team
		for(int i = 0; i < _players.size(); ++i) {
			if(_players.get(i).getTeam() == team) {
				return _players.get(i);
			}
		}
		
		return null;
	}

	/**
	 * Queue's the list of players in the game
	 */
	public void queuePlayers() {
		
		// Clear the current list of players from the queue
		_playerTurnQueue.clear();
		
		// Note: Add player white first before black
		_playerTurnQueue.add(getPlayer(PlayerTeam.WHITE));
		_playerTurnQueue.add(getPlayer(PlayerTeam.BLACK));
		
		for(PlayerModel player : _playerTurnQueue) {
			Tracelog.log(Level.INFO, true, "Adding " + player.toString() + " to the queue");
		}
	}
	
	@Override public void update(SignalEventArgs signalEvent) {
	    super.update(signalEvent);
	    for(PlayerModel player : _playerTurnQueue) {
	        player.update(signalEvent);
	    }
	}
}