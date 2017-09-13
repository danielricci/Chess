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

package game.entities.concrete;

import java.util.List;

import game.core.AbstractEntity;
import game.entities.interfaces.IChessEntity;
import generated.DataLookup.DataLayerName;
import models.PlayerModel;
import models.PlayerModel.PlayerTeam;
import models.TileModel;

/**
 * This class represents the abstract functionality of a chess piece in the game
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
public abstract class AbstractChessEntity extends AbstractEntity implements IChessEntity {
    
    /**
     * The checked state of this entity
     */
    private boolean _isChecked;
    
    /**
     * The check mate state of this entity
     */
    private boolean _isCheckMated;
    
	/**
	 * The player that owns this entity
	 */
	private PlayerModel _player;
	
	/**
     * The tile model that the chess entity is associated to
     */
    private TileModel _tileModel;
	
    /**
     * Indicates if the pawn has moved at least once
     */
    private boolean _hasMovedOnce = false;
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param layerName The name of the layer
	 */
	protected AbstractChessEntity(DataLayerName dataLayerName) {
		super(dataLayerName.toString());
	}
	
	/**
     * Creates an instance of a chess entity with respect to the provided data layer name
     * 
     * @param dataLayerName The data layer name lookup
     * 
     * @return A chess entity
     */
    public static AbstractChessEntity createEntity(DataLayerName dataLayerName) {
        switch(dataLayerName) {
        case BISHOP:
            return new BishopEntity();
        case KING:
            return new KingEntity();
        case KNIGHT:
            return new KnightEntity();
        case PAWN:
            return new PawnEntity();
        case QUEEN:
            return new QueenEntity();
        case ROOK:
            return new RookEntity();
        default:
            return null;	    
        }
    }

    /**
     * Sets the specified player to this entity
     * 
     * @param player The player to set to this entity
     */
    public void setPlayer(PlayerModel player) {
    	if(player != _player) {
    		_player = player;
    		
    		List<String> names = getDataNames();
    		for(String name : names) {
    			if(player.getDataValues().stream().anyMatch(z -> z.toString().equalsIgnoreCase(name))) {
    				setActiveData(name);
    				break;
    			}
    		}
    	}
    }

    /**
     * Sets the tile model to associate to this chess entity
     * 
     * @param tileModel The tile model
     */
    public final void setTile(TileModel tileModel) {
        _tileModel = tileModel;
    }
    
    /**
     * Gets the tile model associated to this chess entity
     * 
     * @return The tile model
     */
    public final TileModel getTile() {
        return _tileModel;
    }
    
    /**
     * Gets the data layer name of this entity
     * 
     * @return The data layer name from the data lookup table
     */
    public final DataLayerName getDataLayerName() {
        return DataLayerName.valueOf(getLayerName());
    }

    /**
	 * @return The team of the player associated to this chess entity 
	 */
	public final PlayerTeam getTeam() {
		return _player.getTeam();
	}

	/**
     * Gets if the chess entity has moved at least once
     * 
     * @return If the chess entity has moved at least once
     */
    public final boolean hasMovedOnce() {
        return _hasMovedOnce;
    }

    /**
     * Sets if the chess piece has moved at least once.  Once this is set to true
     * it can no longer be set back to false
     * 
     * @param hasMovedOnce If the chess entity has moved at least once
     * 
     */
    public final void setHasMoved(boolean hasMovedOnce) {
        if(!_hasMovedOnce) {
            _hasMovedOnce = hasMovedOnce;    
        }
    }
    
    /**
     * Sets the checked state of this entity
     * 
     * Note: If this entity is not checkable then it will not matter if you
     *       set the checked state
     * 
     * @param isChecked The checked state
     */
    public final void setChecked(boolean isChecked) {
        _isChecked = isChecked;
    }
    
    /**
     * Sets the check mate state of this entity
     *
     * Note: If this entity is not checkable then it will not matter if you
     *       set the checked state
     * 
     * @param isCheckMate The check mate state
     */
    public final void setCheckMate(boolean isCheckMate) {
    	_isCheckMated = isCheckMate;
    }
    
    /**
     * Gets the check mate state of this entity
     * 
     * @return The check mate state of this entity
     */
    public final boolean getIsCheckMate() {
    	return getIsCheckable() && _isCheckMated;
    }
    
    /**
     * Gets the checked state of this entity
     * 
     * @return The checked state of this entity
     */
    public final boolean getIsChecked() {
        return getIsCheckable() && _isChecked;
    }
    
    /**
     * Gets if the specified chess entity is a checkable entity
     * 
     * @return TRUE if the chess entity can be put into check
     */
    public boolean getIsCheckable() {
        return false;
    }
    
    @Override public void refresh() {
        _tileModel.refresh();
    }
}