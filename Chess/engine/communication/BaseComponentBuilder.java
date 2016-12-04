package communication;

import java.util.Vector;

import javax.swing.JComponent;

public final class BaseComponentBuilder {

	private Vector<Class<BaseComponent>> _components = new Vector<>();
	
	private BaseComponentBuilder() {
	}
	
	public static BaseComponentBuilder root(JComponent root) {
		return new BaseComponentBuilder();
	}
	
	public <T extends MenuComponent> BaseComponentBuilder addMenu(Class<T> menuComponent) {
		return this;
	}
	
	@SafeVarargs
	public final <T extends BaseComponent> BaseComponentBuilder addMenuItems(Class<T>... baseComponent) {
		return this;
	}
	
	public void render() {	
	}
}