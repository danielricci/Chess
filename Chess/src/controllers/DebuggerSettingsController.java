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

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;

import engine.core.factories.AbstractFactory;
import engine.core.factories.ControllerFactory;
import engine.core.mvc.controller.BaseController;
import game.entities.concrete.AbstractChessEntity;
import generated.DataLookup;
import generated.DataLookup.DataLayerName;
import models.PlayerModel;
import models.PlayerModel.PlayerTeam;
import models.TileModel;
import views.DebuggerSettingsView;

/**
 * The controller associated to the debugger view
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class DebuggerSettingsController extends BaseController {
	
    /**
     * The memory mappings for keeping track of the last saved debugger state
     */
    private final Map<PlayerTeam, Map<AbstractChessEntity, TileModel>> _memory = new HashMap();
    
	/**
	 * The list of entities
	 */
	public final DefaultComboBoxModel<DataLayerName> _entityCollection = new DefaultComboBoxModel(DataLookup.DataLayerName.values());

	/**
	 * The list of player teams
	 */
	public final DefaultComboBoxModel<PlayerTeam> _teamCollection = new DefaultComboBoxModel(PlayerModel.PlayerTeam.values());

	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param view The view associated to this controller
	 * 
	 */
	public DebuggerSettingsController(DebuggerSettingsView view) {
		super(view);
		
		// This hack removes unneeded layer name options
		_entityCollection.removeElement(DataLayerName.WHITE);
		_entityCollection.removeElement(DataLayerName.BLACK);
	}
	
	/**
	 * Gets the entity collections
	 * 
	 * @return The entity collection
	 */
	public DefaultComboBoxModel getEntityCollection() {
		return _entityCollection;
	}
	
	/**
	 * Gets the team collections
	 * 
	 * @return The team collection
	 */
	public DefaultComboBoxModel getTeamCollection() {
		return _teamCollection;
	}
	
	/**
	 * Gets the currently selected entity value
	 *  
	 * @return The currently selected entity value
	 */
	public DataLayerName getSelectedEntityItem() {
		Object selectedItem = _entityCollection.getSelectedItem();
		return selectedItem != null && selectedItem instanceof DataLayerName ? (DataLayerName)selectedItem : null;	
	}
	
	/**
	 * Gets the currently selected team value
	 * 
	 * @return The currently selected team value
	 */
	public PlayerTeam getSelectedTeamItem() {
		Object selectedItem = _teamCollection.getSelectedItem();
		return selectedItem != null && selectedItem instanceof PlayerTeam ? (PlayerTeam)selectedItem : null;
	}
	   
    /**
     * Performs a memory clear of the memory structure
     */
    public void memoryClear() {
        _memory.clear();
    }

    /**
	 * Performs a memory recall of the current memory structure
	 */
	public void memoryRecall() {
	    
	    // Clear the board of its contents
        BoardController boardController = AbstractFactory.getFactory(ControllerFactory.class).get(BoardController.class);
        boardController.clearBoard();
	}

    /**
     * Performs a memory store of the current state of the memory structure
     */
    public void memoryStore() {
        
        // Clear the contents of the store
        memoryClear();
        
        // Get the player controller
        PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class);
        
        // Go through the list of teams to perform the insertion
        for(PlayerTeam team : PlayerTeam.values()) {

            // Create a new entry for this team
            Map<AbstractChessEntity, TileModel> entry = new HashMap<AbstractChessEntity, TileModel>();

            // Go through the entities of this player and insert each of them into the entry list 
            for(AbstractChessEntity entity : playerController.getPlayer(team).getEntities()) {
                entry.put(entity, entity.getTile());
            }
            
            // Insert the entry into the global memory list
            _memory.put(team,  entry);
        }
    }
    
    
    @Override public String toString() {
    
        // Create a string builder to store our strings
        StringBuilder builder = new StringBuilder();
        
        // Go through the maps teams
        for(Map.Entry<PlayerTeam, Map<AbstractChessEntity, TileModel>> entry1 : _memory.entrySet()) {
            
            // Add the team name
            builder.append(entry1.getKey().toString() + "\n");
            
            // Go through the mapping of chess entities and tile models and append them
            // into the string builder
            for(Map.Entry<AbstractChessEntity, TileModel> entry2 : entry1.getValue().entrySet()) {
                builder.append("Piece " + entry2.getKey().toString() + " is position on tile number " + entry2.getValue().toString() + "\n");
            }
        }
        
        // Return the string
        return builder.toString();
    }
}