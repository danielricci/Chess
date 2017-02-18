package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import controllers.MainWindowController;
import engine.communication.internal.command.ItemComponent;
import engine.core.mvc.view.Application;
import engine.core.mvc.view.BaseView;
import engine.factories.ControllerFactory;
import engine.factories.ViewFactory;
import engine.managers.LocalizationManager;
import engine.managers.LocalizationManager.Resources;
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