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
import java.util.Observable;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import controllers.BaseController;
import controllers.BoardGameController;
import models.TileModel;

public class BoardView extends BaseView {
	
	private final JPanel _gamePanel = new JPanel(new GridBagLayout());	
	
	// TODO - are we using this or the other one?
	public BoardView(BoardGameController controller) {
		super(controller);
	}
	
	// TODO - are we using this or the other one?
	public <T extends BaseController> BoardView(Class<T> controller) {
		super(controller);
	}
	
	@Override public void update(Observable obs, Object arg) {
		
		super.update(obs, arg);
		
		BoardGameController boardGameController = getController(BoardGameController.class);
		TileModel tileModel = (TileModel)obs;
		/*
		for(GameModel.DispatchOperation operation : tileModel.getOperations()) {
			switch(operation) {
			case PlayerPieceSelected:
				boardGameController.processTileSelected(tileModel);
				break;
			case PlayerPieceMoveCancel:
				boardGameController.processTileCancel(tileModel);
				break;
			case PlayerPieceMoveAccepted:
				boardGameController.processTileMove(tileModel);
				break;
			}
		}
		*/
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
		BoardGameController boardGameController = getController(BoardGameController.class);
		
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
			// TODO - can we do this somewhere else?
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
}