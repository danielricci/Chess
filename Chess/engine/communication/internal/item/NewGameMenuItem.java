package communication.internal.item;

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

public class NewGameMenuItem extends ItemComponent {

	public NewGameMenuItem(JComponent parent) {
		super(new JMenuItem(ResourcesManager.Get(Resources.NewGame)), parent);
	}
	
	@Override public void onExecute() {
		System.out.println("NewGameMenuItem::onExecute");
	}

	@Override protected void onInitialize() {
		super.get(JMenuItem.class).addActionListener(new AbstractAction(ResourcesManager.Get(Resources.NewGame)) {       	
			@Override public void actionPerformed(ActionEvent event) {	
	    		
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
