package communication.internal.item;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

import managers.ResourcesManager;
import managers.ResourcesManager.Resources;
import views.RootView;

public class WindowResetMenuItem extends ItemComponent {
	
	public WindowResetMenuItem(JComponent parent) {
		super(new JMenuItem(ResourcesManager.Get(Resources.ResetPosition)), parent);
	}
	
	@Override public void onExecute() {
		System.out.println("WindowResetMenuItem::onExecute");
	}
	
	@Override protected void onInitialize() {
		super.get(JMenuItem.class).addActionListener(new AbstractAction(ResourcesManager.Get(Resources.ResetPosition)) {       	
				@Override public void actionPerformed(ActionEvent event) {
					RootView.Instance().setLocationRelativeTo(null);
				}					
		    });
	}
}
