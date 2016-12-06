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

package communication;

import java.util.Vector;

import javax.swing.JComponent;

public final class BaseComponentBuilder {

	/**
	 * The root component of this menu system
	 */
	private JComponent _root;
	
	/**
	 * The list of components in chronologically added order
	 */
	private Vector<Class<BaseComponent>> _components = new Vector<>();
	
	/**
	 * Constructs a new object of this class
	 * 
	 * @param root The root component of this menu system
	 */
	private BaseComponentBuilder(JComponent root) {
		_root = root;
	}
	
	/**
	 * Builder entry-point
	 *  
	 * @param root The root component of this menu system
	 * 
	 * @return A reference to this builder
	 */
	public static BaseComponentBuilder root(JComponent root) {
		return new BaseComponentBuilder(root);
	}
	
	/**
	 * Adds a new item of the specified component to the builder
	 * 
	 * @param component The type of component to construct
	 * 
	 * @return A reference to this builder
	 */
	public final <T extends BaseComponent> BaseComponentBuilder AddItem(Class<T> component) {
		try {
			_components.add((Class<BaseComponent>) component);
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}	
	
		return this;
	}
	
	/**
	 * Renders all components registered to this builder
	 */
	public void render() {
		BaseComponent baseComponent = null;
		for(Class<BaseComponent> component : _components) {
			try {
				BaseComponent createdComponent = component.getConstructor(JComponent.class).newInstance(
					baseComponent == null  
					? _root
					: baseComponent.getComponent());
				
				if(createdComponent instanceof MenuComponent) {
					baseComponent = createdComponent;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}
}