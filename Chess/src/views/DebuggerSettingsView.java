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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import application.Application;
import controllers.BoardController;
import controllers.DebuggerController;
import engine.core.factories.AbstractFactory;
import engine.core.factories.AbstractSignalFactory;
import engine.core.factories.ControllerFactory;
import engine.core.mvc.view.DialogView;
import generated.DataLookup.DataLayerName;
import models.PlayerModel.PlayerTeam;

/**
 * The view associated to the chess debugger
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 */
public class DebuggerSettingsView extends DialogView {
	
	/**
	 * The list of pieces to select from on the UI
	 */
	private JComboBox<DataLayerName> _piecesList = new JComboBox();
	
	/**
	 * The list of players to select from on the UI
	 */
	private JComboBox<PlayerTeam> _teamList = new JComboBox();
	
	/**
	 * The start button that starts the debugging session
	 */
	private JButton _startButton = new JButton("Start");
	
	/**
	 * The stop button that stops the debugging session
	 */
	private JButton _stopButton = new JButton("Stop");
	
	/**
	 * The clear button that clears the board
	 */
	private JButton _clearButton = new JButton("Clear");
	
	/**
	 * The inspector checkbox
	 */
	private JCheckBox _inspector = new JCheckBox();
	
	/**
	 * Constructs a new instance of this class type
	 */
	public DebuggerSettingsView() {
		super(Application.instance(), "Debugger Window", 200, 300);
		
		// Prevent the properties window from being resized
		this.setResizable(false);
		
		// Sets this view to always be on top
		setAlwaysOnTop(true);
		
		// Specify the controller of this dialog
		getViewProperties().setListener(AbstractSignalFactory.getFactory(ControllerFactory.class).get(DebuggerController.class, true, this));
		
		// Set the layout manager of this dialog to be a grid-like layout
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
	}
	
	@Override public void initializeComponents() {
		
		// Populate the models held by the controller into the combo boxes.
		// Note: This needs to happen before setting the maximum size of each box
		//       or the vertical spacing will not be done properly
		DebuggerController controller = getViewProperties().getEntity(DebuggerController.class);
		
		// Teams label and the list of teams
		JPanel teamsPanel = new JPanel();
		JLabel teamsLabel = new JLabel("Teams");
		teamsPanel.add(teamsLabel);
		teamsPanel.add(_teamList);
		_teamList.setModel(controller.getTeamsModel());
		getContentPane().add(teamsPanel);
		teamsPanel.setMaximumSize(teamsPanel.getPreferredSize());
		
		// Pieces label and the list of pieces
		JPanel piecesPanel = new JPanel();
		JLabel piecesLabel = new JLabel("Pieces");
		piecesPanel.add(piecesLabel);
		piecesPanel.add(_piecesList);
		_piecesList.setModel(controller.getPiecesModel());
		getContentPane().add(piecesPanel);
		piecesPanel.setMaximumSize(piecesPanel.getPreferredSize());
		
		// Pieces label and the list of pieces
		JPanel inspectorPanel = new JPanel();
		JLabel inspectorLabel = new JLabel("Inspector");
		inspectorPanel.add(inspectorLabel);
		inspectorPanel.add(_inspector);
		getContentPane().add(inspectorPanel);
		//piecesPanel.setMaximumSize(inspectorPanel.getPreferredSize());
		
		// Action buttons and other related actions
		// for controlling the debugging scene
		JPanel actionPanel = new JPanel();
		actionPanel.add(_startButton);
		actionPanel.add(_stopButton);
		actionPanel.add(_clearButton);
		getContentPane().add(actionPanel);
		
		// Set the states of the action buttons
		_startButton.setEnabled(true);
		_stopButton.setEnabled(false);
		_clearButton.setEnabled(true);
	}
	
	@Override public void initializeComponentBindings() {
		
		// Start the game
		BoardController controller = AbstractFactory.getFactory(ControllerFactory.class).get(BoardController.class);
		
		_startButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent event) {
				_startButton.setEnabled(false);
				_piecesList.setEnabled(false);
				_teamList.setEnabled(false);
				_stopButton.setEnabled(true);
				_clearButton.setEnabled(false);
				_inspector.setEnabled(false);
				
				// Start the game
				controller.startGame();
			}
		});
		_stopButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent event) {
				_startButton.setEnabled(true);
				_piecesList.setEnabled(true);
				_teamList.setEnabled(true);
				_stopButton.setEnabled(false);
				_clearButton.setEnabled(true);
				_inspector.setEnabled(true);
				
				// Stop the game
				controller.stopGame();
				controller.clearBoardHighlights();
			}
		});
		_clearButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent event) {
				// Clear the board
				controller.clearBoard();
			}
		});
		_inspector.addActionListener(new ActionListener() {			
			@Override public void actionPerformed(ActionEvent e) {
				controller.setIsInspecting(_inspector.isSelected());				
			}
		});
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