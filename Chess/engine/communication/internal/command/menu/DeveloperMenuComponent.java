package communication.internal.command.menu;

import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;

import communication.internal.command.MenuComponent;

public class DeveloperMenuComponent extends MenuComponent {

	public DeveloperMenuComponent(JComponent parent) {
		super(new JMenu("Developer"), parent);
		super.get(JMenu.class).setMnemonic(KeyEvent.VK_D);
	}
}
