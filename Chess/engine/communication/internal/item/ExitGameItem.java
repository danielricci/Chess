package communication.internal.item;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

import communication.internal.ItemComponent;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;
import views.RootView;

public class ExitGameItem extends ItemComponent {
	
	public ExitGameItem(JComponent parent) {
		super(new JMenuItem(ResourcesManager.Get(Resources.ExitMenu)), parent);
	}
	
	@Override public void onExecute() {
		System.out.println("ExitGameItem::onExecute");
	}
	
	@Override protected void onInitialize() {
		super.get(JMenuItem.class).addActionListener(new AbstractAction(ResourcesManager.Get(Resources.ExitMenu)) {       	
			@Override public void actionPerformed(ActionEvent event) {	
				RootView.Instance().dispatchEvent(new WindowEvent(RootView.Instance(), WindowEvent.WINDOW_CLOSING));
			}	
	    });
	}
}
