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

package menu;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import engine.communication.internal.signal.arguments.SignalEventArgs;
import engine.core.factories.AbstractFactory;
import engine.core.factories.AbstractSignalFactory;
import engine.core.factories.ViewFactory;
import engine.core.menu.types.MenuItem;
import resources.Resources;
import resources.Resources.ResourceKeys;
import views.TileView;

/**
 * The item for debugging neighboring tiles based
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class TileIdentifierItem extends MenuItem {
	
	/**
	 * Constructs a new instance of this type
	 * 
	 * @param parent The parent representing this item
	 */
	public TileIdentifierItem(JComponent parent) {
		// Create the menu item
		super(new JCheckBoxMenuItem(Resources.instance().getLocalizedString(ResourceKeys.TileIdentifier)), parent);
	}
	
	@Override protected void onReset() {
		super.onReset();
		super.get(JCheckBoxMenuItem.class).setSelected(false);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		
		// Get the desired signal to be sent out
		String signal = get(JCheckBoxMenuItem.class).isSelected() 
				? TileView.EVENT_SHOW_IDENTIFIER 
				: TileView.EVENT_HIDE_IDENTIFIER;
		
		// Send out a signal to all tile views to let them
		// know what to do for this debug mode
		AbstractFactory.getFactory(ViewFactory.class).multicastSignal(
			TileView.class,
			new SignalEventArgs(this, signal)
		);
	}
	
	@Override public boolean enabled() {
		return AbstractSignalFactory.isRunning();
	}
}