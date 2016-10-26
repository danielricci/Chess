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

package engine.managers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import engine.factories.ControllerFactory;
import engine.factories.ViewFactory;
import engine.factories.ViewFactory.ViewType;
import game.controllers.BoardGameController;
import game.models.GameModel.Operation;
import game.views.BaseView;

public final class ViewManager extends JFrame {

	/**
	 * The singleton instance 
	 */
	private static ViewManager _instance;
	
	/**
	 * Constructor 
	 */
	private ViewManager() {
		super("Chess");
		
		// The dimensions of the window is hard-coded by default
		Dimension windowSize = new Dimension(800, 800);
		setSize(windowSize);
		setResizable(false);
		
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(
			screenSize.width / 2 - windowSize.width / 2,
			screenSize.height / 2 - windowSize.height / 2
		);

		SetWindowedInstanceListeners();
		SetWindowedInstanceMenu();
	}
	
	/**
	 * Gets the singleton reference to this class
	 * 
	 * @return The singleton reference to this class
	 */
	public static ViewManager Instance() {
		if(_instance == null) {
			_instance = new ViewManager();
		}
		return _instance;
	}
	
	/**
	 * Adds the standard set of window listeners to this window
	 */
	private void SetWindowedInstanceListeners() {
		
		// Needed to manually handle closing of the window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		// Add a listener to whenever the window is closed
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent event) {
				int response= JOptionPane.showConfirmDialog(ViewManager.Instance(), "Are you sure that you wish to exit the game?", "Exit Game", JOptionPane.YES_NO_OPTION);
				if(response == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});
	}
	
	/**
	 * Populates the menu bar and all the sub-menu items
	 */
	private void SetWindowedInstanceMenu() {
		
		final JMenuBar menu = new JMenuBar();
		PopulateFileMenu(menu);
		//PopulateDebuggerMenu(menu);
		PopulateHelpMenu(menu);
		
		setJMenuBar(menu);		
	}

	/**
	 * Adds the 'File' menu and its functionality to the specified menu bar
	 * 
	 * @param menu The menu bar to attach the functionality onto
	 */
	private void PopulateFileMenu(JMenuBar menu) {
		
		// Create the file menu 
		JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
			        
        // Set the event handler
        JMenuItem fileMenuNew = new JMenuItem(new AbstractAction("New") {       	
			@Override public void actionPerformed(ActionEvent event) {	
	    		
				ControllerFactory.instance().destroy();
				ViewFactory.instance().destroy();
				getContentPane().removeAll();					
				
				BaseView mainWindowView = ViewFactory.instance().getView(ViewType.MainWindowView);
				mainWindowView.render();
				add(mainWindowView);
				
				validate();						
			}	
			
        });

        // Set the shortcut
        fileMenuNew.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(fileMenuNew);
        fileMenu.addSeparator();
        
        // Set the event handler
        JMenuItem fileMenuExit = new JMenuItem(new AbstractAction("Exit") {
        	@Override public void actionPerformed(ActionEvent event) {
        		_instance.dispatchEvent(new WindowEvent(_instance, WindowEvent.WINDOW_CLOSING));
			}	
		});
       
        fileMenu.add(fileMenuExit);
        menu.add(fileMenu);
	}
	
	/**
	 * Adds the 'Debugger' menu and its functionality to the specified menu bar
	 * 
	 * @param menu The menu bar to attach the functionality onto
	 */
	private void PopulateDebuggerMenu(JMenuBar menu) {
		JMenu debuggerMenu = new JMenu("Debugger");			       
		
		// Tile Owners Functionality 
        JCheckBoxMenuItem tileOwners = new JCheckBoxMenuItem("Tile Owners");
        tileOwners.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				JCheckBoxMenuItem item = (JCheckBoxMenuItem)e.getItem();
				BoardGameController boardGameController = ControllerFactory.instance().get(BoardGameController.class);
				boardGameController.debuggerSelection(Operation.Debugger_PlayerTiles, item.isSelected());
			}
		});

        // Tile Coordinates Functionality
        JCheckBoxMenuItem tileCoordinates = new JCheckBoxMenuItem("Tile Coordinates");
        tileCoordinates.addItemListener(new ItemListener() {
  			@Override public void itemStateChanged(ItemEvent e) {
  				JCheckBoxMenuItem item = (JCheckBoxMenuItem)e.getItem();
  				BoardGameController boardGameController = ControllerFactory.instance().get(BoardGameController.class);
  				boardGameController.debuggerSelection(Operation.Debugger_TileCoordinates, item.isSelected());
  			}
  		});
        
        // King Tiles Functionality
        JCheckBoxMenuItem kingTiles = new JCheckBoxMenuItem("King Tiles");
        kingTiles.addItemListener(new ItemListener() {
  			@Override public void itemStateChanged(ItemEvent e) {
  				JCheckBoxMenuItem item = (JCheckBoxMenuItem)e.getItem();
  				BoardGameController boardGameController = ControllerFactory.instance().get(BoardGameController.class);
  				boardGameController.debuggerSelection(Operation.Debugger_KingTiles, item.isSelected());
  			}
  		});
        
        // Add options to the menu
        debuggerMenu.add(tileOwners);
        debuggerMenu.add(tileCoordinates);
        debuggerMenu.add(kingTiles);
        debuggerMenu.add(debuggerMenu);	
        menu.add(debuggerMenu);	
	}

	/**
	 * Adds the 'Help' menu and its functionality to the specified menu bar
	 * 
	 * @param menu The menu bar to attach the functionality onto
	 */
	private void PopulateHelpMenu(JMenuBar menu)
	{
		JMenu help = new JMenu("Help");
		help.setMnemonic('H');
		
		// Help -> About
		JMenuItem aboutItem = new JMenuItem(new AbstractAction("About") {
			@Override public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(
					ViewManager.Instance(),
					"Chess\nVersion 1.0\n\nDaniel Ricci\nthedanny09@gmail.com\nhttps:/github.com/danielricci/Chess",
					"About Chess",
					JOptionPane.INFORMATION_MESSAGE
				);
			}
		});

		help.add(aboutItem);	
		
		menu.add(help);
	}
}