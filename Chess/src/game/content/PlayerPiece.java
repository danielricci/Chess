/**
* Daniel Ricci <2016> <thedanny09@gmail.com>
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

package game.content;

import java.awt.Image;

import javax.swing.ImageIcon;

import game.models.PlayerModel;
import game.models.TileModel;

public class PlayerPiece {
	
	private PlayerModel _playerModel;
	private boolean _isKinged;
		
	public PlayerPiece(PlayerModel playerModel) {
		_playerModel = playerModel;
	}	

	public void updatePlayerPiece(TileModel tileModel, PlayerModel playerModel) {
		_playerModel.updatePlayerPiece(tileModel, null);
		_playerModel = playerModel;
	}
	
	public Image getImage(TileModel tile) {
		return new ImageIcon(getClass().getResource(_playerModel.getTeamPath(tile))).getImage();
	}
	
	public void setAsKinged() { 
		_isKinged = true;
	}
	
	public boolean getIsKinged() {
		return _isKinged;
	}
}