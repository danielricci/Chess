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

package models;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import engine.core.mvc.model.BaseModel;
import engine.utils.io.logging.Tracelog;
import game.components.MovementComponent.PlayerDirection;
import game.entities.concrete.AbstractChessEntity;
import generated.DataLookup;
import generated.DataLookup.DataLayerName;

/**
 * Player class for the game 
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class PlayerModel extends BaseModel {

	/**
	 * The enumeration of teams available to all players 
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 *
	 */
	public enum PlayerTeam { 
		WHITE(PlayerDirection.FORWARD), 
		BLACK(PlayerDirection.BACKWARD);
		
		public final PlayerDirection direction;
		
		PlayerTeam(PlayerDirection direction) {
			this.direction = direction;
		}
	}
	
	/**
	 * The team that this player is associated
	 */
	private final PlayerTeam _team;
	
	/**
	 * The data values associated to the player
	 */
	private final List<Enum> _dataValues;
	
	/**
	 * The list of entities owned by the player
	 */
	private final List<AbstractChessEntity> _entities = new ArrayList();
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param team The team of the player
	 * @param dataValues The data values associated to the player
	 * @param generatePlayerEntities indicates if the chess pieces should be generated using the preset values or done manually
	 */
	public PlayerModel(PlayerTeam team, List<Enum> dataValues, boolean generatePlayerEntities) {
		
		_team = team;
		_dataValues = new ArrayList(dataValues);
		
		if(generatePlayerEntities) {
			generatePlayerEntities();
		}
	}
	
	/**
	 * Generates the chess piece items that the player owns by default
	 */
	private void generatePlayerEntities() {
		
		// PAWN
		for(int i = 0, width = 8; i < width; ++i) {
			createEntity(DataLookup.DataLayerName.PAWN);
		}

		// BISHOP, KNIGHT, ROOK
		for(int i = 0; i < 2; ++i) {
			createEntity(DataLookup.DataLayerName.BISHOP);
			createEntity(DataLookup.DataLayerName.KNIGHT);
			createEntity(DataLookup.DataLayerName.ROOK);
		}
		
		// KING
		createEntity(DataLookup.DataLayerName.QUEEN);
		
		// QUEEN
		createEntity(DataLookup.DataLayerName.KING);
	}
	
	/**
	 * Create a method with the specified entity name
	 * 
	 * @param entityName The name of the entity
	 * 
	 * @return The newly created entity
	 */
	public AbstractChessEntity createEntity(DataLayerName layerName) {
	    
	    // Create the chess entity based on the layer name
		AbstractChessEntity entity = AbstractChessEntity.createEntity(layerName);
		
		// If there was an invalid entry then log it
		if(entity == null) {
		    Tracelog.log(Level.SEVERE, true, "Invalid chess entity specified");
		    return null;
		}
		
		// Set the entity to this player
		entity.setPlayer(this);
		
		// Add the entity into the list of entities for this player
		_entities.add(entity);
		
		// return the created entity
		return entity;
	}
	
	/**
	 * Gets the list of entities associated to the specified type
	 * 
	 * @param entityClass The entity type
	 * 
	 * @return A list of entities
	 */
	public <T extends AbstractChessEntity> List<T> getEntities(DataLayerName layerName) {
		return (List<T>) _entities.stream().filter(z -> z.getDataLayerName() == layerName).collect(Collectors.toList());
	}
	
	/**
	 * Gets the data values associated to the player 
	 * 
	 * @return The data values associated to the player
	 */
	public List<Enum> getDataValues() {
		return _dataValues;
	}

	/**
	 * gets the team associated to the player 
	 * 
	 * @return The team associated to the player
	 */
	public PlayerTeam getTeam() {
		return _team;
	}

	/**
	 * Removes the specified entity from the player
	 * 
	 * @param entity The entity to remove
	 */
	public void removeEntity(AbstractChessEntity entity) {
		_entities.remove(entity);
	}
	
	/**
	 * Clears all the entities from the player
	 */
	public void clearEntities() {
		_entities.clear();
	}
	
	@Override public String toString() {
		return _team.toString();
	}

}
