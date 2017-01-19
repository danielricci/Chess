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

package communication.internal.command;

import java.util.Vector;

import javax.swing.JComponent;

public final class ComponentBuilder {

    /**
     * The host of this builder
     */
    private final JComponent _host;
    
	/**
	 * The root component of this menu system
	 */
	private JComponent _root;
	
	/**
	 * The list of components in chronologically added order
	 */
	private Vector<JComponent> _components = new Vector<>();
	
	private ComponentBuilder(JComponent host) {
	    _host = host;
	}
	
	/**
	 * Builder entry-point
	 *  
	 * @param root The root component of this menu system
	 * 
	 * @return A reference to this builder
	 */
	public static ComponentBuilder start(JComponent host) {
		return new ComponentBuilder(host);
	}
	
	/**
	 * Builder entry-point
	 * 
     * @param root The root component of this menu system
     * 
     * @return A reference to this builder
	 */
	public static ComponentBuilder start(ComponentBuilder host) {
	    return start(host._root);
	}
	
	/**
	 * Sets the root of this builder
	 * 
	 * @param root The root of this builder
	 * 
	 * @return A reference to this builder
	 */
	public ComponentBuilder root(JComponent root) {
	    _root = root;
	    return this;
	}
	
	/**
	 * Adds a new item of the specified component to the builder
	 * 
	 * @param component The type of component to construct
	 * 
	 * @return A reference to this builder
	 */
	public final <T extends BaseComponent> ComponentBuilder AddItem(Class<T> component) {
		try {
		    T baseComponent = component.getConstructor(JComponent.class).newInstance(_root == null ? _host : _root);
		    if(_components.isEmpty() && _root == null) {
		        _root = baseComponent.getComponent();
		    }
		    else {
		        _components.add(baseComponent.getComponent());    
		    }
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}	
	
		return this;
	}
	
	public final <T extends BaseComponent> ComponentBuilder AddSeparator() {
		if(_root != null) {
			T component = (T) _root.getClientProperty(_root);
			component.addSeperator();			
		}
		
		return this;
	}

		
    public ComponentBuilder AddItem(ComponentBuilder builder) {
        builder._root = _root;
        _components.addAll(builder._components);
        
        return this;
    }
}