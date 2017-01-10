package api;

import java.util.Collection;

import communication.internal.dispatcher.Operation;

public interface IReceivable {
	public boolean isValidListener(Operation... operation);
	public Collection<Operation> getRegisteredOperations();
}