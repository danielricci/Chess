package navigation.menus;

import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;

import engine.core.option.types.OptionMenu;

public class HelpMenuComponent extends OptionMenu {
	public HelpMenuComponent(JComponent parent) {
		super(new JMenu("Help"), parent);
		super.get(JMenu.class).setMnemonic(KeyEvent.VK_H);
	}
}
