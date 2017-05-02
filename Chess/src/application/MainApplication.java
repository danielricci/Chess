package application;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;

import engine.core.option.OptionBuilder;
import engine.core.system.Application;
import navigation.items.ExitGameMenuItem;
import navigation.items.NewGameMenuItem;
import navigation.menus.FileMenuComponent;
import resources.Resources;

public class MainApplication extends Application {

	private static MainApplication _instance;
	
	public MainApplication() {
		setTitle("Chess");
        setJMenuBar(new JMenuBar());
        setSize(new Dimension(800, 800));
        setResizable(false);
        setIconImage(new ImageIcon("data/internal/chess-icon-16.png").getImage());
	}
	
	public static MainApplication instance() {
		if(_instance == null) {
			_instance = new MainApplication();
		}
		return _instance;
	}
	
	public static void main(String[] args) {
		try {      	
        	EventQueue.invokeLater(new Runnable() {
        		@Override public void run() {
        			MainApplication.instance().setVisible(true);
            	}
            });
    	} 
		catch (Exception exception) {
        	exception.printStackTrace();
    	}
    }
	
	@Override protected void setEngineDefaults() {
		Resources.instance().addLocale(ResourceBundle.getBundle("resources/Resources", Locale.CANADA), true);
	}
	
	@Override protected void setWindowedInstanceMenu() {
		PopulateFileMenu();
		PopulateDeveloperMenu();
		PopulateWindowMenu();
		PopulateHelpMenu();
	}

	private void PopulateFileMenu() {
		OptionBuilder.start(getJMenuBar())
			.AddItem(FileMenuComponent.class)
			.AddItem(NewGameMenuItem.class)
			.AddSeparator()
			.AddItem(ExitGameMenuItem.class);
	}

	private void PopulateDeveloperMenu() {
		//ComponentBuilder.start(getJMenuBar()).AddItem(DeveloperMenuComponent.class)
		//.AddItem(DeveloperNewGameMenuItem.class).AddSeparator().AddItem(NearestNeighbourItem.class);
	}

	private void PopulateWindowMenu() {
		//ComponentBuilder.start(getJMenuBar()).AddItem(WindowMenuComponent.class).AddItem(WindowResetMenuItem.class);
	}

	private void PopulateHelpMenu() {
		//ComponentBuilder.start(getJMenuBar()).AddItem(HelpMenuComponent.class).AddItem(AboutMenuItem.class);
	}

	@Override public void flush() {
		// TODO Handle what happens when we perform a flush of the game
	}
}