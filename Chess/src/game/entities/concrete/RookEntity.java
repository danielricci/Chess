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

import java.util.ArrayList;
import java.util.List;

import game.compositions.MovementComposition.EntityMovements;
import generated.DataLookup.DataLayerName;

/**
 * This class represents a rook in the chess game
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
class RookEntity extends AbstractChessEntity {
    
    /**
     * Constructs a new instance of this class type
     */
    public RookEntity() {
        super(DataLayerName.ROOK);
    }

    @Override public List<EntityMovements[]> getMovements() {
        return new ArrayList<EntityMovements[]>() {{
            add(new EntityMovements[] { EntityMovements.TOP});
            add(new EntityMovements[] { EntityMovements.BOTTOM});
            add(new EntityMovements[] { EntityMovements.LEFT});
            add(new EntityMovements[] { EntityMovements.RIGHT});
        }};
    }

    @Override public boolean isMovementContinuous() {
        return true;
    }
}