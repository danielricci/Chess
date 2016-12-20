package communication.internal.command.item;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import communication.internal.command.ItemComponent;
import controllers.MainWindowController;
import factories.ControllerFactory;
import factories.ViewFactory;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;
import views.BaseView;
import views.MainView;
import views.RootView;

public class DeveloperNewGameMenuItem extends ItemComponent {

	public DeveloperNewGameMenuItem(JComponent parent) {
		super(new JMenuItem(ResourcesManager.Get(Resources.NewGame)), parent);
	}
	
	@Override public void onExecute() {
		ControllerFactory.instance().dispose();
		ViewFactory.instance().dispose();
		RootView.Instance().getContentPane().removeAll();					
		
		BaseView view = ViewFactory.instance().get(MainView.class, true, MainWindowController.class);
		view.render();
		
		RootView.Instance().add(view);
		RootView.Instance().validate();
	}
}