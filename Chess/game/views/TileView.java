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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import communication.internal.dispatcher.DispatcherOperation;
import controllers.TileController;
import models.GameModel;

public class TileView extends BaseView {
	
	public enum TileBackgroundColor {
		FirstColor(new Color(209, 139, 71)),
		SecondColor(new Color(255, 205, 158));
		
		public final Color color;
		
		TileBackgroundColor(Color color) {
			this.color = color;
		}
	}
	
	private Image _image;
	
	private Color _defaultBackgroundColor;
	private Color _currentBackgroundColor;
		
	private class ToggleNeighborTiles implements ActionListener {
		@Override public void actionPerformed(ActionEvent actionEvent) {
			addMouseListener(new MouseAdapter() {
				@Override public void mouseEntered(MouseEvent event) {
					getController(TileController.class).highlightNeighbors(true);
				}
				@Override public void mouseExited(MouseEvent event) {
					getController(TileController.class).highlightNeighbors(false);
				}
			});
		}		
	}
	
	public TileView(Class<TileController> controller) {
		super(controller);
	}
	
	public void setDefaultBackgroundColor(TileBackgroundColor backgroundColor) {
		_defaultBackgroundColor = _currentBackgroundColor = backgroundColor.color;
	}
	
	@Override public void setBackground(Color backgroundColor) {
		super.setBackground(backgroundColor);
		_currentBackgroundColor = backgroundColor;
	}
		
    @Override public void register() {
    	addMouseListener(new MouseAdapter() {  		    		
    		@Override public void mouseEntered(MouseEvent e) {
    		}
    		@Override public void mouseExited(MouseEvent e) {
    		}
		});
    }
    
	@Override public Map<DispatcherOperation, ActionListener> getRegisteredOperations() {
		return new HashMap<DispatcherOperation, ActionListener>(){{
			put(DispatcherOperation.ToggleNeighborTiles, new ToggleNeighborTiles());
		}};
	}
	
	@Override public void render() {
		
		super.render();
		setBackground(_currentBackgroundColor);
		repaint();
	}
	
	@Override public void refresh(GameModel gameModel) {
		super.refresh(gameModel);
		repaint();		
	}

	@Override public void dispose() {
		removeAll();
	}
	
	@Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(_image, 10, 8, 48, 48, null, null);       
	}
}