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

package views;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import communication.internal.BaseComponentBuilder;
import communication.internal.item.AboutMenuItem;
import communication.internal.item.ExitGameItem;
import communication.internal.item.NewGameMenuItem;
import communication.internal.item.WindowResetMenuItem;
import communication.internal.menu.DeveloperMenuComponent;
import communication.internal.menu.FileMenuComponent;
import communication.internal.menu.HelpMenuComponent;
import communication.internal.menu.WindowMenuComponent;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;

public final class RootView extends JFrame {

	private static RootView _instance;
	
	private RootView() {
		super(ResourcesManager.Get(Resources.ChessTitle));
		
		setJMenuBar(new JMenuBar());
		setSize(new Dimension(800, 800));
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setIconImage(new ImageIcon("data/internal/chess-icon-16.png").getImage());  // TODO - use resources instead of hardcoded
	
		// Add a listener to whenever the window is closed
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent event) {
				int response= JOptionPane.showConfirmDialog(RootView.Instance(), "Are you sure that you wish to exit the game?", "Exit Game", JOptionPane.YES_NO_OPTION);
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
				setLocationRelativeTo(null);
				SetWindowedInstanceMenu();
			}
		});
	}
	
	public static RootView Instance() {
		if(_instance == null) {
			_instance = new RootView();
		}
		return _instance;
	}
	
	private void SetWindowedInstanceMenu() {
		PopulateFileMenu();
		PopulateDeveloperMenu();
		PopulateWindowMenu();		
		PopulateHelpMenu();
		
		getJMenuBar().revalidate();
		getJMenuBar().repaint();
	}
	private void PopulateFileMenu() {
		BaseComponentBuilder.root(getJMenuBar())
			.AddItem(FileMenuComponent.class)
			.AddItem(NewGameMenuItem.class)
			.AddItem(ExitGameItem.class)
		.render();
	}
	
	private void PopulateDeveloperMenu() {
		BaseComponentBuilder.root(getJMenuBar())
			.AddItem(DeveloperMenuComponent.class)
			.AddItem(NewGameMenuItem.class)
		.render();
	}

	private void PopulateWindowMenu() {
		BaseComponentBuilder.root(getJMenuBar())
			.AddItem(WindowMenuComponent.class)
			.AddItem(WindowResetMenuItem.class)
		.render();
	}
	
	private void PopulateHelpMenu()	{
		BaseComponentBuilder.root(getJMenuBar())
			.AddItem(HelpMenuComponent.class)
			.AddItem(AboutMenuItem.class)
		.render();
	}
}