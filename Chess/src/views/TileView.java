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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import controllers.TileController;
import engine.communication.internal.signal.ISignalReceiver;
import engine.communication.internal.signal.types.SignalEvent;
import engine.core.factories.AbstractSignalFactory;
import engine.core.factories.ControllerFactory;
import engine.core.mvc.view.PanelView;
import models.TileModel;

/**
 * This view represents the visual contents of a single tile in this game
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class TileView extends PanelView {
	
	/**
	 * This flag when set to true with perform a neighbor highlight on
	 * this tile when you mouse over it
	 */
	private boolean _highlightNeighbors = false;
	
	/**
	 * Event associated to show neighbors
	 */
	public static final String EVENT_SHOW_NEIGHBORS = "EVENT_SHOW_NEIGHBORS";
	
	/**
	 * Event associated to hiding neighbors
	 */
	public static final String EVENT_HIDE_NEIGHBORS = "EVENT_HIDE_NEIGHBORS";
	
	/**
	 * The color of all the odd files
	 */
	public static final Color ODD_FILES_COLOR = new Color(209, 139, 71);
	
	/**
	 * The Color of all the even files
	 */
	public static final Color EVEN_FILES_COLOR = new Color(255, 205, 158);
	
	/**
	 * Highlight border style of this view
	 */
	private static final Border HIGHLIGHT_BORDER = BorderFactory.createLineBorder(Color.BLUE, 2);
	
	/**
	 * Normal border style of this view
	 */
	private final Border DEFAULT_BORDER_STYLE;
	
	/**
	 * The default background color of this tile
	 */
	private final Color DEFAULT_BACKGROUND_COLOR;
			
	/**
	 * The current background color of this tile
	 */
	private Color _currentBackgroundColor;
	
	/**
	 * Constructs a new instance of this class type
	 */
	public TileView(Color tileColor) {
		
		// Set the controller associated to this view
		getViewProperties().setController(AbstractSignalFactory
			.getFactory(ControllerFactory.class)
			.get(TileController.class, false, this)
		);	
		
		// Set the color that has been specified
		DEFAULT_BACKGROUND_COLOR = tileColor;
		setBackground(DEFAULT_BACKGROUND_COLOR);
		
		// Set the default border color of this tile
		// which should be the same color as the default 
		// background color of the tile
		//
		// Note: By having a border, we avoid artifact issues with
		// rendering an actual border on a particular tile when
		// no other tile has a border drawn
		DEFAULT_BORDER_STYLE = BorderFactory.createLineBorder(DEFAULT_BACKGROUND_COLOR, 2);
		setBorder(DEFAULT_BORDER_STYLE);
	}
	
	@Override public void initializeComponents() {
	}

	@Override public void initializeComponentBindings() {
		
		// Add a mouse listener to handle when mousing over
		// a tile, this will highlight the highlight of the
		// actual tile and properly displays the border of 
		// the tile in the highlight effect
		this.addMouseListener(new MouseAdapter() {
			
			// Handle when the mouse enters this tile
			@Override public void mouseEntered(MouseEvent event) {
				setBorder(HIGHLIGHT_BORDER);
			}
			
			// Handle when the mouse exits this tile
			@Override public void mouseExited(MouseEvent event) {
				setBorder(DEFAULT_BORDER_STYLE);
			}
		});
		
		// Add a mouse listener to handle when mousing over
		// a tile, this will perform the debug operation
		// for showing neighboring tiles
		this.addMouseListener(new MouseAdapter() {
			@Override public void mouseEntered(MouseEvent e) {
				if(_highlightNeighbors) {
					getViewProperties().getController(TileController.class).showTileNeighborsDebug(true);
				}
			}
			
			@Override public void mouseExited(MouseEvent e) {
				if(_highlightNeighbors) {
					getViewProperties().getController(TileController.class).showTileNeighborsDebug(false);
				}				
			}
		});
	}
	
	@Override public void registerSignalListeners() {
		
		// Register the event for showing neighbors
		registerSignalListener(EVENT_SHOW_NEIGHBORS, new ISignalReceiver<SignalEvent>() {
			@Override public void signalReceived(SignalEvent event) {
				_highlightNeighbors = true;
			}			
		});
		
		// Register the event for hiding neighbors
		registerSignalListener(EVENT_HIDE_NEIGHBORS, new ISignalReceiver<SignalEvent>() {
			@Override public void signalReceived(SignalEvent event) {
				_highlightNeighbors = false;
			}			
		});
	}
	
	@Override public void setBackground(Color backgroundColor) {
		
		// Before rendering a new background color, ensure that
		// the background color isn't already set to the specified
		// color
		if(backgroundColor != _currentBackgroundColor) {
			// Render the new background settings
			super.setBackground(backgroundColor);
			
			// Record the new background color change 
			_currentBackgroundColor = backgroundColor;
		}
	}
	
	@Override public void update(SignalEvent signalEvent) {
		if(!(signalEvent.getSource() instanceof TileModel)) {
			return;
		}
		
		TileModel tileModel = (TileModel) signalEvent.getSource();
		setBorder(tileModel.getIsSelected() ? HIGHLIGHT_BORDER : DEFAULT_BORDER_STYLE);
	}
}