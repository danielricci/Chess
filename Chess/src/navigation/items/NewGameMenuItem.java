package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import application.MainApplication;
import engine.core.factories.AbstractFactory;
import engine.core.factories.ViewFactory;
import engine.core.option.types.OptionItem;
import views.MainWindowView;

public class NewGameMenuItem extends OptionItem {

	public NewGameMenuItem(JComponent parent) {
		super(new JMenuItem("New Game"), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		
		// Get a reference to the view factory 
		ViewFactory factory = AbstractFactory.getFactory(ViewFactory.class);
		
		if(factory != null) {
			// Get a reference to the main window to start application
			MainWindowView view = factory.get(MainWindowView.class, true); 
			
			// Render the specified view
			view.render();
			
			// Add the view to the application
			MainApplication.instance().add(view);
			
			// Validate the application to layout all contents
			// onto the screen
			MainApplication.instance().validate();
			
		}
	}
	
	@Override public boolean enabled() {
		return true;
	}
}