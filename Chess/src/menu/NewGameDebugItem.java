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

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import application.Application;
import engine.core.factories.AbstractSignalFactory;
import engine.core.factories.ViewFactory;
import engine.core.menu.types.MenuItem;
import game.structure.GameMode;
import resources.Resources;
import resources.Resources.ResourceKeys;
import views.MainWindowView;

/**
 * The new game menu item for the debugger
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class NewGameDebugItem extends MenuItem {

	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param parent The parent of this menu item
	 */
	public NewGameDebugItem(JComponent parent) {
		super(new JMenuItem(Resources.instance().getLocalizedString(ResourceKeys.NewGameDebug)), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		
		// Flush the application before continuing
		if(!Application.instance().flush()) {
			return;
		}

		
		// Get a reference to the view factory 
		ViewFactory factory = AbstractSignalFactory.getFactory(ViewFactory.class);
		
		// Get a reference to the main window to start application
		MainWindowView view = factory.get(MainWindowView.class, true, GameMode.DEBUG); 
			
		// Add the view to the application
		Application.instance().add(view);
		
		// Render the specified view
		view.render();
	}
	
	@Override public boolean enabled() {
		return true;
	}
}