package communication.internal.command.menu;

import javax.swing.JComponent;
import javax.swing.JMenu;

import communication.internal.command.MenuComponent;

public class FileMenuComponent extends MenuComponent {

	public FileMenuComponent(JComponent parent) {
		super(new JMenu("File"), parent);
	}
	
	@Override public void onExecute() {
	}
}
