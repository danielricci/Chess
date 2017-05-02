package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import engine.core.option.types.OptionItem;

public class ExitGameMenuItem extends OptionItem {
	
	public ExitGameMenuItem(JComponent parent) {
		super(new JMenuItem("Exit"), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		//Application.Instance().dispatchEvent(new WindowEvent(Application.Instance(), WindowEvent.WINDOW_CLOSING));
	}
}