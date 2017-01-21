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

import api.IReceivable;
import communication.internal.dispatcher.DispatcherOperation;
import models.TileModel;
import views.BaseView;
import views.TileView;

public class TileController extends BaseController {
	
	private TileModel _tile;
	
	public <T extends BaseView> TileController(Class<T> viewClass) {
		super(viewClass, true);
	}
	
	// TODO - can this be done from the constructor
	public TileModel populateTileModel(IReceivable receiver) {		
		_tile = new TileModel(receiver, getView(TileView.class));
		return _tile;
	}
	
	private class ToggleNeighborTiles implements ActionListener {
		@Override public void actionPerformed(ActionEvent actionEvent) {
			_tile.setSelected(DispatcherOperation.ToggleNeighborTiles);
		}		
	}
	
	@Override public Map<DispatcherOperation, ActionListener> getRegisteredOperations() {
		return new HashMap<DispatcherOperation, ActionListener>(){{
			put(DispatcherOperation.ToggleNeighborTiles, new ToggleNeighborTiles());
		}};
	}

	public void highlightNeighbors(boolean isHighlighted) {
		_tile.getAllNeighbors();
	}
}