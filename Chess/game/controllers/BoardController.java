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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import controllers.TileController.NeighborXPosition;
import controllers.TileController.NeighborYPosition;
import models.TileModel;
import views.BoardView;

public class BoardController extends BaseController {
	
	private static final int _dimension = 8;

	private Vector<Vector<TileModel>> _tiles = new Vector<>(Arrays.asList(new Vector<TileModel>()));		
	
	/**
	 * The list of neighbors logically associated to a specified controller
	 * Key1: A Controller that maps to its association of neighbors
	 * Key2: The Y-Axis of the neighbor
	 * Key3: The X-Axis of the neighbor
	 */
	private final Map<TileController, Map<NeighborYPosition, Map<NeighborXPosition, TileController>>> _neighbors = new HashMap<>();
	
	public BoardController(BoardView view) {
		super(view);
	}	
	
	public void createTileRow(ArrayList<TileController> controllers) {
		// Populate the tile model and observer it with our view
		for(TileController controller : controllers) {
			controller.populateTileModel(getView(BoardView.class));
		}
		
		link();
	}

	private void link() {
		
		Vector<TileModel> tilesRow = _tiles.lastElement();
		for(int i = 0; i < tilesRow.size(); ++i) {
			TileModel tile = tilesRow.get(i);
			tile.setNeighbors(
				NeighborYPosition.NEUTRAL,
				new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.LEFT, i - 1 < 0 ? null : tilesRow.get(i - 1)),
				new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.NEUTRAL, tile),
				new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.RIGHT, i + 1 == tilesRow.size() ? null : tilesRow.get(i + 1))
			);
		}
		
		if(_tiles.size() > 1) {

			// Grab the most recently populated two elements to populate their neighbors
			Vector<TileModel> firstRow = _tiles.get(_tiles.size() - 2);
			Vector<TileModel> secondRow = _tiles.lastElement();
			
			// Populate both rows neighbors
			for(int i = 0; i < firstRow.size(); ++i) {
				TileModel firstRowElement = firstRow.get(i);
				TileModel secondRowElement = secondRow.get(i);
			
				// Set the neighbors of the first rows bottom neighbors
				firstRowElement.setNeighbors(NeighborYPosition.BOTTOM, 
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.LEFT, i > 0 ? secondRow.get(i - 1) : null),
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.NEUTRAL, secondRowElement),
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.RIGHT, i + 1 < firstRow.size() ? secondRow.get(i + 1) : null)
				);
				
				// Set the neighbors of the second rows top neighbors
				secondRowElement.setNeighbors(NeighborYPosition.TOP, 
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.LEFT, i > 0 ? firstRow.get(i - 1) : null),
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.NEUTRAL, firstRowElement),
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.RIGHT, i + 1 < firstRow.size() ? firstRow.get(i + 1) : null)
				);					
			}
		}
	}
	
	public static int getDimensions() {		
		return _dimension;
	}
	
	@Override public void dispose() {
		_tiles.clear();
	}
}