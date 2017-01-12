package api;

import communication.internal.dispatcher.DispatcherOperation;

public interface IDispatchable<T> {
	public <U extends T> void SendMessage(Object sender, DispatcherOperation operation, Class<U> type, Object... args);
}
