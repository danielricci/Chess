package communication.internal.item;

import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import communication.internal.ItemComponent;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;
import views.RootView;

public class ExitGameMenuItem extends ItemComponent {
	
	public ExitGameMenuItem(JComponent parent) {
		super(new JMenuItem(ResourcesManager.Get(Resources.ExitMenu)), parent);
	}
	
	@Override public void onExecute() {
		RootView.Instance().dispatchEvent(new WindowEvent(RootView.Instance(), WindowEvent.WINDOW_CLOSING));
	}
}