package communication.internal.item;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import communication.internal.ItemComponent;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;
import views.RootView;

public class AboutMenuItem extends ItemComponent {

	public AboutMenuItem(JComponent parent) {
		super(new JMenuItem("About"), parent);
	}
	
	@Override public void onExecute() {
		System.out.println("AboutMenuItem::onExecute");
	}

	@Override protected void onInitialize() {
		super.get(JMenuItem.class).addActionListener(new AbstractAction(ResourcesManager.Get(Resources.NewGame)) {       	
			@Override public void actionPerformed(ActionEvent event) {	
				JOptionPane.showMessageDialog(
						RootView.Instance(),
						"Chess\nVersion 1.0\n\nDaniel Ricci\nthedanny09@gmail.com\nhttps:/github.com/danielricci/Chess",
						"About Chess",
						JOptionPane.INFORMATION_MESSAGE
					);						
			}	
	    });
	}
}
