package communication;

import javax.swing.JComponent;

import api.IBaseComponent;
import controllers.BaseController;

public abstract class BaseComponent implements IBaseComponent {
	
	private final JComponent _component;
	private final JComponent _parent;
	private static final String ParentKey = "__parent__";
	
	protected BaseComponent(JComponent component, JComponent parent) {
		_component = component;
		_component.putClientProperty(component, this);
		
		_parent = parent;
		if(parent != null) {
			_parent.putClientProperty(ParentKey, parent);
			_parent.add(_component);
		}
		
		onInitialize();
	}
	
	protected final <T extends JComponent> T get(Class<T> componentClass) {
		return (T)_component;
	}
	
	@Override public boolean isVisible() {
		return _component.isVisible();
	}

	@Override public boolean isEnabled() {
		return _component.isEnabled();
	}
	
	@Override public void bind(BaseController controller) {
	}
	
	protected abstract void onInitialize();
	@Override public abstract void onExecute();
}