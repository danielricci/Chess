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

package views.controls;

import javax.swing.JButton;

import game.entities.concrete.AbstractChessEntity;

/**
 * A button class with the capability of caching data related to the abstract chess entity
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class PromotionButton extends JButton {
	
	/**
	 * The abstract chess entity associated to this button
	 */
	private AbstractChessEntity _entity;

	/**
	 * Sets the chess entity
	 * 
	 * @param entity The chess entity to set
	 */
	public void setChessEntity(AbstractChessEntity entity) {
		_entity = entity;
	}
	
	/**
	 * Gets the chess entity
	 * 
	 * @return The chess entity of this button
	 */
	public AbstractChessEntity getChessEntity() {
		return _entity;
	}
}