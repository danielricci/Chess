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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.TileController.NeighborXPosition;
import controllers.TileController.NeighborYPosition;
import views.BoardView;

public class BoardController extends BaseController {
	
	private static final Dimension _dimensions = new Dimension(8, 8);
	
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
	
	public void populateBoardNeighbors(List<List<TileController>> tiles) {
				
		for(int i = 0; i < tiles.size(); ++i) {
			link(tiles.get(0), tiles.get(1), tiles.get(2));
		}
	}

	/**
	 * Links together the passed in rows with respect to a flood fill 
	 * @param top
	 * @param neutral
	 * @param bottom
	 */
	private void link(List<TileController> topRow, List<TileController> neutralRow, List<TileController> bottomRow) {
		
		Map<NeighborYPosition, Map<NeighborXPosition, TileController>> neighbors = new HashMap<NeighborYPosition, Map<NeighborXPosition, TileController>>(){{
			put(NeighborYPosition.TOP, new HashMap<NeighborXPosition, TileController>());
			put(NeighborYPosition.NEUTRAL, new HashMap<NeighborXPosition, TileController>());
			put(NeighborYPosition.BOTTOM, new HashMap<NeighborXPosition, TileController>());
		}};
		
		for(int i = 0, dim = getDimensionX(); i < dim; ++i) {
					
			// Top Neighbors
			neighbors.get(NeighborYPosition.TOP).put(NeighborXPosition.LEFT, i - 1 < 0 ? null : topRow.get(i - 1));
			neighbors.get(NeighborYPosition.TOP).put(NeighborXPosition.NEUTRAL, i - 1 < 0 ? null : topRow.get(i - 1));
			neighbors.get(NeighborYPosition.TOP).put(NeighborXPosition.RIGHT, i - 1 < 0 ? null : topRow.get(i - 1));
			
			// Neutral Neighbors
			neighbors.get(NeighborYPosition.NEUTRAL).put(NeighborXPosition.LEFT, i - 1 < 0 ? null : neutralRow.get(i - 1));
			neighbors.get(NeighborYPosition.NEUTRAL).put(NeighborXPosition.NEUTRAL, i - 1 < 0 ? null : neutralRow.get(i - 1));
			neighbors.get(NeighborYPosition.NEUTRAL).put(NeighborXPosition.RIGHT, i - 1 < 0 ? null : neutralRow.get(i - 1));
				
			// Bottom Neighbors
			neighbors.get(NeighborYPosition.BOTTOM).put(NeighborXPosition.LEFT, i - 1 < 0 ? null : bottomRow.get(i - 1));
			neighbors.get(NeighborYPosition.BOTTOM).put(NeighborXPosition.NEUTRAL, i - 1 < 0 ? null : bottomRow.get(i - 1));
			neighbors.get(NeighborYPosition.BOTTOM).put(NeighborXPosition.RIGHT, i - 1 < 0 ? null : bottomRow.get(i - 1));
		}
	}
	
	public static int getDimensionX() {		
		return _dimensions.width;
	}
	
	public static int getDimensionY() {		
		return _dimensions.height;
	}
}