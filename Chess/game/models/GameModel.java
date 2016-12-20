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
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import communication.internal.dispatcher.DispatchOperation;

public class GameModel extends Observable
{
	private final Queue<DispatchOperation> _operations = new LinkedList<>();
	private final Map<DispatchOperation, Object> _debugger = new HashMap<>();
	
	protected GameModel(Observer... observer) {
		for(Observer obs : observer) {
			addObserver(obs);
		}
	}
	
	public final void addCachedData(DispatchOperation operation, Object value) {
		_debugger.put(operation, value);
		addOperation(operation);
		doneUpdating();
	}
	public final <T extends Object> T getCachedData(DispatchOperation operation) { 
		return (T)_debugger.get(operation); 
	}
	public final Queue<DispatchOperation> getOperations() {
		return _operations;
	}
		
	protected final void doneUpdating() {
		setChanged();
		if(_operations.isEmpty()) {
			_operations.add(DispatchOperation.Refresh);
		}
		notifyObservers(_operations);
		_operations.clear();
	}	
	protected final void addOperation(DispatchOperation operation) { _operations.add(operation); }
	protected final void clearOperations() { _operations.clear(); }
}