package api;

import java.awt.event.ActionListener;
import java.util.Map;

import communication.internal.dispatcher.DispatcherOperation;

public interface IReceivable {
	public void executeRegisteredOperation(Object sender, DispatcherOperation operation);
	public Map<DispatcherOperation, ActionListener> getRegisteredOperations();
}