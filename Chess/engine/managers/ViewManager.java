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

package managers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import factories.ControllerFactory;
import factories.ViewFactory;
import factories.ViewFactory.ViewType;
import managers.ResourcesManager.Resources;
import views.BaseView;

public final class ViewManager extends JFrame {

	/**
	 * The singleton instance 
	 */
	private static ViewManager _instance;
	
	/**
	 * Environment Arguments
	 */
	private Set<String> _environmentArgs = new HashSet<>();
	
	/**
	 * Constructor 
	 */
	private ViewManager() {
		super(ResourcesManager.Get(Resources.ChessTitle));
		
		// The dimensions of the window is hard-coded by default
		Dimension windowSize = new Dimension(800, 800);
		setSize(windowSize);
		setResizable(false);
		setIconImage(new ImageIcon("data/internal/chess-icon-16.png").getImage());
		
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(
			screenSize.width / 2 - windowSize.width / 2,
			screenSize.height / 2 - windowSize.height / 2
		);
		
		SetListeners();
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
	 * Sets the arguments for the singleton
	 * 
	 * @param args Environment arguments to add
	 */
	public void SetEnvironmentVariables(String[] args) {
		for(String arg : args)
		{ 
			_environmentArgs.add(arg.toLowerCase());	
		}
	}
	
	/**
	 * Adds the standard set of window listeners to this window
	 */
	private void SetListeners() {
		
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
		
		// Add a listener to the rendering visibility of this component
		addComponentListener(new ComponentAdapter() {
			@Override public void componentHidden(ComponentEvent e) {
				setJMenuBar(null);
			}
			@Override public void componentShown(ComponentEvent e) {
				SetWindowedInstanceMenu();
			}
		});
	}
	
	/**
	 * Populates the menu bar and all the sub-menu items
	 */
	private void SetWindowedInstanceMenu() {
		
		final JMenuBar menu = new JMenuBar();
		PopulateFileMenu(menu);
		
		if(_environmentArgs.contains("-developer"))
		{
			PopulateDeveloperMenu(menu);
			PopulateWindowMenu(menu);
			setTitle(ResourcesManager.Get(Resources.ChessTitleDeveloper));
		}
		
		PopulateHelpMenu(menu);
		setJMenuBar(menu);
		
		menu.revalidate();
		menu.repaint();
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
        JMenuItem fileMenuNew = new JMenuItem(new AbstractAction(ResourcesManager.Get(Resources.NewGame)) {       	
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
	 * Adds the 'Developer' menu and its functionality to the specified menu bar
	 * 
	 * @param menu The menu bar to attach the functionality onto
	 */
	private void PopulateDeveloperMenu(JMenuBar menu) {
		JMenu developerMenu = new JMenu("Developer");
		developerMenu.setMnemonic('D');
			        
	    // Set the event handler
	    // TODO - this needs to run a special new game
	    // TODO - should we use a builder here?
	    // TODO - we need to design how this will look
	    JMenuItem developerMenuNew = new JMenuItem(new AbstractAction(ResourcesManager.Get(Resources.NewGame)) {       	
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
	      
	    developerMenu.add(developerMenuNew);
	    menu.add(developerMenu);
	}
	
	/**
	 * Adds the 'Developer' menu and its functionality to the specified menu bar
	 * 
	 * @param menu The menu bar to attach the functionality onto
	 */
	private void PopulateWindowMenu(JMenuBar menu) {

		JMenu windowMenu = new JMenu("Window");
		windowMenu.setMnemonic('W');
			        
	    JMenuItem windowMenuResetPosition = new JMenuItem(new AbstractAction(ResourcesManager.Get(Resources.ResetPosition)) {       	
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
	      
	    windowMenu.add(windowMenuResetPosition);
	    menu.add(windowMenu);
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