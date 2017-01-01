package api;

import controllers.BaseController;

public interface IReceiver<T extends BaseController> {
	// Define how a reciever is to register
	//void Register(); 
	
	// Define what should occur when a message is sent to the reciever
	//void RecieveMessage(Messa message); // Defines what to do when it recieves a message
}
