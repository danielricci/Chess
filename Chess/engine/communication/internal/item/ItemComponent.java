package communication.internal.item;

import javax.swing.JComponent;

import communication.internal.BaseComponent;

public abstract class ItemComponent extends BaseComponent{

	protected ItemComponent(JComponent component, JComponent parent) {
		super(component, parent);
	}	
}
