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

package api;

import java.awt.event.ActionListener;
import java.util.Map;

import communication.internal.dispatcher.DispatcherOperation;

/**
 * Contract that specifies how entities register to dispatched events
 */
public interface IReceiver {
	
	/**
	 * Executes the specified operation based on the operations registered by the IReceivable entity
	 * 
	 * @param sender The sender
	 * @param operation The operation
	 * @param args 
	 */
	public void executeRegisteredOperation(Object sender, DispatcherOperation operation);
	
	/**
	 * Gets the list of registered operation by the entity
	 * 
	 * @return Map<DispatcherOperation, ActionListener>
	 */
	public Map<DispatcherOperation, ActionListener> getRegisteredOperations();
}