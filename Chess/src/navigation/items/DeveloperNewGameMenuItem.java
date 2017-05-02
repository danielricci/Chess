package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import engine.core.option.types.OptionItem;

public class DeveloperNewGameMenuItem extends OptionItem {

	public DeveloperNewGameMenuItem(JComponent parent) {
		super(new JMenuItem("New Game"), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		
		/*
		JPopupMenu popupMenu = (JPopupMenu) get().getParent(); 
		JComponent invoker = (JComponent) popupMenu.getInvoker();      
		Application application = (Application) invoker.getTopLevelAncestor();
		
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