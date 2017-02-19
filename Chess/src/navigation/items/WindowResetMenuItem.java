package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import engine.communication.internal.command.ItemComponent;
import engine.managers.LocalizationManager;
import engine.managers.LocalizationManager.Resources;

public class WindowResetMenuItem extends ItemComponent {
	
	public WindowResetMenuItem(JComponent parent) {
		super(new JMenuItem(LocalizationManager.Get(Resources.ResetPosition)), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		//Application.Instance().setLocationRelativeTo(null);
	}
}