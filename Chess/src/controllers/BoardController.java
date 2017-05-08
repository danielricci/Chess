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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import controllers.enums.NeighborXPosition;
import controllers.enums.NeighborYPosition;
import engine.api.IModel;
import engine.communication.internal.signal.ISignalReceiver;
import engine.communication.internal.signal.types.ModelEvent;
import engine.core.mvc.controller.BaseController;
import models.BoardModel;
import models.TileModel;
import views.BoardView;

/**
 * This controller is in charge of the overall board game.  As far as tiles are concerned, they
 * do not know about their neighbors or the rules of the game, they just know that they want to
 * go somewhere.  This is how a tile controller would differ from a board controller.
 * 
 * This controller should receive messages from tile models whenever there is a change in event, and
 * based on that event, the board controller should intervene and do something with respect to the logic
 * of the game rules.
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class BoardController extends BaseController {
	
	private final BoardModel _boardModel;
	
	/**
	 * The list of neighbors logically associated to a specified controller
	 * 
	 * Key1: A Controller that maps to its association of neighbors
	 * Key2: The Y-Axis of the neighbor
	 * Key3: The X-Axis of the neighbor
	 */
	private final Map<TileModel, Map<NeighborYPosition, Map<NeighborXPosition, TileModel>>> _neighbors;
	
	/**
	 * Constructs a new instance of this class
	 * 
	 * @param view The view to link with this controller
	 */
	public BoardController(BoardView view) {
		super(view);
		
		// Set the board model
		_boardModel = IModel.MODEL_FACTORY.get(BoardModel.class, false);
		
		// Initialize the neighbor structure to the initial size of the board
		_neighbors = new LinkedHashMap<>(BoardModel.DIMENSIONS.width * BoardModel.DIMENSIONS.height);
		
		// Register the signal listeners, we don't want to wait until rendering is done for this to occur
		// because this class will miss important events before hand
		registerSignalListeners();
	}	
	
	@Override public void registerSignalListeners() {
		
		// Register to when this controller is added as a listener
		// TODO - it would be nice if this would only be called iff the ISignalReceiver type were the same
		registerSignalListener(IModel.EVENT_LISTENER_ADDED, new ISignalReceiver<ModelEvent<TileModel>>() {
			@Override public void signalReceived(ModelEvent<TileModel> event) {
				
				// Get the tile model from the event object
				TileModel tileModel = event.getSource();
					
				// If what are trying to insert has already been inserted then something went wrong
				if(_neighbors.putIfAbsent(tileModel, null) != null) {
					System.out.println("Error: Tile model already exists in the list... cannot add this one in");
					System.out.println(java.util.Arrays.toString((new Throwable()).getStackTrace()));
				}

				// If we have enough neighboring elements, then its time to link them (logically)
				// together
				if(BoardModel.AREA == _neighbors.size()) {
					generateLogicalTileLinks();
				}
			}
		});
	}
	
	/**
	 * logically attaches the list of tiles together
	 */
	private void generateLogicalTileLinks() {
		
		TileModel[] tiles = _neighbors.keySet().toArray(new TileModel[0]);
		
		/*
		for(int i = 0, rows = BoardModel.DIMENSIONS.height; i < rows; ++i) {
			linkTileRow(
					
				i - 1 >= 0 ? tiles.get(i - 1) : null,
				tiles.get(i),
			)
		}
		*/
		/*
		for(int i = 0; i < tiles.size(); ++i) {
			linkTileRow(
				i - 1 >= 0 ? tiles.get(i - 1) : null, 
				tiles.get(i), 
				i + 1 < tiles.size() ? tiles.get(i + 1) : null
			);
		}*/
	}
	
	/**
	 * Links together the passed in rows with respect to a flood fill
	 *  
	 * @param top The top row
	 * @param neutral the neutral row
	 * @param bottom The bottom row
	 */
	private void link(List<TileController> topRow, List<TileController> neutralRow, List<TileController> bottomRow) {
		/*
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
		*/
	}
	
	
	/**
	 * Gets the list of neighbors
	 * 
	 * @return The list of neighbors 
	 */
	/*
	public List<TileController> getNeighbors(TileController neutral) {
		
		List<TileController> controllers = new ArrayList<>();
		Map<NeighborYPosition, Map<NeighborXPosition, TileController>> neighbors = _neighbors.get(neutral);
		for(Map.Entry<NeighborYPosition, Map<NeighborXPosition, TileController>> entry : neighbors.entrySet()) {
			controllers.addAll(entry.getValue().entrySet().stream().map(z -> z.getValue()).filter(a-> a != null).collect(Collectors.toList()));
		}
		return controllers;
	}
	*/
	
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