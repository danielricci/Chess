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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import engine.core.mvc.controller.BaseController;
import views.BoardView;

public class BoardController extends BaseController {
	
	/**
	 * The dimensions of the board
	 */
	public static final Dimension Dimensions = new Dimension(8, 8);
	
	/**
	 * The list of neighbors logically associated to a specified controller
	 * 
	 * Key1: A Controller that maps to its association of neighbors
	 * Key2: The Y-Axis of the neighbor
	 * Key3: The X-Axis of the neighbor
	 */
	private final Map<TileController, Map<NeighborYPosition, Map<NeighborXPosition, TileController>>> _neighbors = new HashMap<TileController, Map<NeighborYPosition, Map<NeighborXPosition, TileController>>>();
	
	/**
	 * Specifies the interactions on the x-axis of neighbors 
	 */
	private enum NeighborXPosition {
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
	private enum NeighborYPosition {
		
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
	 * @param view The view to link with this controller
	 */
	public BoardController(BoardView view) {
		super(view);
	}	
	
	/**
	 * Populates the list of neighbors, logically attaching them
	 * 
	 * @param tiles The list of tiles 
	 */
	public void populateBoardNeighbors(List<List<TileController>> tiles) {
		for(int i = 0; i < tiles.size(); ++i) {
			link(
				i - 1 >= 0 ? tiles.get(i - 1) : null, 
				tiles.get(i), 
				i + 1 < tiles.size() ? tiles.get(i + 1) : null
			);
		}
	}

	/**
	 * Links together the passed in rows with respect to a flood fill
	 *  
	 * @param top The top row
	 * @param neutral the neutral row
	 * @param bottom The bottom row
	 */
	private void link(List<TileController> topRow, List<TileController> neutralRow, List<TileController> bottomRow) {
		
		for(int i = 0, dim = Dimensions.width ; i < dim; ++i) {
			
			Map<NeighborYPosition, Map<NeighborXPosition, TileController>> neighbors = new HashMap<NeighborYPosition, Map<NeighborXPosition, TileController>>(){{
				put(NeighborYPosition.TOP, new HashMap<NeighborXPosition, TileController>());
				put(NeighborYPosition.NEUTRAL, new HashMap<NeighborXPosition, TileController>());
				put(NeighborYPosition.BOTTOM, new HashMap<NeighborXPosition, TileController>());
			}};
					
			// Top Neighbors
			neighbors.get(NeighborYPosition.TOP).put(NeighborXPosition.LEFT, i - 1 < 0 || topRow == null ? null : topRow.get(i - 1));
			neighbors.get(NeighborYPosition.TOP).put(NeighborXPosition.NEUTRAL, topRow == null ? null : topRow.get(i));
			neighbors.get(NeighborYPosition.TOP).put(NeighborXPosition.RIGHT, i + 1 >= dim || topRow == null ? null : topRow.get(i + 1));
			
			// Neutral Neighbors
			neighbors.get(NeighborYPosition.NEUTRAL).put(NeighborXPosition.LEFT, i - 1 < 0 ? null : neutralRow.get(i - 1));
			neighbors.get(NeighborYPosition.NEUTRAL).put(NeighborXPosition.NEUTRAL, neutralRow.get(i));
			neighbors.get(NeighborYPosition.NEUTRAL).put(NeighborXPosition.RIGHT, i + 1 >= dim ? null : neutralRow.get(i + 1));
				
			// Bottom Neighbors
			neighbors.get(NeighborYPosition.BOTTOM).put(NeighborXPosition.LEFT, i - 1 < 0 || bottomRow == null ? null : bottomRow.get(i - 1));
			neighbors.get(NeighborYPosition.BOTTOM).put(NeighborXPosition.NEUTRAL, bottomRow == null ? null : bottomRow.get(i));
			neighbors.get(NeighborYPosition.BOTTOM).put(NeighborXPosition.RIGHT, i + 1 >= dim || bottomRow == null ? null : bottomRow.get(i + 1));
			
			// Assign the mappings where we reference the neutral-neutral tile as the key
			_neighbors.put(neutralRow.get(i), neighbors);
		}
	}
	
	
	/**
	 * Gets the list of neighbors
	 * 
	 * @return The list of neighbors 
	 */
	public List<TileController> getNeighbors(TileController neutral) {
		
		List<TileController> controllers = new ArrayList<>();
		Map<NeighborYPosition, Map<NeighborXPosition, TileController>> neighbors = _neighbors.get(neutral);
		for(Map.Entry<NeighborYPosition, Map<NeighborXPosition, TileController>> entry : neighbors.entrySet()) {
			controllers.addAll(entry.getValue().entrySet().stream().map(z -> z.getValue()).filter(a-> a != null).collect(Collectors.toList()));
		}
		return controllers;
	}
	
	/**
	 * Gets all the neighbors associated to this controller
	 */
	/*
	public SortedSet<TileController> getAllNeighbors() {
		SortedSet<TileController> allNeighbours = new TreeSet<>(
			getNeighbors(NeighborYPosition.TOP)
		);
		allNeighbours.addAll(getNeighbors(NeighborYPosition.NEUTRAL));
		allNeighbours.addAll(getNeighbors(NeighborYPosition.BOTTOM));

		return allNeighbours;
	}*/
	
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
}