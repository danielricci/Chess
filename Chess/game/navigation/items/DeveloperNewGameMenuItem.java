package navigation.items;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import communication.internal.command.ItemComponent;
import managers.LocalizationManager;
import managers.LocalizationManager.Resources;

public class DeveloperNewGameMenuItem extends ItemComponent {

	public DeveloperNewGameMenuItem(JComponent parent) {
		super(new JMenuItem(LocalizationManager.Get(Resources.NewGame)), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		
		JMenuItem component = ((JMenuItem)actionEvent.getSource());
		Window window = SwingUtilities.getWindowAncestor(component.getParent());

/*
		ControllerFactory.instance().dispose();
		ViewFactory.instance().dispose();
		application.getContentPane().removeAll();					
		
		BaseView view = ViewFactory.instance().get(MainView.class, true, MainWindowController.class);
		view.render();
		
		application.add(view);
		application.validate();
		*/
	}
}