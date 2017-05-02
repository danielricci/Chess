package navigation.menus;

import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;

import engine.core.option.types.OptionMenu;

public class WindowMenuComponent extends OptionMenu {
	public WindowMenuComponent(JComponent parent) {
		super(new JMenu("Window"), parent);
		super.get(JMenu.class).setMnemonic(KeyEvent.VK_W);
	}
}
