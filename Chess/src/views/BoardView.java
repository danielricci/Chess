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

package views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import controllers.BoardController;
import controllers.PlayerController;
import controllers.TileController;
import engine.core.factories.AbstractFactory;
import engine.core.factories.AbstractSignalFactory;
import engine.core.factories.ControllerFactory;
import engine.core.factories.ViewFactory;
import engine.core.mvc.view.PanelView;
import game.entities.ChessEntity;
import game.player.Player.PlayerTeam;
import generated.DataLookup;

/**
 * This view represents the entire board, it holds all the tiles of the board
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class BoardView extends PanelView {
	
	/**
	 * Holds the game panel as a grid
	 */
	private final JPanel _gamePanel = new JPanel(new GridBagLayout());
		
	/**
	 * Constructs a new instance of this type
	 */
	public BoardView() {
		// Set the controller associated to this view
		getViewProperties().setListener(
			AbstractSignalFactory
			.getFactory(ControllerFactory.class)
			.get(BoardController.class, true, this)
		);	
		
		// Set the layout manager
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	@Override public void initializeComponents() {
		
		// Set the constraints of views 
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		// Get the board dimensions
		Dimension boardDimensions = getViewProperties().getEntity(BoardController.class).getBoardDimensions();
		
		// Get a reference to the player controller
		PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class);
		
		// Create the board view structure, row by row
		for(int row = 0, dimensionsX = boardDimensions.width; row < dimensionsX; ++row) {

						
			// Create a row
			for(int col =  0, dimensionsY = boardDimensions.height; col < dimensionsY; ++col) {		

				// Create a tile and add it to our board
				TileView view = AbstractSignalFactory.getFactory(ViewFactory.class).get(
					TileView.class, 
					false,
					(col + row) % 2 == 0 ? TileView.EVEN_FILES_COLOR : TileView.ODD_FILES_COLOR
				);
				
				// If we are on the first or last row on the board, inject the proper chess pieces
				if(row == 0 || row == dimensionsX - 1) {
					TileController tileController = view.getViewProperties().getEntity(TileController.class);
					PlayerTeam team = row == 0 ? PlayerTeam.BLACK : PlayerTeam.WHITE;
					
					switch(col) {
						case 0: {
							List<ChessEntity> entities = playerController.getEntities(team, DataLookup.DataLayerName.ROOK.toString());
							tileController.setChessEntity(entities.get(0));
							break;
						}
						case 1: {
							List<ChessEntity> entities = playerController.getEntities(team, DataLookup.DataLayerName.KNIGHT.toString());
							tileController.setChessEntity(entities.get(0));
							break;
						}
						case 2: {
							List<ChessEntity> entities = playerController.getEntities(team, DataLookup.DataLayerName.BISHOP.toString());
							tileController.setChessEntity(entities.get(0));
							break;
						}
						case 3: {
							List<ChessEntity> entities = playerController.getEntities(team, DataLookup.DataLayerName.QUEEN.toString());
							tileController.setChessEntity(entities.get(0));
							break;
						}
						case 4: {
							List<ChessEntity> entities = playerController.getEntities(team, DataLookup.DataLayerName.KING.toString());
							tileController.setChessEntity(entities.get(0));
							break;
						}
						case 5: {
							List<ChessEntity> entities = playerController.getEntities(team, DataLookup.DataLayerName.BISHOP.toString());
							tileController.setChessEntity(entities.get(1));
							break;
						}
						case 6: {
							List<ChessEntity> entities = playerController.getEntities(team, DataLookup.DataLayerName.KNIGHT.toString());
							tileController.setChessEntity(entities.get(1));
							break;
						}
						case 7: {
							List<ChessEntity> entities = playerController.getEntities(team, DataLookup.DataLayerName.ROOK.toString());
							tileController.setChessEntity(entities.get(1));
							break;
						}
					};	
				}
				// If we are on the second row or second to last row, then inject the proper pieces
				else if(row == 1 || row == dimensionsX - 2) {
					TileController tileController = view.getViewProperties().getEntity(TileController.class);
					List<ChessEntity> entities = playerController.getEntities(row == 1 ? PlayerTeam.BLACK : PlayerTeam.WHITE, DataLookup.DataLayerName.PAWN.toString());
					tileController.setChessEntity(entities.get(col));
				}
				
				// Make sure that dimensions are properly mapped
				gbc.gridx = col;
				gbc.gridy = row;
				_gamePanel.add(view, gbc);
				
				// render the view
				view.render();
			}			
		}
		
		// Add the game panel to this view
		add(_gamePanel);
	}

	@Override public void initializeComponentBindings() {
	}
		
	@Override public void render() {
		super.render();
	}
}