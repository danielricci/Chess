package navigation.menus;

import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;

import engine.core.option.types.OptionMenu;

public class DeveloperMenuComponent extends OptionMenu {
	public DeveloperMenuComponent(JComponent parent) {
		super(new JMenu("Developer"), parent);
		super.get(JMenu.class).setMnemonic(KeyEvent.VK_D);
	}
}
