package navigation.menus;

import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;

import engine.communication.internal.menu.MenuComponent;

public class WindowMenuComponent extends MenuComponent {

	public WindowMenuComponent(JComponent parent) {
		super(new JMenu("Window"), parent);
		super.get(JMenu.class).setMnemonic(KeyEvent.VK_W);
	}
}
