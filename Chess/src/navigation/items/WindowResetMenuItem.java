package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import engine.core.option.types.OptionItem;

public class WindowResetMenuItem extends OptionItem {
	
	public WindowResetMenuItem(JComponent parent) {
		super(new JMenuItem("Reset Menu"), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		//Application.Instance().setLocationRelativeTo(null);
	}
}