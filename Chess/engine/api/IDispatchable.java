package api;

import java.util.List;

import communication.internal.dispatcher.DispatchOperation;

public interface IDispatchable<T> {	
	
	public class Message<U>
	{ 
		public final List<Object> args;
		public final DispatchOperation operation;
		public final List<U> resources;
		
		public Message(DispatchOperation operation, List<U> resources, List<Object> args) {
			this.operation = operation;
			this.resources = resources;
			this.args = args;	
		}
	}

	public <U extends T> void SendMessage(DispatchOperation operation, Class<U> type, Object... args);
}
