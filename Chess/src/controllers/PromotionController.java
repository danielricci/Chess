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

		// Add the list of entities that can be used for the promotion
		_promotionList.add(AbstractChessEntity.createEntity(DataLayerName.BISHOP));
		_promotionList.add(AbstractChessEntity.createEntity(DataLayerName.KNIGHT));
		_promotionList.add(AbstractChessEntity.createEntity(DataLayerName.QUEEN));
		_promotionList.add(AbstractChessEntity.createEntity(DataLayerName.ROOK));		
	}
	
	/**
	 * Updates the promoted pieces
	 */
	public void updatePromotedPieces() {
		// Set the player
		PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class);
		_player = playerController.getCurrentPlayer();
		
		for(AbstractChessEntity entity : _promotionList) {
			entity.setPlayer(_player);
		}
	}
	
	public void setSelectedEntity() {
	
	}
	
	/**
	 * Promotes the specified chess piece to the specified chess piece
	 * 
	 * @param from The piece being promoted
	 * @param to The piece to use as promotion
	 */
	public void promote(AbstractChessEntity from, AbstractChessEntity to) {
		if(from.getTeam() == _player.getTeam() && _promotionList.contains(to)) {
			System.out.print("PROMOTING");
		}
		else {
			Tracelog.log(Level.SEVERE, true, "Could not promote the specified chess piece between of a mismatch");
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
}