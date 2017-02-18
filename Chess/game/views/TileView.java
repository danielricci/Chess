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
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;

import controllers.TileController;
import engine.communication.internal.command.ItemComponent;
import engine.communication.internal.dispatcher.DispatcherOperation;
import engine.core.mvc.view.BaseView;

public class TileView extends BaseView {
	
	private static int _counter = 0; 
	private int _coordinate = ++_counter;;
	
	private Image _image;
	
	private Color _defaultBackgroundColor;
	private Color _currentBackgroundColor;
	private Color _previousBackgroundColor;
		
	public enum TileBackgroundColor {
		FirstColor(new Color(209, 139, 71)),
		SecondColor(new Color(255, 205, 158)),
		NeighborColor(Color.BLUE);
		
		public final Color color;
		
		TileBackgroundColor(Color color) {
			this.color = color;
		}
	}
	
	public TileView(Class<TileController> controller, TileBackgroundColor defaultBackgroundColor) {
		super(controller, false);
		
		// Set the default background color, and set the currently active color which should
		// be both the same when the initial render happens
		_defaultBackgroundColor = _currentBackgroundColor = defaultBackgroundColor.color;
	}
	
	@Override public Map<DispatcherOperation, ActionListener> getRegisteredOperations() {
		return new HashMap<DispatcherOperation, ActionListener>(){{
			put(DispatcherOperation.ToggleNeighborTiles, new ToggleNeighborTiles());
		}};
	}
		
	private class ToggleNeighborTiles extends MouseAdapter implements ActionListener {
	
		@Override public void mouseExited(MouseEvent event) {
			TileController controller = getController(TileController.class);
			List<TileView> views = controller.getNeighbors();
			for(TileView view : views) {
				view.setBackground(view._defaultBackgroundColor);
			}
		}
		
		@Override public void mouseEntered(MouseEvent event) {
			TileController controller = getController(TileController.class);
			List<TileView> views = controller.getNeighbors();
			for(TileView view : views) {
				view.setBackground(TileBackgroundColor.NeighborColor.color);
			}
		}
		
		@Override public void actionPerformed(ActionEvent actionEvent) {
			ItemComponent itemComponent = (ItemComponent) actionEvent.getSource();
			JCheckBoxMenuItem item = (JCheckBoxMenuItem)itemComponent.getComponent();
			if(item.isSelected()) {
				addMouseListener(this);
			}
			else {
				for(MouseListener listener : getMouseListeners()) {
					if(listener instanceof ToggleNeighborTiles) {
						removeMouseListener(listener);
					}
				}
			}
		}		
	}
	

	
	@Override public void setBackground(Color backgroundColor) {
		super.setBackground(backgroundColor);
		_currentBackgroundColor = backgroundColor;
		repaint();
	}
		
 	@Override public void render() {
		super.render();
		setBackground(_currentBackgroundColor);
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
	
	@Override public String toString() {
		return String.valueOf(_coordinate);
	}
}