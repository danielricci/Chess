package communication.internal.command;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public abstract class MenuComponent extends BaseComponent {

	protected MenuComponent(JComponent component, JComponent parent) {
		super(component, parent);
	}
	
	@Override public final boolean enabled() {
		return super.enabled();
	}

	@Override public final boolean visibility() {
		return super.visibility();
	}
	
	@Override protected void onInitialize() {
		super.get(JMenu.class).addMenuListener(new MenuListener() {
			@Override public void menuSelected(MenuEvent e) {
				JMenu menu = (JMenu)e.getSource();
				for(Component component : menu.getMenuComponents()) {
					if(component instanceof JComponent) {
						JComponent jComponent = (JComponent) component;
						Object clientProperty = jComponent.getClientProperty(jComponent);
						if(clientProperty instanceof ItemComponent) {
						    ItemComponent itemComponent = (ItemComponent) jComponent.getClientProperty(jComponent);
	                        jComponent.setEnabled(itemComponent.enabled());
	                        jComponent.setVisible(itemComponent.visibility());    
						}
					}
				}
			}
			@Override public void menuCanceled(MenuEvent e) {
			}
			@Override public void menuDeselected(MenuEvent e) {
			}			
		});
	}
}
