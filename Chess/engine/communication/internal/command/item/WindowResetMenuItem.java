package communication.internal.command.item;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import communication.internal.command.ItemComponent;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;
import views.RootView;

public class WindowResetMenuItem extends ItemComponent {
	
	public WindowResetMenuItem(JComponent parent) {
		super(new JMenuItem(ResourcesManager.Get(Resources.ResetPosition)), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		RootView.Instance().setLocationRelativeTo(null);
	}
}