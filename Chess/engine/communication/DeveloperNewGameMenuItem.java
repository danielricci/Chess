package communication;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

import controllers.MainWindowController;
import factories.ControllerFactory;
import factories.ViewFactory;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;
import views.BaseView;
import views.MainView;
import views.RootView;

public class DeveloperNewGameMenuItem extends BaseComponent {

	public DeveloperNewGameMenuItem(JComponent parent) {
		super(new JMenuItem(), parent);
	}
	
	@Override public void onExecute() {
		System.out.println("DeveloperNewGameMenuItem::onExecute");
	}

	@Override protected void onInitialize() {
		JMenuItem item = super.get(JMenuItem.class);
		item.addActionListener(new AbstractAction(ResourcesManager.Get(Resources.NewGame)) {       	
			@Override public void actionPerformed(ActionEvent event) {	
	    		
				// Ensures everything is cleaned up before
				// starting the game
				ControllerFactory.instance().dispose();
				ViewFactory.instance().dispose();
				RootView.Instance().getContentPane().removeAll();					
				
				BaseView view = ViewFactory.instance().get(MainView.class, true, MainWindowController.class);
				view.render();
				
				RootView.Instance().add(view);
				RootView.Instance().validate();						
			}	
	    });
	}
}
