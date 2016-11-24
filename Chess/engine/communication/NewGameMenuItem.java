package communication;

import javax.swing.JComponent;
import javax.swing.JMenu;

public class NewGameMenuItem extends BaseMenuItem {

	public NewGameMenuItem(JComponent component) {
		super(component);
	}
	
	@Override public void bind(JMenu menu) {
		menu.add(super.get());
	}

	@Override public void onExecute() {
		System.out.println("NewGameMenuItem::onExecute");
	}
}
