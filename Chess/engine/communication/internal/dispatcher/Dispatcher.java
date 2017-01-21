package communication.internal.dispatcher;

import java.util.concurrent.ConcurrentLinkedQueue;

import api.IController;
import api.IReceivable;

public class Dispatcher<T extends IReceivable> extends Thread {
	private volatile ConcurrentLinkedQueue<DispatcherMessage<T>> _messages = new ConcurrentLinkedQueue<>();		
	
	public void add(DispatcherMessage<T> message) {
		_messages.add(message);
	}
	
	@Override public void run() {
		while(true) {
			try {
				DispatcherMessage<T> message = _messages.poll();
				if(message != null) {
					for(Object resource : message.resources)
					{
						((IController)resource).executeRegisteredOperation(message.sender, message.operation);
					}
				}
				Thread.sleep(220);						
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
	}
}
