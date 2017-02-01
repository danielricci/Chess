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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import controllers.BoardController;
import controllers.TileController;
import core.mvc.controller.BaseController;
import core.mvc.view.BaseView;
import factories.ViewFactory;
import views.TileView.TileBackgroundColor;

public class BoardView extends BaseView {
	
	private final JPanel _gamePanel = new JPanel(new GridBagLayout()); 	// TODO - cant we just make BoardView have this layout and add to this	
		
	public <T extends BaseController> BoardView(Class<T> controller) {
		super(controller, true);
	}
		
	@Override public void render() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Set the constraints of views 
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
				
		// Holds the list of tile views 
		List<List<TileView>> tiles = new ArrayList<>();
		
		// Create the board view structure, row by row
		for(int row = 0, dimensionsX = BoardController.Dimensions.width; row < dimensionsX; ++row) {
			
			Vector<TileView> tileRow = new Vector<>();
			// Create a row
			for(int col =  0, dimensionsY = BoardController.Dimensions.height; col < dimensionsY; ++col) {		
				
				// Create a tile and add it to our board
				TileView view = ViewFactory.instance().get(
					TileView.class, 
					false, 
					TileController.class,
					(col + row) % 2 == 0 ? TileBackgroundColor.FirstColor :  TileBackgroundColor.SecondColor
				);

				tileRow.add(view);
				
				// Make sure that dimensions are properly mapped
				gbc.gridx = col;
				gbc.gridy = row;
				_gamePanel.add(view, gbc);
				
				// render the view
				view.render();
			}
			
			tiles.add(tileRow);
		}
		
		// Link the views together logically
		getController(BoardController.class).populateBoardNeighbors(
			tiles
			.stream().map(z -> z
			.stream().map(a -> a.getController(TileController.class)).collect(Collectors.toList()))
			.collect(Collectors.toList())
		);
		
		add(_gamePanel);
	}
}