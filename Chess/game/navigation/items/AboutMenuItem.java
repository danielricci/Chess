package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import communication.internal.command.ItemComponent;
import managers.LocalizationManager;
import managers.LocalizationManager.Resources;

public class AboutMenuItem extends ItemComponent {

	public AboutMenuItem(JComponent parent) {
		super(new JMenuItem(LocalizationManager.Get(Resources.AboutMenu)), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		JOptionPane.showMessageDialog(
			null,
			"Chess\nVersion 1.0\n\nDaniel Ricci\nthedanny09@gmail.com\nhttps:/github.com/danielricci/Chess",
			"About Chess",
			JOptionPane.INFORMATION_MESSAGE
		);
	}
}