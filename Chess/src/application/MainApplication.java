/**
* Daniel Ricci <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package application;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import engine.core.option.OptionBuilder;
import engine.core.system.Application;
import navigation.items.AboutMenuItem;
import navigation.items.ExitGameMenuItem;
import navigation.items.NewGameMenuItem;
import navigation.menus.FileMenuComponent;
import navigation.menus.HelpMenuComponent;
import resources.Resources;
import resources.Resources.ResourceKeys;

public class MainApplication extends Application {

	private static MainApplication _instance;
	
	public MainApplication() {
		setTitle(Resources.instance().getLocalizedString(ResourceKeys.Title));
        setSize(new Dimension(800, 800));
        setResizable(false);
        setIconImage(new ImageIcon(Resources.instance().getLocalizedString(ResourceKeys.GameIcon)).getImage());
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
		PopulateHelpMenu();
	}

	private void PopulateFileMenu() {
		OptionBuilder.start(getJMenuBar())
			.AddItem(FileMenuComponent.class)
			.AddItem(NewGameMenuItem.class)
			.AddSeparator()
			.AddItem(ExitGameMenuItem.class);
	}

	private void PopulateHelpMenu() {
		OptionBuilder.start(getJMenuBar())
			.AddItem(HelpMenuComponent.class)
			.AddItem(AboutMenuItem.class);
	}

	@Override public void flush() {
		// TODO Handle what happens when we perform a flush of the game
	}
}