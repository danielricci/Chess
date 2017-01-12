package communication.internal.dispatcher;

import java.util.List;

public class DispatcherMessage<U> {
	public final Object sender;
	public final List<Object> args;
	public final DispatcherOperation operation;
	public final List<U> resources;
		
	public DispatcherMessage(Object sender, DispatcherOperation operation, List<U> resources, List<Object> args) {
		this.sender = sender;
		this.operation = operation;
		this.resources = resources;
		this.args = args;
	}
}
