package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import engine.core.option.types.OptionItem;

public class NewGameMenuItem extends OptionItem {

	public NewGameMenuItem(JComponent parent) {
		super(new JMenuItem("New Game"), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		/*ControllerFactory.instance().dispose();
		ViewFactory.instance().dispose();
		Application.Instance().getContentPane().removeAll();					
		
		BaseView view = ViewFactory.instance().get(MainView.class, true, MainWindowController.class);
		view.render();
		
		Application.Instance().add(view);
		Application.Instance().validate();
		*/
	}
	
	@Override public boolean enabled() {
		return false;
	}
}