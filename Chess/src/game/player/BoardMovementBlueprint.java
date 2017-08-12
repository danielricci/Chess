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

package game.player;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import models.TileModel;

/**
 * This class represents the instructions for how a particular chess piece should move
 *
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class BoardMovementBlueprint {
 
    /**
     * The dimensions of the board game
     */
    private final Dimension _dimensions;
    
    /**
     * The list of neighbors logically associated to a specified controller
     * 
     * Key1: A Controller that maps to its association of neighbors
     * Key2: The Y-Axis of the neighbor
     * Key3: The X-Axis of the neighbor
     */
    private final Map<TileModel, Map<EntityMovements, Map<EntityMovements, TileModel>>> _neighbors = new LinkedHashMap();
    
    /**
     * Represents the available movement positions
     * 
     * @author Daniel Ricci <thedanny09@gmail.com>
     *
     */
    public enum EntityMovements {
        /**
         * Movement is done towards the left
         */
        LEFT,
        /**
         * Movement is done towards the right
         */
        RIGHT,
        /**
         * Movement is done towards the top
         */
        TOP,
        /**
         * Movement is done towards the bottom
         */
        BOTTOM
    }
    
    /**
     * Constructs a new instance of this class type
     * 
     * @param dimensions The board game dimensions
     */
    public BoardMovementBlueprint(Dimension dimensions) {
        _dimensions = dimensions;
    }
    
    /**
     * Adds the specified tile model into the neighbors list
     * 
     * @param tileModel The tile model
     */
    public void addTileEntity(TileModel tileModel) {
        // If what are trying to insert has already been inserted then something went wrong
        if(_neighbors.putIfAbsent(tileModel, null) != null) {
            System.out.println("Error: Tile model already exists in the list... cannot add this one in");
            System.out.println(java.util.Arrays.toString((new Throwable()).getStackTrace()));
        }
    
        // If we have enough neighboring elements, then its time to link them together
        if(_dimensions.width * _dimensions.height == _neighbors.size()) {
            generateLogicalTileLinks();
        }
    }
    
    /**
     * Gets all the neighbors associated to the particular model
     * 
     * @param tileModel The tile model to use as a search for neighbors around it
     * 
     * @return The list of tile models that neighbor the passed in tile model
     */
    public List<TileModel> getAllNeighbors(TileModel tileModel) {
        
        // Get the list of neighbors associated to our tile model
        Map<EntityMovements, Map<EntityMovements, TileModel>> tileModelNeighbors = _neighbors.get(tileModel);
        
        // This collection holds the list of all the neighbors
        List<TileModel> allNeighbors = new ArrayList();
        
        // Go through every entry set in our structure
        for(Map.Entry<EntityMovements, Map<EntityMovements, TileModel>> entry : tileModelNeighbors.entrySet()) {
            
            // Add all the neighbors in our entry set that does not have a null tile model
            allNeighbors.addAll(entry.getValue().values().stream().filter(a -> a != null && a != tileModel).collect(Collectors.toList()));
        }
        
        // return the list of neighbors
        return allNeighbors;
    }
    
    /**
     * Logically attaches the list of tiles together by sub-dividing the list of tiles.
     * Note: Order matters in cases such as this, which is why insertion order was important
     *       when I chose the data structure for the neighbors map 
     */
    private void generateLogicalTileLinks() {
                
        // Get the array representation of our tile models.
        // Note: This is done because it is easier to get a subset of an array
        //       and because the neighbor data structure tracks the insertion 
        //       order at runtime which is what is important here.
        TileModel[] tiles = _neighbors.keySet().toArray(new TileModel[0]);
                
        // For every row that exists within our setup model
        for(int i = 0, rows = _dimensions.height, columns = _dimensions.width; i < rows; ++i) {
            
            // Link the tile rows together
            linkTiles(
                // Previous row
                i - 1 >= 0 ? Arrays.copyOfRange(tiles, (i - 1) * columns, ((i - 1) * columns) + columns) : null,
                // Current Row
                Arrays.copyOfRange(tiles, i * columns, (i * columns) + columns),
                // Next Row
                i + 1 >= 0 ? Arrays.copyOfRange(tiles, (i + 1) * columns, ((i + 1) * columns) + columns) : null
            );
        }
    }
    
    /**
     * Links together the passed in rows
     *  
     * @param top The top row
     * @param neutral the neutral row
     * @param bottom The bottom row
     */
    private void linkTiles(TileModel[] topRow, TileModel[] neutralRow, TileModel[] bottomRow) {
        
        for(int i = 0, columns = _dimensions.width; i < columns; ++i) {
            
            // Represents the structural view of a particular tile
            Map<EntityMovements, Map<EntityMovements, TileModel>> neighbors = new HashMap<EntityMovements, Map<EntityMovements, TileModel>>(){{
                put(EntityMovements.TOP, new HashMap<EntityMovements, TileModel>());
                put(null, new HashMap<EntityMovements, TileModel>());
                put(EntityMovements.BOTTOM, new HashMap<EntityMovements, TileModel>());
            }};
                    
            // Top Neighbors
            neighbors.get(EntityMovements.TOP).put(EntityMovements.LEFT, i - 1 < 0 || topRow == null ? null : topRow[i - 1]);
            neighbors.get(EntityMovements.TOP).put(null, topRow == null ? null : topRow[i]);
            neighbors.get(EntityMovements.TOP).put(EntityMovements.RIGHT, i + 1 >= columns || topRow == null ? null : topRow[i + 1]);
            
            // Neutral Neighbors
            neighbors.get(null).put(EntityMovements.LEFT, i - 1 < 0 ? null : neutralRow[i - 1]);
            neighbors.get(null).put(null, neutralRow[i]);
            neighbors.get(null).put(EntityMovements.RIGHT, i + 1 >= columns ? null : neutralRow[i + 1]);
                
            // Bottom Neighbors
            neighbors.get(EntityMovements.BOTTOM).put(EntityMovements.LEFT, i - 1 < 0 || bottomRow == null ? null : bottomRow[i - 1]);
            neighbors.get(EntityMovements.BOTTOM).put(null, bottomRow == null ? null : bottomRow[i]);
            neighbors.get(EntityMovements.BOTTOM).put(EntityMovements.RIGHT, i + 1 >= columns || bottomRow == null ? null : bottomRow[i + 1]);
            
            // Assign the mappings where we reference the neutral-neutral tile as the key
            _neighbors.put(neutralRow[i], neighbors);
        }
    }
}