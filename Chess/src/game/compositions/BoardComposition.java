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

package game.compositions;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import game.compositions.MovementComposition.EntityMovements;
import game.entities.interfaces.IChessEntity;
import models.TileModel;

/**
 * This class represents the instructions for how the board game should operate
 *
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class BoardComposition {
 
    /**
     * The dimensions of the board game
     */
    private final Dimension _dimensions;
    
    /**
     * The list of neighbors logically associated to a specified controller
     * 
     * Key1: The tile model center
     * Key2: The TOP, BOTTOM, LEFT, RIGHT neighboring tiles of the specified key1
     */
    private final Map<TileModel, Map<EntityMovements, TileModel>> _neighbors = new LinkedHashMap();
    
    /**
     * Constructs a new instance of this class type
     * 
     * @param dimensions The board game dimensions
     */
    public BoardComposition(Dimension dimensions) {
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
    
    
    public List<TileModel> getBoardPositions(TileModel tileModel) {
        
        List<TileModel> tiles = new ArrayList();

        IChessEntity entity = tileModel.getEntity();
        if(entity == null) {
            return tiles;
        }
        
            
            Set<TileModel> positions = new HashSet();
            
            for(EntityMovements[] position : entity.getMovements()) {
            
            
                for(Map.Entry<EntityMovements, TileModel> entry : _neighbors.get(tileModel).entrySet()) {
                    
                }
            
            }
//                Map<EntityMovements, Map<EntityMovements, TileModel>> tileModelPositions = _neighbors.get(tileModel);
//                for(EntityMovements movement : position) {
//                    tileModelPositions.get(movement)
//
//                    
//                    
//                    
//                    
//                    
//
//
//                }
//                for(Map.Entry<EntityMovements, Map<EntityMovements, TileModel>> entry : tileModelPositions.entrySet()) {
//                    entry.get
//                }
//            }

        
        return tiles;
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
        Map<EntityMovements, TileModel> tileModelNeighbors = _neighbors.get(tileModel);
        
        // This collection holds the list of all the neighbors
        List<TileModel> allNeighbors = new ArrayList();
        
        // Go through every entry set in our structure
        for(Map.Entry<EntityMovements, TileModel> entry : tileModelNeighbors.entrySet()) {
            
            // Get the tile model
            TileModel tile = entry.getValue();
            if(tile == null) {
                continue;
            }
            
            // Add our tile to the list
            allNeighbors.add(tile);
            
            // To get the diagonals, make sure that if we are on the top or bottom tile
            // that we fetch their respective tiles, and provided that they are valid
            // add them to our list.
            switch(entry.getKey()) {
            case TOP:
            case BOTTOM:
                TileModel left = _neighbors.get(tile).get(EntityMovements.LEFT);
                if(left != null) {
                    allNeighbors.add(left);
                }
                
                TileModel right = _neighbors.get(tile).get(EntityMovements.RIGHT);
                if(right != null) {
                    allNeighbors.add(right);
                }
                break;
            }
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
            Map<EntityMovements, TileModel> neighbors = new HashMap<EntityMovements, TileModel>();
                    
            // Populate the neighbors structure with the movement elements
            // Note: Diagonals can be fetched using these primites
            neighbors.put(EntityMovements.TOP, topRow == null ? null : topRow[i]);
            neighbors.put(EntityMovements.LEFT, i - 1 < 0 ? null : neutralRow[i - 1]);
            neighbors.put(EntityMovements.RIGHT, i + 1 >= columns ? null : neutralRow[i + 1]);
            neighbors.put(EntityMovements.BOTTOM, bottomRow == null ? null : bottomRow[i]);
                       
            // Assign the mappings where we reference the neutral-neutral tile as the key
            _neighbors.put(neutralRow[i], neighbors);
        }
    }
}