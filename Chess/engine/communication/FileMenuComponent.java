package communication;

import javax.swing.JComponent;
import javax.swing.JMenu;

public class FileMenuComponent extends MenuComponent {

	public FileMenuComponent(JComponent parent) {
		super(new JMenu("File"), parent);
	}

	@Override
	public void onExecute() {
	}
}
