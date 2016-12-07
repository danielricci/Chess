package communication.internal.menu;

import javax.swing.JComponent;
import javax.swing.JMenu;

public class HelpMenuComponent extends MenuComponent {

	public HelpMenuComponent(JComponent parent) {
		super(new JMenu("Help"), parent);
	}

	@Override public void onExecute() {
	}
}
