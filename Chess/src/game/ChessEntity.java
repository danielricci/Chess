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

package game;

import game.core.AbstractEntity;
import game.player.Player;

/**
 * Abstract class for all chess pieces in the game
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
public abstract class ChessEntity extends AbstractEntity implements IEntityLink<Player> {
	
	/**
	 * The player that owns this entity
	 */
	private Player _player;
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param layerName The name of the layer
	 */
	protected ChessEntity(String layerName) {
		super(layerName);
	}
	
	@Override public void LinkData(Player entity) {
		if(entity != _player) {
			_player = entity;
		}
	}
}