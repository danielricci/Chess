package communication.internal.item;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import communication.internal.ItemComponent;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;
import views.RootView;

public class WindowResetMenuItem extends ItemComponent {
	
	public WindowResetMenuItem(JComponent parent) {
		super(new JMenuItem(ResourcesManager.Get(Resources.ResetPosition)), parent);
	}
	
	@Override public void onExecute() {
		RootView.Instance().setLocationRelativeTo(null);
	}
}