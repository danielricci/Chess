package communication.internal.command.menu;

import javax.swing.JComponent;
import javax.swing.JMenu;

import communication.internal.command.MenuComponent;

public class HelpMenuComponent extends MenuComponent {

	public HelpMenuComponent(JComponent parent) {
		super(new JMenu("Help"), parent);
	}

	
	@Override public void onExecute() {
	}
}
