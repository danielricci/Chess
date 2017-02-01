package application;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;

import communication.internal.command.ComponentBuilder;
import core.mvc.view.Application;
import managers.LocalizationManager;
import managers.LocalizationManager.Resources;
import navigation.items.AboutMenuItem;
import navigation.items.DeveloperNewGameMenuItem;
import navigation.items.ExitGameMenuItem;
import navigation.items.NearestNeighbourItem;
import navigation.items.NewGameMenuItem;
import navigation.items.WindowResetMenuItem;
import navigation.menus.DeveloperMenuComponent;
import navigation.menus.FileMenuComponent;
import navigation.menus.HelpMenuComponent;
import navigation.menus.WindowMenuComponent;

public class MainApplication extends Application {

	public MainApplication() {
		setTitle(LocalizationManager.Get(Resources.ChessTitle));
        setJMenuBar(new JMenuBar());
        setSize(new Dimension(800, 800));
        setResizable(false);
        setIconImage(new ImageIcon("data/internal/chess-icon-16.png").getImage());
	}
	
	public static void main(String[] args) {
        try {
			MainApplication application = new MainApplication();
			application.setVisible(true);
        } catch (Exception exception) {
        	exception.printStackTrace();
        }
    }
	
	@Override protected void SetWindowedInstanceMenu() {
		PopulateFileMenu();
		PopulateDeveloperMenu();
		PopulateWindowMenu();
		PopulateHelpMenu();
	}

	private void PopulateFileMenu() {
		ComponentBuilder.start(getJMenuBar()).AddItem(FileMenuComponent.class).AddItem(NewGameMenuItem.class)
				.AddSeparator().AddItem(ExitGameMenuItem.class);
	}

	private void PopulateDeveloperMenu() {
		ComponentBuilder.start(getJMenuBar()).AddItem(DeveloperMenuComponent.class)
				.AddItem(DeveloperNewGameMenuItem.class).AddSeparator().AddItem(NearestNeighbourItem.class);
	}

	private void PopulateWindowMenu() {
		ComponentBuilder.start(getJMenuBar()).AddItem(WindowMenuComponent.class).AddItem(WindowResetMenuItem.class);
	}

	private void PopulateHelpMenu() {
		ComponentBuilder.start(getJMenuBar()).AddItem(HelpMenuComponent.class).AddItem(AboutMenuItem.class);
	}
}