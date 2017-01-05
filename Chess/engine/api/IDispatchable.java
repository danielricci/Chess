package api;

import communication.internal.dispatcher.DispatchOperation;

public interface IDispatchable<T> {	
	
	public class Message<U>
	{ 
		public final Object[] args;
		public final DispatchOperation operation;
		public final U[] resources;
		
		public Message(DispatchOperation operation, U[] resources, Object[] args) {
			this.operation = operation;
			this.resources = resources;
			this.args = args;	
		}
	}

	public <U extends T> void SendMessage(DispatchOperation operation, Class<U> type, Object... args);
}
