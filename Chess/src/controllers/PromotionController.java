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
import java.util.List;
import java.util.logging.Level;

import engine.core.factories.AbstractFactory;
import engine.core.factories.ControllerFactory;
import engine.core.mvc.controller.BaseController;
import engine.utils.io.logging.Tracelog;
import game.entities.concrete.AbstractChessEntity;
import generated.DataLookup.DataLayerName;
import models.PlayerModel;
import models.TileModel;
import views.PromotionView;

/**
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
public final class PromotionController extends BaseController {

	/**
	 * The list of chess pieces available for promotion
	 */
	private final List<AbstractChessEntity> _promotionList = new ArrayList();
	
	/**
	 * The currently selected entity
	 */
	private AbstractChessEntity _selectedEntity;
	
	/**
	 * The tile where the promotion will take place
	 */
	private TileModel _tile;
	
	/**
	 * The player that is performing the promotion
	 */
	private PlayerModel _player;
	
	/**
	 * Constructs a new instance of this class type
	 *
	 * @param viewClass The view to instantiate this controller with
	 * 
	 */
	public PromotionController(PromotionView viewClass) {
		super(viewClass);
	}
	
	/**
	 * Sets the entity to be promoted
	 * 
	 * @param tile The tile where the promotion will take place
	 */
	public void setTile(TileModel tile) {
		_tile = tile;
	}
	
	/**
	 * Updates the promoted pieces
	 */
	public void updatePromotedPieces() {

		// Add the list of entities that can be used for the promotion
		_promotionList.clear();
		_promotionList.add(AbstractChessEntity.createEntity(DataLayerName.BISHOP));
		_promotionList.add(AbstractChessEntity.createEntity(DataLayerName.KNIGHT));
		_promotionList.add(AbstractChessEntity.createEntity(DataLayerName.QUEEN));
		_promotionList.add(AbstractChessEntity.createEntity(DataLayerName.ROOK));	

		// Set the player
		PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class);
		_player = playerController.getCurrentPlayer();
		
		for(AbstractChessEntity entity : _promotionList) {
			entity.setPlayer(_player);
		}
	}
	
	/**
	 * Sets the currently selected chess entity for the promotion
	 * 
	 * @param selectedEntity The selected chess entity for the promotion
	 */
	public void setSelectedEntity(AbstractChessEntity selectedEntity) {
		_selectedEntity = selectedEntity;	
	}
	
	/**
	 * Applies the promotion based on the selected chess entity
	 */
	public void applyPromotion() {
		
		// If the selected entity and tile are both valid
		if(_selectedEntity != null && _tile != null && _tile.getEntity() != null && _tile.getEntity().isPromotable()) {
			
			// Get the player controller
			PlayerController controller = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class);
			
			// Get the player of the tile that is on the tile in question
			PlayerModel player = controller.getPlayer(_tile.getEntity().getTeam());
			
			// Remove the entity that the player owns
			player.removeEntity(_tile.getEntity());
			
			// Replace the entity on the tile with the selected entity
			_tile.setEntity(_selectedEntity);
			
			// Perform a cleanup so that this controller doesn't hold reference needlessly
			flush();
		}
		else {
			Tracelog.log(Level.SEVERE, true, "Cannot promote the specified entity");
		}
	}

	/**
	 * Gets the promotion list of available promotions
	 * 
	 * @return The list of available promotions 
	 * 
	 */
	public List<AbstractChessEntity> getPromotionList() {
		return _promotionList;
	}
	
	@Override public boolean flush() {
		
		_promotionList.clear();
		_selectedEntity = null;
		_player = null;
		_tile = null;
		
		return super.flush() && true;
	}
}