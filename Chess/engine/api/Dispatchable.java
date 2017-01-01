package api;

import controllers.BaseController;

public interface Dispatchable {
	
	public class Message<T extends BaseController>
	{ 
		public Message(Class<T> type) {
			_dispatchTo = type;
		}
		
		public enum MessageType
		{
			Debug_RenderNeighborTiles
		}
		
		public Object[] args = new Object[1 << 6];
		public MessageType type;
		public final Class<T> _dispatchTo;
	} 	
		
	void SendMessage(Message message);
	void BroadcastMessage(Message message);
}