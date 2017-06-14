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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import engine.communication.internal.persistance.IXMLCodec;
import engine.core.option.OptionBuilder;
import engine.core.system.Application;
import navigation.items.AboutItem;
import navigation.items.ExitItem;
import navigation.items.NeighboursItem;
import navigation.items.NewGameItem;
import navigation.options.DebugOption;
import navigation.options.FileOption;
import navigation.options.HelpOption;
import resources.Resources;
import resources.Resources.ResourceKeys;

public class MainApplication extends Application implements IXMLCodec {

	/**
	 * The singleton instance of this class type
	 */
	private static MainApplication _instance;
	
	/**
	 * Constructs a new instance of this class
	 */
	private MainApplication() {
		
		// Set the title
		setTitle(Resources.instance().getLocalizedString(ResourceKeys.Title));
		
		// Set the size
        setSize(new Dimension(800, 800));
        
        // The user cannot resize the game
        setResizable(false);
        
        // Set the icon that will at the upper-left of the window
        setIconImage(new ImageIcon(Resources.instance().getLocalizedString(ResourceKeys.GameIcon)).getImage());
        
        // Add the window listener to listen in on when there is a signal to exit the game
        addWindowListener(new WindowAdapter() {
			/**
			 * Catches a closing of this JFrame so we can handle it properly
			 * 
			 * @param windowEvent The event that this window triggered
			 */
			@Override public void windowClosing(WindowEvent windowEvent) {
				// Exit the game
				System.exit(0);
			};		
		});
	}
	
	/**
	 * Gets a singleton reference to this class type
	 * 
	 * @return A singleton reference to this class type
	 */
	public static MainApplication instance() {
		if(_instance == null) {
			_instance = new MainApplication();
		}
		return _instance;
	}
	
	/**
	 * Main entry point of the application
	 * 
	 * @param args The arguments passed in to affect the game
	 */
	public static void main(String[] args) {
		try {
			// Call the event queue and invoke a new runnable
			// object to execute our game.
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
		
		// Set the default locale for our engine to recognize our localization
		Resources.instance().addLocale(ResourceBundle.getBundle("resources/Resources", Locale.CANADA), true);
	}
	
	@Override protected void setWindowedInstanceMenu() {
		
		// Populate the file menu and its entries
		PopulateFileMenu();
		
		// Populate the debug menu and its entries
		PopulateDebugMenu();
		
		// Populate the help menu and its entries
		PopulateHelpMenu();
	}

	/**
	 * Populate the file menu and its entries
	 */
	private void PopulateFileMenu() {
		OptionBuilder.start(getJMenuBar())
			.AddItem(FileOption.class)
			.AddItem(NewGameItem.class)
			.AddSeparator()
			.AddItem(ExitItem.class);
	}

	/**
	 * Populate the debug menu and its entries
	 */
	private void PopulateDebugMenu() {
		OptionBuilder.start(getJMenuBar())
			.AddItem(DebugOption.class)
			.AddItem(NeighboursItem.class);
	}
	
	/**
	 * Populate the help menu and its entries
	 */
	private void PopulateHelpMenu() {
		OptionBuilder.start(getJMenuBar())
			.AddItem(HelpOption.class)
			.AddItem(AboutItem.class);
	}

	@Override public void flush() {
		// TODO Handle what happens when we perform a flush of the game
	}
}