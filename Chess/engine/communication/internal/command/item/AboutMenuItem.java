package communication.internal.command.item;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import communication.internal.command.ItemComponent;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;
import views.RootView;

public class AboutMenuItem extends ItemComponent {

	public AboutMenuItem(JComponent parent) {
		super(new JMenuItem(ResourcesManager.Get(Resources.AboutMenu)), parent);
	}
	
	@Override public void onExecute() {
		JOptionPane.showMessageDialog(
			RootView.Instance(),
			"Chess\nVersion 1.0\n\nDaniel Ricci\nthedanny09@gmail.com\nhttps:/github.com/danielricci/Chess",
			"About Chess",
			JOptionPane.INFORMATION_MESSAGE
		);
	}
}