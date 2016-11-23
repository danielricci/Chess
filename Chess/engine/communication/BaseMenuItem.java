package communication;

import javax.swing.JComponent;

import api.IMenuItem;
import controllers.BaseController;
import views.BaseView;

public abstract class BaseMenuItem implements IMenuItem {
	
	private final JComponent _component;
	
	public BaseMenuItem(JComponent component) {
		_component = component;
	}

	protected <T extends JComponent> T get(Class<T> componentClass) {
		return (T)_component;
	}
	
	@Override public boolean isVisible() {
		return _component.isVisible();
	}

	@Override public boolean isEnabled() {
		return _component.isEnabled();
	}
	
	@Override public void bind(BaseView view) {
	}

	@Override public void bind(BaseController controller) {
	}
	
	@Override public abstract void onExecute();
}