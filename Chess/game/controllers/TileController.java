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

package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import api.IReceiver;
import communication.internal.dispatcher.DispatcherOperation;
import models.PlayerModel.Team.Orientation;
import models.TileModel;
import views.BaseView;
import views.TileView;

/**
 * TileController is the controller in charge of managing events fired from TileView actions 
 */
public class TileController extends BaseController {
	
	/**
	 * The Model associated to this controller
	 */
	private TileModel _tile;
	
	/**
	 * The list of neighbors logically associated to this controller
	 */
	private final Map<NeighborYPosition, Map<NeighborXPosition, TileController>> _neighbors = new HashMap<>();
	
	/**
	 * Specifies the interactions on the x-axis of neighbors 
	 */
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
	
	/**
	 * Specifies the interactions on the x-axis of neighbors
	 */
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
	
	/**
	 * Constructs a new instance of this class
	 * 
	 * @param viewClass The view to instantiate this controller with
	 */
	public <T extends BaseView> TileController(Class<T> viewClass) {
		super(viewClass, true);
		
		// Create the placeholder for the neighboring system
		_neighbors.put(NeighborYPosition.BOTTOM, new HashMap<NeighborXPosition, TileController>());
		_neighbors.put(NeighborYPosition.NEUTRAL, new HashMap<NeighborXPosition, TileController>());
		_neighbors.put(NeighborYPosition.TOP, new HashMap<NeighborXPosition, TileController>());
	}
	
	/**
	 * Populates this controller with a receivable entity model
	 * 
	 * @param receiver The receiver to hook onto the model
	 * 
	 */
	public void populateTileModel(IReceiver receiver) { // TODO - can this be done in a different way
		_tile = new TileModel(receiver, getView(TileView.class));
	}
	
	
	/**
	 * Gets the list of neighbors
	 * 
	 * @param position The forward position orientation
	 * 
	 * @return The list of neighbors 
	 */
	private SortedSet<TileController> getNeighbors(NeighborYPosition position) {
		
		if(position.isAgnostic()) {
			position = NeighborYPosition.fromAgnostic(position);
		}
		
		SortedSet<TileController> neighbors = new TreeSet<>();
		if(_neighbors.containsKey(position)) {
			for(TileController controller : _neighbors.get(position).values()) {
				if(controller != null) {
					neighbors.add(controller);
				}
			}
		}
		return neighbors;
	}
	
	/**
	 * Gets all the neighbors associated to this controller
	 */
	public SortedSet<TileController> getAllNeighbors() {
		SortedSet<TileController> allNeighbours = new TreeSet<>(
			getNeighbors(NeighborYPosition.TOP)
		);
		allNeighbours.addAll(getNeighbors(NeighborYPosition.NEUTRAL));
		allNeighbours.addAll(getNeighbors(NeighborYPosition.BOTTOM));

		return allNeighbours;
	}
	
	/**
	 * Sets the neighbors specified to this model
	 * 
	 * @param neighborYPosition The Y-Axis positioning of the neighbor, refer to documentation for this if need be
	 * @param neighborTiles The neighboring tiles to add to this model
	 * 
	 */
	//@SafeVarargs
	/*public final void setNeighbors(NeighborYPosition neighborYPosition, Entry<NeighborXPosition, TileModel>... neighborTiles) {	

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
	*/
	

	
	private class ToggleNeighborTiles implements ActionListener { // TODO - can we get rid of classes and just write methods
		@Override public void actionPerformed(ActionEvent actionEvent) {
			System.out.println("private class ToggleNeighborTiles implements ActionListener");
			//_tile.setSelected(DispatcherOperation.ToggleNeighborTiles);
		}		
	}
	
	@Override public Map<DispatcherOperation, ActionListener> getRegisteredOperations() {
		return new HashMap<DispatcherOperation, ActionListener>(){{
			put(DispatcherOperation.ToggleNeighborTiles, new ToggleNeighborTiles());
		}};
	}

	/**
	 * Performs a highlight command on the surrounding cells
	 *  
	 * @param isHighlighted If it should be enabled or not
	 */
	public void highlightNeighbors(boolean enabled) {
		//_tile.getAllNeighbors();
	}
}