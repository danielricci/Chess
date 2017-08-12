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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;

import controllers.BoardController;
import controllers.DebuggerController;
import controllers.PlayerController;
import controllers.TileController;
import engine.core.factories.AbstractFactory;
import engine.core.factories.AbstractSignalFactory;
import engine.core.factories.ControllerFactory;
import engine.core.factories.ViewFactory;
import engine.utils.io.logging.Tracelog;
import game.entities.concrete.AbstractChessEntity;

/**
 * The board view for testing the game, it uses similar functionality to that of our board view
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 */
public class BoardViewTester extends BoardView {
	
	public BoardViewTester()
	{
	}
	
	@Override public void initializeComponents() {
		
		// Set the constraints of views 
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		// Get the board dimensions
		Dimension boardDimensions = getViewProperties().getEntity(BoardController.class).getBoardDimensions();
		
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
				
				view.addMouseListener(new MouseAdapter() {
					@Override public void mouseReleased(MouseEvent e) {
						
						// If the debugger window is not visible then do not go any further.
						DebuggerController debuggerController = AbstractFactory.getFactory(ControllerFactory.class).get(DebuggerController.class);
						if(debuggerController == null || !debuggerController.getControllerProperties().isViewVisible()) {
							Tracelog.log(Level.WARNING, true, "Cannot create an entity without the debugger window.");
							return;
						}
						
						// If the tile already has an entity then do not create a new one on it
						TileController tileController = view.getViewProperties().getEntity(TileController.class);
						if(tileController.hasEntity()) {
							Tracelog.log(Level.WARNING, true, "Cannot create an entity on a tile that already has an entity.");
							return;
						}
							
						// Get a reference to the player controller
						PlayerController playerController = AbstractFactory.getFactory(ControllerFactory.class).get(PlayerController.class);
						
						// Get a reference to the new entity based on the options selected in the debugger
						AbstractChessEntity entity = playerController.createEntity(debuggerController.getSelectedTeamDebug(), debuggerController.getSelectedPieceDebug());
						
						// If we got back a valid entity then add it
						if(entity != null) {
							tileController.setChessEntity(entity);
						}
					}
				});
				
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
		// override this method to avoid our super class from
		// starting the game earlier than we want it to
	}
}