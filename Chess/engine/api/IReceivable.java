package api;

import communication.internal.dispatcher.Operation;

public interface IReceivable {
	public void executeRegisteredOperation(Object sender, Operation operation);
}