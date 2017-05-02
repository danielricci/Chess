package navigation.menus;

import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;

import engine.core.option.types.OptionMenu;

public class FileMenuComponent extends OptionMenu {
	public FileMenuComponent(JComponent parent) {
		super(new JMenu("File"), parent);
		super.get(JMenu.class).setMnemonic(KeyEvent.VK_F);
	}
}
