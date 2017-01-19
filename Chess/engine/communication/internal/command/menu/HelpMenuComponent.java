package communication.internal.command.menu;

import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;

import communication.internal.command.MenuComponent;

public class HelpMenuComponent extends MenuComponent {

	public HelpMenuComponent(JComponent parent) {
		super(new JMenu("Help"), parent);
		super.get(JMenu.class).setMnemonic(KeyEvent.VK_H);
	}
}
