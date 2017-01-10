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

package models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import communication.internal.dispatcher.Operation;
import models.PlayerModel.Team.Orientation;

/** 
 * A tile model represents a tile that knows about what it currently holds on itself
 * and its current state; it also is aware of its immediate neighbors
 */
public class TileModel extends GameModel implements IPlayableTile, Comparable<TileModel> {
    
	private PlayerModel _player;	
	private Selection _selection;
	
	private static int IDENTIFIER;
	private final int _identifier = ++IDENTIFIER;
    
	private final Map<NeighborYPosition, Map<NeighborXPosition, TileModel>> _neighbors = new HashMap<>();
			
	public enum Selection {
		GuideSelected,
		MoveSelected,
		CaptureSelected,
		None;
	}
	
	public enum NeighborXPosition {
		/**
		 * Note: Make sure that agnostic values follow non-agnostic ones
		 */
		LEFT	(1 << 0, false),
		LEFT_AGNOSTIC(1 << 1, true),
		
		RIGHT (1 << 2, false),
		RIGHT_AGNOSTIC	(1 << 3, true),
		
		NEUTRAL(1 << 4, false),
		NEUTRAL_AGNOSTIC(1 << 5, true);
	
		private final int _value;
		private final boolean _agnostic;
		
		private NeighborXPosition(int value, boolean agnostic) {
			_value = value;
			_agnostic = agnostic;
		}
	}

	public enum NeighborYPosition {
		
		/**
		 * Note: Make sure that agnostic values follow non-agnostic ones
		 */
		TOP	(1 << 0, false),
		TOP_AGNOSTIC(1 << 1, true),
		
		BOTTOM (1 << 2, false),
		BOTTOM_AGNOSTIC	(1 << 3, true),
		
		NEUTRAL(1 << 4, false),
		NEUTRAL_AGNOSTIC(1 << 5, true);
	
		private final int _value;
		private final boolean _agnostic;
		
		private NeighborYPosition(int value, boolean agnostic) {
			_value = value;
			_agnostic = agnostic;
		}
		
		protected static NeighborYPosition flip(NeighborYPosition pos) {
			switch(pos) {
			case BOTTOM:
				return TOP;
			case TOP:
				return BOTTOM;
			default:
				return pos;
			}
		}
		
		/**
		 * Normalizes a position by removing its agnostic value
		 */
		protected static NeighborYPosition fromAgnostic(NeighborYPosition position) {
			switch(position) {
				case BOTTOM_AGNOSTIC:
				case TOP_AGNOSTIC:
				case NEUTRAL_AGNOSTIC:
				{
					int val = position._value >> 1;
					for(NeighborYPosition pos : NeighborYPosition.values()) {
						if(pos._value == val) {
							return pos;
						}
					}			
					break;
				}
			}
			System.out.println("Error with fromAgnostic");
			System.out.println(java.util.Arrays.toString((new Throwable()).getStackTrace()));
			return position;
		}
		
		protected boolean isAgnostic() {
			return _agnostic;
		}
		
		protected static NeighborYPosition convertOrientation(Orientation orientation) {
			return orientation == Orientation.UP 
				? NeighborYPosition.TOP 
				: NeighborYPosition.BOTTOM;
		}
		
		protected static NeighborYPosition toAgnostic(NeighborYPosition position) {
			switch(position) {
			case BOTTOM:
			case TOP:
				int val = position._value << 1;
				for(NeighborYPosition pos : NeighborYPosition.values()) {
					if(pos._value == val) {
						return pos;
					}
				}
				break;
			case BOTTOM_AGNOSTIC:
				break;
			case TOP_AGNOSTIC:
				break;
			default:
				break;
			}
			
			System.out.println("Error with toAgnostic");
			System.out.println(java.util.Arrays.toString((new Throwable()).getStackTrace()));
			return position;
		}
	};
	
    public TileModel(Observer... observers) {
		super(observers);
		
		_neighbors.put(NeighborYPosition.BOTTOM, new HashMap<NeighborXPosition, TileModel>());
		_neighbors.put(NeighborYPosition.NEUTRAL, new HashMap<NeighborXPosition, TileModel>());
		_neighbors.put(NeighborYPosition.TOP, new HashMap<NeighborXPosition, TileModel>());
		
		//_player = player;

		//if(player != null) {
		//	player.addTilePiece(this);
		//}
		doneUpdating();
	}
    
	private SortedSet<TileModel> getNeighbors(NeighborYPosition position) {
		
		if(position.isAgnostic()) {
			position = NeighborYPosition.fromAgnostic(position);
		}
		else if(_player != null && _player.getPlayerOrientation() == Orientation.DOWN) {
			// This is done to normalize the neighbor concept
			position = NeighborYPosition.flip(position);
		}
		
		SortedSet<TileModel> tiles = new TreeSet<>();
		if(_neighbors.containsKey(position)) {
			for(TileModel tileModel : _neighbors.get(position).values()) {
				if(tileModel != null) {
					tiles.add(tileModel);
				}
			}
		}
		return tiles;
	}
	
	public SortedSet<TileModel> getBackwardNeighbors() {
		System.out.println("IMPLEMENT ME - getBackwardNeighbors()");
		return null;
		
		/*SortedSet<TileModel> neighbors = new TreeSet<>();
		NeighborYPosition neighborPosition = NeighborYPosition.BOTTOM;
		
		if(_player.getPlayerOrientation() == Orientation.DOWN)
		{
			neighborPosition = NeighborYPosition.flip(neighborPosition);
		}
		
		if(_neighbors.containsKey(neighborPosition)) {
			neighbors.addAll(_neighbors.get(neighborPosition));
		}
		
		return neighbors;
		*/				
	}
	
	public SortedSet<TileModel> getForwardNeighbors() {
		System.out.println("IMPLEMENT ME - getForwardNeighbors()");
		return null;
		
		/*
		SortedSet<TileModel> neighbors = new TreeSet<>();
		
		NeighborYPosition neighborPosition = NeighborYPosition.TOP;
		if(_player != null && _player.getPlayerOrientation() == Orientation.DOWN)
		{
			neighborPosition = NeighborYPosition.flip(neighborPosition);
		}
		
		if(_neighbors.containsKey(neighborPosition)) {
			neighbors.addAll(_neighbors.get(neighborPosition));
		}
		
		return neighbors;
		*/		
	}
	 
	public void setSelected(Operation operation, Selection selection, boolean flushBuffer) {
		if(flushBuffer) {
			clearOperations();
		}
		addOperation(operation);
		_selection = selection;
		doneUpdating();
	}
	
	public void setSelected(Operation operation, Selection selection) { 
		setSelected(operation, selection, false); 
	}
		

	/**
	 * Sets the neighbors specified to this model
	 * 
	 * @param neighborYPosition The Y-Axis positioning of the neighbor, refer to documentation for this if need be
	 * @param neighborTiles The neighboring tiles to add to this model
	 * 
	 */
	@SafeVarargs
	public final void setNeighbors(NeighborYPosition neighborYPosition, Entry<NeighborXPosition, TileModel>... neighborTiles) {	

		// Set the reference to the mappings for the neighbors to this model
		if(_neighbors.containsKey(neighborYPosition)) {
			_neighbors.get(neighborYPosition).clear();

			// Go through the passed in contents and add use it to populate the neighbors of this model
			for (Entry<NeighborXPosition, TileModel> neighborTile : neighborTiles)
			{
				_neighbors.get(neighborYPosition).put(neighborTile.getKey(), neighborTile.getValue());
			}
		}
	}
	
	public void removeTile() {
		_player.updatePlayerPiece(this,  null);
		_player = null;

		clearOperations();
		doneUpdating();
	}
	
	/**
	 * Updates the player of this 
	 * @param player
	 */
	public void updateOwner(PlayerModel player) {
		
		// Update the piece of the tile so that it is now
		// associated to this tile
		PlayerPiece piece = _player.getPlayerPiece(this);
		
		// Update the piece to indicate the new player that owns it
		piece.updatePlayerPiece(this, player);
		
		// Update the tile to indicate the player is currently playing on it
		_player = player;
				
		// Update the players list of pieces that it owns
		player.addTilePiece(this, piece);
	}
	
	public void swapWith(TileModel tileModel) {
		
		// Update the player
		PlayerModel player = _player;
		_player = tileModel._player;
		tileModel._player = player;
		
		// Update the players entry
		tileModel._player.updatePlayerPiece(this, tileModel);
		
		// Send done signals
		doneUpdating();
		tileModel.doneUpdating();
	}
	
	public boolean isPlayerHuman() { return _player != null; }
	public boolean isSelected() { return _selection == Selection.MoveSelected; }
	public boolean isGuideSelected() { return _selection == Selection.GuideSelected; }

	public int getIdentifier() { return _identifier; }
	
	public Selection getSelectionType() { return _selection; }
	public PlayerModel getPlayer() { return _player; }
	
	public SortedSet<TileModel> getAllNeighbors() {
		SortedSet<TileModel> allNeighbours = new TreeSet<>(
			getNeighbors(NeighborYPosition.TOP)
		);
		allNeighbours.addAll(getNeighbors(NeighborYPosition.NEUTRAL));
		allNeighbours.addAll(getNeighbors(NeighborYPosition.BOTTOM));

		return allNeighbours;
	}
	
	@Override public boolean isMovableTo() {
    	return _player == null && _selection != Selection.MoveSelected;
    }
    
	@Override public boolean isPlayable() {
		return _player != null && _selection != Selection.MoveSelected;
	}
	
	/**
	 * Gets all of the tiles surrounding this tile that are considered "capturable" w.r.t the passed in capturer
	 * 
	 * @param capturer The tile performing the capture on this tile
	 * 
	 * @return List of tiles that are capturable by the capturer
	 */
	public Vector<TileModel> getCapturableNeighbors(TileModel capturer) {
		
		Vector<TileModel> capturablePositions = new Vector<>();
		if(_player == null || capturer.getPlayer() == _player) {
			return capturablePositions;
		}
		
		NeighborYPosition position = NeighborYPosition.convertOrientation(_player.getPlayerOrientation());
		populateCapturableTiles(capturer, capturablePositions, capturer.getForwardNeighbors(), position);
		if(capturer.getPlayer().getPlayerPiece(capturer).getIsKinged()) {
			populateCapturableTiles(capturer, capturablePositions, capturer.getBackwardNeighbors(), NeighborYPosition.flip(position));
		}
		
		return capturablePositions;
	}
	
	private void populateCapturableTiles(TileModel capturer, Vector<TileModel> outCapturablePositions, SortedSet<TileModel> capturerNeighbors, NeighborYPosition position) {
		int index = -1;
		if(capturerNeighbors.contains(this)) {
			index = capturerNeighbors.headSet(this).size();
		} 
		
		if(index == -1) {
			return;
		}
			
		position = NeighborYPosition.flip(position);
		position = NeighborYPosition.toAgnostic(position);
		
		Object[] neighborObjects = getNeighbors(position).toArray();
		if(neighborObjects.length > 1) {
			TileModel diagonalTile = (TileModel) neighborObjects[index];
			if(capturer.getForwardNeighbors().size() == 1 && diagonalTile.getForwardNeighbors().size() == 1) {
				diagonalTile = (TileModel)neighborObjects[++index];
			}
			
			if(diagonalTile.isMovableTo()) {
				outCapturablePositions.add(diagonalTile);
			}	
		}
	}
	
	@Override public String toString() {
		return Integer.toString(_identifier);
	}

	@Override public int compareTo(TileModel tileModel) {
		if(tileModel == this || _identifier == tileModel._identifier) {
			return 0;
		}
		
		return _identifier < tileModel._identifier ? -1 : 1;
	}
}