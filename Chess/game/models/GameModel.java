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

package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import api.IReceivable;
import communication.internal.dispatcher.DispatcherOperation;

/**
 * A Game Model represents the base class of all model type objects
 */
public class GameModel
{
	/**
	 * The list of receivers that can receive a message from the GameModel
	 */
	private final ArrayList<IReceivable> _receivers = new ArrayList<>();

	/**
	 * The list of operations that can be used on the model
	 */
	private final Queue<DispatcherOperation> _operations = new LinkedList<>();
	
	/**
	 * The list of receivable objects that can receive messages 
	 * 
	 * @param receivers The list of receivers
	 */
	protected GameModel(IReceivable... receivers) {
		_receivers.addAll(Arrays.asList(receivers));
	}
		
	public final void addReceiver(IReceivable receiver) {
		_receivers.add(receiver);
	}

	public final void removeReciever(IReceivable receiver) {
		_receivers.remove(receiver);
	}

	protected final void notifyReceivers() {
		for(IReceivable receiver : _receivers) {
			for(DispatcherOperation operation : _operations) {
				receiver.executeRegisteredOperation(this, operation);
			}
		}
	}
				
	protected final void doneUpdating() {
		if(_operations.isEmpty()) {
			_operations.add(DispatcherOperation.Refresh);
		}
		
		notifyReceivers();
		_operations.clear();
	}	
	
	protected final void addOperation(DispatcherOperation operation) { 
		_operations.add(operation); 
	}
	
	protected final DispatcherOperation[] getOperations() {
		DispatcherOperation[] operations = new DispatcherOperation[_operations.size()];
		_operations.toArray(operations);
		return operations;
	}
}