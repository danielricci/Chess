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

package game.views.factory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

import javax.swing.JLabel;

import game.controllers.factory.TileController;
import game.models.GameModel;
import game.models.GameModel.Operation;
import game.models.PlayerModel;
import game.models.TileModel;
import game.models.TileModel.Selection;

public class TileView extends BaseView {

	private static final Color _defaultColor = Color.LIGHT_GRAY;
	private static final Color _selectedColor = Color.DARK_GRAY;
	private static final Color _guideColor = Color.BLUE;
	private static final Color _captureColor = Color.GREEN;
	private static final Color _kingTileColor = Color.CYAN;
		
	private final JLabel _tileCoordinatesLabel = new JLabel();
	
	private Image _image;
	
	private void debugger_playerColorVisibility(TileModel tile, Operation operation) {
		TileController controller = getController(TileController.class);
		Color color = controller.getTileColor();

		boolean isSelected = (boolean)tile.getCachedData(operation);
		if(!isSelected || color == null) {
			color = _defaultColor;
		}
		
		updateSelectedCommand(color);
		repaint();
	}
	
	private void tileCoordinateVisibility(boolean isVisible) { 
		_tileCoordinatesLabel.setVisible(isVisible);
    }
	
	private void kingTileVisibility(TileModel tile, Operation operation) {
		
		Color color = _defaultColor;
		if(tile.getIsKingTile()) {
			boolean isVisible = (boolean)tile.getCachedData(operation);
			if(isVisible) {
				color = _kingTileColor;
			}			
		}
		
		updateSelectedCommand(color);
		repaint();		
	}

	private void updateSelectedCommand(Color color) {
    	setBackground(color);
    }
	
    @Override protected void registerListeners() {
    	addMouseListener(new MouseAdapter() {  		    		
    		@Override public void mouseReleased(MouseEvent e) {
    			TileController controller = getController(TileController.class);
    			controller.processTileSelected();
    		}
		});
    }
    
	@Override public void update(Observable obs, Object arg) {
		
		TileModel tileModel = (TileModel)obs;
		TileController tileController = getController(TileController.class);
		
		for(Operation operation : tileModel.getOperations()) {
			switch(operation) {
			case EmptyTileSelected:
				break;
			case PlayerPieceSelected:
				updateSelectedCommand(_selectedColor);
				tileController.tileGuidesCommand(Operation.ShowGuides);
				break;
			case PlayerPieceMoveCancel:
				updateSelectedCommand(_defaultColor);
				tileController.tileGuidesCommand(Operation.HideGuides); 
				break;
			case PlayerPieceMoveAccepted:
				updateSelectedCommand(_defaultColor);
				break;
			case HideGuides:
				updateSelectedCommand(_defaultColor);
				break;
			case ShowGuides:
				updateSelectedCommand(tileModel.getSelectionType() == Selection.CaptureSelected ? _captureColor : _guideColor);
				break;
			case Debugger_PlayerTiles:
				debugger_playerColorVisibility(tileModel, operation);
				break;
			case Debugger_TileCoordinates:
				tileCoordinateVisibility(tileModel.getCachedData(operation));
				break;
			case Debugger_KingTiles:
				kingTileVisibility(tileModel, operation);
				break;
			case Refresh:
				break;
			default:
				break;
			}
			refresh(tileModel); 
		}
		
	}
		
	@Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(_image, 10, 8, 48, 48, null, null);       
	}
	
	@Override public void render() {
		// Set the coordinates of this tile
		TileController controller = getController(TileController.class);
		_tileCoordinatesLabel.setText(Integer.toString(controller.getTileCoordinate()));
		_tileCoordinatesLabel.setVisible(false);
		add(_tileCoordinatesLabel);
		
		// Set the image of this tile
		_image = controller.getTileImage();
		
		setBackground(_defaultColor);
		repaint();
	}
	
	@Override public void refresh(GameModel gameModel) {
		
		TileModel model = (TileModel)gameModel;
		PlayerModel player = model.getPlayer();

		if(player == null) {
			_image = null;
		}
		else {
			_image = player.getPieceData(model);
		}
		
		repaint();
	}

	@Override public void destroy() {
		removeAll();
	}
}