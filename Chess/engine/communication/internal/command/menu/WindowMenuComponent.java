package communication.internal.command.menu;

import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;

import communication.internal.command.MenuComponent;

public class WindowMenuComponent extends MenuComponent {

	public WindowMenuComponent(JComponent parent) {
		super(new JMenu("Window"), parent);
		super.get(JMenu.class).setMnemonic(KeyEvent.VK_W);
	}
}
