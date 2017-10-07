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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import application.Application;
import controllers.PromotionController;
import engine.core.factories.AbstractSignalFactory;
import engine.core.factories.ControllerFactory;
import engine.core.mvc.view.DialogView;
import engine.external.SpringUtilities;
import game.entities.concrete.AbstractChessEntity;
import views.controls.PromotionButton;

/**
 * The view for the promotion of a pawn
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public class PromotionView extends DialogView {
		
	/**
	 * The original background color of the tile
	 * 
	 */
	private final Color ORIGINAL_COLOR = new JButton().getBackground();
	
	/**
	 * The selection background color of the tile
	 */
	private final Color SELECTED_COLOR = Color.LIGHT_GRAY;
	
	/**
	 * This panel holds the list of buttons that can be selected for the promotion of a pawn
	 */
	private JPanel _chessPiecesPanel = new JPanel(new SpringLayout());
	
	/**
	 * The OK Button
	 */
	private final JButton _okButton = new JButton("OK");
	
	/**
	 * The list of buttons representing the options to choose for promotion
	 */
	private List<PromotionButton> _promotionOptions = new ArrayList() {{
		add(new PromotionButton());
		add(new PromotionButton());
		add(new PromotionButton());
		add(new PromotionButton());
	}};
	
	/**
	 * Constructs a new instance of this class type
	 */
	public PromotionView() {
		super(Application.instance(), "Promotion", 200, 200);
		
		// Make it so that you cannot close the dialog
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// Set the controller for this setup
		getViewProperties().setListener(
			AbstractSignalFactory.getFactory(ControllerFactory.class)
			.get(PromotionController.class, true, this)
		);
		
		// Set the layout manager of this view
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		// Set the modal state
		setModal(true);
		
		// Make sure it is always on top
		setAlwaysOnTop(true);
		
		// Do not allow it to be resized
		setResizable(false);
		
		// Ensures that the dialog is centered on the screen w.r.t its owner
		setLocationRelativeTo(null);
	}
	
	/**
	 * Removes the selection color of all the buttons in the list
	 */
	private void resetButtonSelectionState() {
		// Clear the button selection
		for(PromotionButton buttonOption : _promotionOptions) {
			buttonOption.setBackground(ORIGINAL_COLOR);	
		}
	}
	
	@Override public void initializeComponents() {
		
		// Add the buttons into the specified panel
		for(PromotionButton button : _promotionOptions) {
			_chessPiecesPanel.add(button);
			button.setBorder(new LineBorder(Color.BLACK));
		}
		
		JPanel actionsPanel = new JPanel();
		actionsPanel.add(_okButton);
		_okButton.setEnabled(false);
		
		// Add the panel to the view
		add(_chessPiecesPanel);
		add(actionsPanel);
		
		// Turn the panel into the specified grid
		SpringUtilities.makeCompactGrid(_chessPiecesPanel, 2, 2, 0, 0, 0, 0);
	}

	@Override public void initializeComponentBindings() {
		
		// Go through each button and add a binding to handle the mouse events
		for(PromotionButton button : _promotionOptions) {
			
			button.addMouseListener(new MouseAdapter() {
				
				@Override public void mouseReleased(MouseEvent e) {
					
					// Get the button that was just selected
					PromotionButton myButton = (PromotionButton) e.getComponent();
					
					// Reset the button list state
					resetButtonSelectionState();
					
					// Set the background color
					button.setBackground(SELECTED_COLOR);
					
					// Call the controller and notify it that this entity was just selected
					getViewProperties().getEntity(PromotionController.class).setSelectedEntity(myButton.getChessEntity());
					
					// Enable the OK button
					_okButton.setEnabled(true);
					
					// required for OSX to paint the selection properly
					button.setOpaque(true);
				}
			});
		}
		
		/**
		 * Bindings for the OK button
		 */
		_okButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				getViewProperties().getEntity(PromotionController.class).applyPromotion();
				
				// Set the dialog result
				setDialogResult(JOptionPane.OK_OPTION);
				
				// Reset the button list state
				resetButtonSelectionState();
				
				// Put the button back into a disabled state
				_okButton.setEnabled(false);
				
				// Hide the dialog
				setVisible(false);
			}
		});
	}
	
	@Override public void render() {
		
		// Update the list of pieces w.r.t the current player
		getViewProperties().getEntity(PromotionController.class).updatePromotedPieces();
		
		// Render the pieces by updating the icons within our buttons
		List<AbstractChessEntity> entities = getViewProperties().getEntity(PromotionController.class).getPromotionList();
		for(int i = 0; i < _promotionOptions.size(); ++i) {
			PromotionButton button = _promotionOptions.get(i);
			button.setIcon(new ImageIcon(entities.get(i).getRenderableContent()));
			button.setChessEntity(entities.get(i));
		}
		
		// Make the UI fit its contents
		pack();
		
		// Show the content on screen
		super.render();
		
	}
}