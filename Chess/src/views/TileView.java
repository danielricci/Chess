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
	private boolean _highlightNeighbors;
	
	/**
	 * The color of all the odd files
	 */
	public static final Color ODD_FILES_COLOR = new Color(209, 139, 71);
	
	/**
	 * The Color of all the even files
	 */
	public static final Color EVEN_FILES_COLOR = new Color(255, 205, 158);

	/**
     * The color used when a tile is considered selected 
     */
    private static final Color SELECTED_COLOR = Color.LIGHT_GRAY;
		
	/**
	 * The default background color of this tile
	 */
	private final Color DEFAULT_BACKGROUND_COLOR;
			
	/**
	 * The current background color of this tile
	 */
	private Color _currentBackgroundColor;
    
    
	/**
	 * Normal border style of this view
	 */
	private final Border DEFAULT_BORDER_STYLE;
	
	/**
	 * Event associated to show neighbors
	 */
	public static final String EVENT_SHOW_NEIGHBORS = "EVENT_SHOW_NEIGHBORS";
	
	/**
	 * Event associated to hiding neighbors
	 */
	public static final String EVENT_HIDE_NEIGHBORS = "EVENT_HIDE_NEIGHBORS";
		
	/**
	 * Constructs a new instance of this class type
	 */
	public TileView(Color tileColor) {
		
		// Set the controller associated to this view
		getViewProperties().setListener(AbstractSignalFactory
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
		// a tile, this will highlight the 
		// actual tile and properly displays the border of 
		// the tile in the highlight effect
		this.addMouseListener(new MouseAdapter() {
			
			@Override public void mouseEntered(MouseEvent event) {
				if(_highlightNeighbors) {
					getViewProperties().getEntity(TileController.class).showTileNeighborsDebug(true);
				}
			}
			
			@Override public void mouseExited(MouseEvent event) {				
				if(_highlightNeighbors) {
					getViewProperties().getEntity(TileController.class).showTileNeighborsDebug(false);
				}
			}
			
			@Override public void mouseReleased(MouseEvent event) {
				TileController controller = getViewProperties().getEntity(TileController.class);
				controller.performSelect();
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
				
				// Force the tile to hide its debug selection in case the mouse is already highlighting
				// some of the tiles
				getViewProperties().getEntity(TileController.class).showTileNeighborsDebug(false);
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
		// Call the super implementation
		super.update(signalEvent);
		
		// Some calls are not necessarily from tile models, so prevent this from causing damage
		if(signalEvent.getSource() instanceof TileModel) {
			
			// Get the tile model of the source
			TileModel tileModel = (TileModel) signalEvent.getSource();
			
			// If the tile model is in a selected state then update the 
			// background accordingly
			if(tileModel.getIsSelected() || tileModel.getIsHighlighted()) {				
				// Set the background, and then set the border
				// because we want it to be selected
			    this.setBackground(SELECTED_COLOR);
			}
			else {
				// Set the background back to the default state
			    this.setBackground(DEFAULT_BACKGROUND_COLOR);			    
			}
			
			// Add any renderable content
			addRenderableContent(tileModel.getEntity());
		}
		
		// Repaint the view
		repaint();
	}
}