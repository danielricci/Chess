package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import communication.internal.command.ItemComponent;
import controllers.MainWindowController;
import core.mvc.view.Application;
import core.mvc.view.BaseView;
import factories.ControllerFactory;
import factories.ViewFactory;
import managers.LocalizationManager;
import managers.LocalizationManager.Resources;
import views.MainView;

public class DeveloperNewGameMenuItem extends ItemComponent {

	public DeveloperNewGameMenuItem(JComponent parent) {
		super(new JMenuItem(LocalizationManager.Get(Resources.NewGame)), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		
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
	}
}