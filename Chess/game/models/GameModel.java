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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import api.IView;
import communication.internal.dispatcher.Operation;

public class GameModel extends Observable
{
	private final Queue<Operation> _operations = new LinkedList<>();
	private final ArrayList<Observer> _observers = new ArrayList<>();
	
	protected GameModel(Observer... observer) {
		for(Observer obs : observer) {
			addObserver(obs);
		}
	}
	
	public final void addCachedData(Operation operation) {
		addOperation(operation);
		doneUpdating();
	}
	
	public final Queue<Operation> getOperations() {
		return _operations;
	}
	
	// TODO - remove observer crap and replace it with our new system
	@Override public synchronized void addObserver(Observer observer) {
		super.addObserver(observer);
		_observers.add(observer);
	}
	
	// TODO - remove observer crap and replace it with our new system
	@Override public synchronized void deleteObserver(Observer observer) {
		super.deleteObserver(observer);
		_observers.remove(observer);
	}
	
	// TODO - remove observer crap and replace it with our new system
	@Override public void notifyObservers(Object arg) {
		if (!hasChanged()) 
		{
			return;
		}
        
		clearChanged();        
        for(Observer obs : _observers) {
        	if(obs instanceof IView)
        	{
        		obs.update(this,  arg);
        		// TODO - remove observer crap and replace it with our new system
        		//IView obsView = (IView) obs;
        		//obsView.update(tho, arg);
        		/*
        		if(obsView.isValidListener(_operations.toArray(new Operation[_operations.size()])))
        		{
            		obs.update(this, arg);        			
        		}
        		*/
        	}
        }
	}
		
	protected final void doneUpdating() {
		setChanged();
		if(_operations.isEmpty()) {
			_operations.add(Operation.Refresh);
		}
		notifyObservers(_operations);
		_operations.clear();
	}	
	
	protected final void addOperation(Operation operation) { 
		_operations.add(operation); 
	}
	
	protected final void clearOperations() { 
		_operations.clear(); 
	}
}