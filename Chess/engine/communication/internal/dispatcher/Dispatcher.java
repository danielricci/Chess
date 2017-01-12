package communication.internal.dispatcher;

import java.util.concurrent.ConcurrentLinkedQueue;

import api.IController;

public class Dispatcher extends Thread {
	private volatile ConcurrentLinkedQueue<DispatcherMessage<?>> _messages = new ConcurrentLinkedQueue<>();		
	
	public void add(DispatcherMessage<?> message) {
		_messages.add(message);
	}
	
	@Override public void run() {
		while(true) {
			try {
				DispatcherMessage<?> message = _messages.poll();
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
