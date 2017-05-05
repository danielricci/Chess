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

package navigation.options;

import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;

import engine.core.option.types.OptionMenu;
import resources.Resources;
import resources.Resources.ResourceKeys;

/**
 * This option holds elements related to debugging
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class DebugOption extends OptionMenu {
	
	//--------------------------------------------------------------
	/**
	 * Constructs a new instance of this type
	 * 
	 * @param parent The parent representing this item
	 */
	//--------------------------------------------------------------
	public DebugOption(JComponent parent) {
		
		// Create a new menu entry
		super(new JMenu(Resources.instance().getLocalizedString(ResourceKeys.Debug)), parent);

		// Set mnemonic key to open this menu
		super.get(JMenu.class).setMnemonic(KeyEvent.VK_D);
	}
}
