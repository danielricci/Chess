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
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JTextField;

import application.Application;
import controllers.ChessPiecesController;
import engine.core.factories.AbstractSignalFactory;
import engine.core.factories.ControllerFactory;
import engine.core.mvc.view.DialogView;

/**
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 */
public class ChessPiecesView extends DialogView {

	private JTextField _pieceTextField = new JTextField("Piece");

	private JTextField _teamTextField = new JTextField("Team");
	
	private JButton _startButton = new JButton("Start");
	
	private JButton _stoptButton = new JButton("Stop");
	
	/**
	 * Constructs a new instance of this class type
	 */
	public ChessPiecesView() {
		super(Application.instance(), "Debugger Window", 350, 600);
		
		// Prevent the properties window from being resized
		this.setResizable(false);
		
		// Sets this view to always be on top
		setAlwaysOnTop(true);
		
		// Specify the controller of this dialog
		getViewProperties().setListener(AbstractSignalFactory.getFactory(ControllerFactory.class).get(ChessPiecesController.class, true, this));

		// Set the layout manager of this dialog to be a grid-like layout
		getContentPane().setLayout(new GridLayout(3, 2, 0, 0));
	}
	
	@Override public void initializeComponents() {
		
	}
	
	@Override public void initializeComponentBindings() {

	}
	
	@Override public void render() {
		super.render();

		// Positions this dialog at the middle-right of the application
		Dimension screenSize = getToolkit().getScreenSize();
		setLocation((int)(screenSize.getWidth() - getWidth()), (int)((screenSize.getHeight() - getHeight()) / 2));

		// Request focus on the window
		requestFocus();
	}
}