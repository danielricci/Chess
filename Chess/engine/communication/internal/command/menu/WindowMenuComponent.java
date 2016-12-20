package communication.internal.command.menu;

import javax.swing.JComponent;
import javax.swing.JMenu;

import communication.internal.command.MenuComponent;

public class WindowMenuComponent extends MenuComponent {

	public WindowMenuComponent(JComponent parent) {
		super(new JMenu("Window"), parent);
	}

	@Override public void onExecute() {
	}
}
