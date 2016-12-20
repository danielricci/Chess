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

import java.util.UUID;

import javax.swing.JComponent;

import api.IBaseComponent;
import controllers.BaseController;

public abstract class BaseComponent implements IBaseComponent {
	
	private final UUID _identifier = UUID.randomUUID();
	private final String ParentKey = "__parent__";
	
	private final JComponent _component;
	private final JComponent _parent;
	
	protected BaseComponent(JComponent component, JComponent parent) {

		_component = component;
		_parent = parent;
		
		_component.putClientProperty(component, this);
		
		if(parent != null) {
			if(_parent.getClientProperty(ParentKey) == null) {
				_parent.putClientProperty(ParentKey, parent);	
			}
			_parent.add(_component);
		}
		
		onInitialize();
	}
	
	protected BaseComponent(JComponent component, BaseComponent parent) {
		this(component, parent._component);
	}
	
	public final JComponent getComponent() {
		return _component;
	}
	
	public final JComponent getParentComponent() {
		return _parent;
	}
	
	@Override public final String toString() {
		return _identifier.toString();
	}
	
	@Override public final boolean equals(Object obj) {
		if(obj == null || obj instanceof BaseComponent) {
			return false;
		}
		
		BaseComponent component = (BaseComponent) obj;
		return component.toString() == this.toString();
	}
	
	protected final <T extends JComponent> T get(Class<T> componentClass) {
		return (T)_component;
	}
	
	protected final JComponent get() {
		return _component;
	}
	
	@Override public boolean visibility() {
		return _component.isVisible();
	}

	@Override public boolean enabled() {
		return _component.isEnabled();
	}
	
	@Override public void bind(BaseController controller) {
	}
	
	protected abstract void onInitialize();
	@Override public abstract void onExecute();
}