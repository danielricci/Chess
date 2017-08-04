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

import javax.swing.DefaultComboBoxModel;

import engine.core.mvc.controller.BaseController;
import game.player.Player;
import game.player.Player.PlayerTeam;
import generated.DataLookup;
import generated.DataLookup.DataLayerName;
import views.ChessPiecesView;

/**
 * The chess pieces debugger controller
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class ChessPiecesController extends BaseController {
	
	/**
	 * The model of the list of available pieces
	 */
	public final DefaultComboBoxModel<DataLayerName> _piecesListModel = new DefaultComboBoxModel(DataLookup.DataLayerName.values());

	/**
	 * The model of the list of available player teams
	 */
	public final DefaultComboBoxModel<PlayerTeam> _teamListModel = new DefaultComboBoxModel(Player.PlayerTeam.values());

	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param view The view associated to this controller
	 */
	public ChessPiecesController(ChessPiecesView view) {
		super(view);
	}
	
	/**
	 * @return A reference to the pieces model
	 */
	public DefaultComboBoxModel getPiecesModel() {
		return _piecesListModel;
	}
	
	/**
	 * @return A reference to the teams model
	 */
	public DefaultComboBoxModel getTeamsModel() {
		return _teamListModel;
	}
	
	/** 
	 * @return The currently selected piece in the debugger
	 */
	public DataLayerName getSelectedPieceDebug() {
		Object selectedItem = _piecesListModel.getSelectedItem();
		return selectedItem != null ? (DataLayerName)selectedItem : null;	
	}
	
	/**
	 * @return The currently selected team in the debugger
	 */
	public PlayerTeam getSelectedTeam() {
		Object selectedItem = _teamListModel.getSelectedItem();
		return selectedItem != null ? (PlayerTeam)selectedItem : null;
	}
}