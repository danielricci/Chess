package api;

import java.awt.event.ActionListener;
import java.util.Map;

import communication.internal.dispatcher.DispatcherOperation;

public interface IReceivable {
	
	/**
	 * Executes the specified operation based on the operations registered by the IReceivable entity
	 * 
	 * @param sender The sender
	 * @param operation The operation
	 */
	public void executeRegisteredOperation(Object sender, DispatcherOperation operation);
	
	/**
	 * Gets the list of registered operation by the entity
	 * 
	 * @return Map<DispatcherOperation, ActionListener>
	 */
	public Map<DispatcherOperation, ActionListener> getRegisteredOperations();
}