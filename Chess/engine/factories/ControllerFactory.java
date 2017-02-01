/**
* Daniel Ricci <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package factories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import api.IController;
import api.IDestructor;
import api.IDispatcher;
import communication.internal.dispatcher.Dispatcher;
import communication.internal.dispatcher.DispatcherMessage;
import communication.internal.dispatcher.DispatcherOperation;
import core.mvc.controller.BaseController;

public class ControllerFactory implements IDestructor, IDispatcher<BaseController> {
	
	/**
	 * A message dispatcher used to communicate with controller 
	 */
	Dispatcher<IController> _dispatcher = new Dispatcher<>();
	
    /**
     * Contains the history of all the controllers ever created by this factory, organized 
     * by class name and mapping to the list of all those classes
     */
    private final Map<String, Set<IController>> _history = new HashMap<>(); // TODO - can this be put into the dispatcher functionality?
    
    /**
     * Contains the list of all exposed unique controllers created by this factory.  An exposed
     * controller is one where the controller reference is marked to be stored by this factory
     * so that it may be referenced by others during the lifespan of the process
     */
	private final Set<BaseController> _controllers = new HashSet<>(); 
	
	/**
	 * Singleton instance of this class
	 */
	private static ControllerFactory _instance;
	
	/**
	 * Constructs a new object of this class
	 */
	private ControllerFactory() {
		_dispatcher.start();
	}
	
	/**
	 * Returns the singleton reference 
	 * 
	 * @return The singleton reference
	 */
	public synchronized static ControllerFactory instance() {
		if(_instance == null) {
			_instance = new ControllerFactory();
		}	
		return _instance;
	}
	
	/**
	 * Adds a controller
	 * 
	 * @param controller The controller to add
	 * @param isShared If the controller should be added into the exposed cache
	 */
	private void Add(BaseController controller, boolean isShared) { 
	    String controllerName = controller.getClass().getName();
	    
	    Set<IController> controllers = _history.get(controllerName);
	    if(controllers == null) {
	        controllers = new HashSet<IController>();
	        _history.put(controllerName, controllers);
	    }
	    controllers.add(controller);
	    
	    if(isShared) {
	        _controllers.add(controller);
	    }
	}

	/**
	 * Gets the specified type of resource
	 * 
	 * @param controllerClass The controller class to get
	 * @param isShared If the factory should keep tabs of this class
	 * @param args The arguments to pass into the controller class
	 * @return A reference to the specified class
	 */
	public <T extends BaseController> T get(Class<T> controllerClass, boolean isShared, Object...args) {
		
		if(isShared) {
			for(BaseController item : _controllers) {
				if(item.getClass() == controllerClass) {
					return (T)item;
				}
			}
		}
		
		// Get the list of arguments together
		Class<?>[] argsClass = new Class<?>[args.length];
		for(int i = 0; i < args.length; ++i) {
			argsClass[i] = args[i].getClass();
		}
		
		try {
		    T createdClass = controllerClass.getConstructor(argsClass).newInstance(args);
			Add(createdClass, isShared);
			return createdClass;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return null;
	}
	
	@Override public void dispose() {
		for(BaseController controller : _controllers) { // TODO - this needs to remove from _history
			controller.dispose();
		}
		_instance = null;
	}

	@Override public <U extends BaseController> void SendMessage(Object sender, DispatcherOperation operation, Class<U> type, Object... args) {
		
		List<IController> resources = null;
		
		for(Set<IController> controllers : _history.values()) {
			if(controllers.iterator().next().getClass() == type) {
				resources = new ArrayList<>(controllers);
				break;
			} 
			continue;
		}

		DispatcherMessage<IController> message = new DispatcherMessage<IController>(sender, operation, resources, Arrays.asList(args));
		_dispatcher.add(message);
	}
}