package api;

import java.util.List;

import communication.internal.dispatcher.Operation;

public interface IDispatchable<T> {
	
	public class Message<U>
	{ 
		public final List<Object> args;
		public final Operation operation;
		public final List<U> resources;
		
		public Message(Operation operation, List<U> resources, List<Object> args) {
			this.operation = operation;
			this.resources = resources;
			this.args = args;
		}
	}

	public <U extends T> void SendMessage(Operation operation, Class<U> type, Object... args);
}
