package communication.internal.menu;

import javax.swing.JComponent;
import javax.swing.JMenu;

public class WindowMenuComponent extends MenuComponent {

	public WindowMenuComponent(JComponent parent) {
		super(new JMenu("Window"), parent);
	}

	@Override public void onExecute() {
	}
}
