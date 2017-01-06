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
import java.util.Arrays;
import java.util.Collection;
import java.util.Observable;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import communication.internal.dispatcher.DispatchOperation;
import controllers.BaseController;
import controllers.BoardController;

public class BoardView extends BaseView {
	
	// TODO - cant we just make BoardView have this layout and add to this
	// why the fuck do we need to have another JPanel?
	private final JPanel _gamePanel = new JPanel(new GridBagLayout());	
		
	public <T extends BaseController> BoardView(Class<T> controller) {
		super(controller);
	}
	
	@Override public void update(Observable obs, Object arg) {
		super.update(obs, arg);
		System.out.println("Calling update from BoardView");
	};
	
	@Override public void render() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Set the constraints of views 
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		// Holds all the cells that are to be rendered
		Vector<Vector<TileView>> tiles = new Vector<>();
		
		// Reference to the specified controller, in this case it is unique
		BoardController boardGameController = getController(BoardController.class);
		
		// Create the board, row by row
		for(int row = 0, dimensions = boardGameController.getDimensions(); row < dimensions; ++row) {
			
			// Create a row
			Vector<TileView> tileRow = new Vector<>();
			for(int col = 0; col < dimensions; ++col) {		

				TileView view = boardGameController.createTile();
				tileRow.add(view);
				
				gbc.gridx = col;
				gbc.gridy = row;
				_gamePanel.add(view, gbc);			
			}
			TileView.cycleBackgroundColor();
			
			tiles.add(tileRow);			
		}
		
		// Render the elements
		for(Vector<TileView> row : tiles) {
			for(TileView view : row) {
				view.render();
			}
		}
		
		add(_gamePanel);
	}

	@Override public void dispose() {
		_gamePanel.removeAll();
	}

	@Override protected Collection<DispatchOperation> getRegisteredOperations() {
		return Arrays.asList(
			DispatchOperation.CellSelected
		);
	}
}