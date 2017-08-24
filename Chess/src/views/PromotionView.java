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
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import application.Application;
import controllers.PromotionController;
import engine.core.factories.AbstractSignalFactory;
import engine.core.factories.ControllerFactory;
import engine.core.mvc.view.DialogView;
import engine.external.SpringUtilities;
import engine.utils.io.logging.Tracelog;
import game.entities.concrete.AbstractChessEntity;

/**
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public class PromotionView extends DialogView {
		
	/**
	 * This panel holds the list of buttons that can be selected for the promotion
	 * of a pawn
	 */
	private JPanel _chessPiecesPanel = new JPanel(new SpringLayout());
		
	/**
	 * The list of buttons representing the options to choose for promotion
	 */
	private List<JButton> _promotionOptions = new ArrayList() {{
		add(new JButton());
		add(new JButton());
		add(new JButton());
		add(new JButton());
	}};
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param owner The owner of the dialog
	 */
	public PromotionView() {
		super(Application.instance(), "Promotion", 200, 200);
		
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
	
	@Override public void initializeComponents() {
		
		// Add the buttons into the specified panel
		for(JButton button : _promotionOptions) {
			_chessPiecesPanel.add(button);
			button.setBorder(new LineBorder(Color.BLACK));
			button.addMouseListener(new MouseAdapter() {
				@Override public void mouseReleased(MouseEvent e) {
					e.getComponent().setBackground(Color.red);
					e.getComponent().setForeground(Color.red);
				}
			});
		}
		
		// Add the panel to the view
		add(_chessPiecesPanel);
		
		// Turn the panel into the specified grid
		SpringUtilities.makeCompactGrid(_chessPiecesPanel, 2, 2, 0, 0, 0, 0);
	}

	@Override public void initializeComponentBindings() {
	}
	
	@Override public void render() {
	
		// Render in the middle of the screen
		Dimension screenSize = getToolkit().getScreenSize();
		setLocation(
			(int)screenSize.getWidth() - getWidth(), 
			(int)screenSize.getHeight() - getHeight()
		);
		
		// Update the list of pieces w.r.t the current player
		getViewProperties().getEntity(PromotionController.class).updatePromotedPieces();
		
		// Render the pieces by updating the icons within our buttons
		List<AbstractChessEntity> entities = getViewProperties().getEntity(PromotionController.class).getPromotionList();
		if(entities.size() == _promotionOptions.size()) {
			for(int i = 0; i < _promotionOptions.size(); ++i) {
				_promotionOptions.get(i).setIcon(new ImageIcon(entities.get(i).getRenderableContent()));
			}
		}
		else {
			Tracelog.log(Level.SEVERE, true, "Cannot render list, there is a mismatch in size!");
		}
		
		// Make the UI fit its contents
		pack();
		
		// Show the content on screen
		super.render();
	}
}