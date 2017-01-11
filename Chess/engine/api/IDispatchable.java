package api;

import java.util.List;

import communication.internal.dispatcher.Operation;

public interface IDispatchable<T> {
		
	public class Message<U>
	{ 
		public final Object sender;
		public final List<Object> args;
		public final Operation operation;
		public final List<U> resources;
		
		public Message(Object sender, Operation operation, List<U> resources, List<Object> args) {
			this.sender = sender;
			this.operation = operation;
			this.resources = resources;
			this.args = args;
		}
	}

	public <U extends T> void SendMessage(Object sender, Operation operation, Class<U> type, Object... args);
}
