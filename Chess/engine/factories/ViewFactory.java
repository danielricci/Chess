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
import java.util.Vector;

import api.IDestructor;
import api.IDispatcher;
import api.IView;
import communication.internal.dispatcher.Dispatcher;
import communication.internal.dispatcher.DispatcherMessage;
import communication.internal.dispatcher.DispatcherOperation;
import views.BaseView;

public class ViewFactory implements IDestructor, IDispatcher<BaseView> {

	/**
	 * A message dispatcher used to communicate with views 
	 */
	Dispatcher<IView> _dispatcher = new Dispatcher<>();
	
	/**
     * Contains the history of all the views ever created by this factory, organized 
     * by class name and mapping to the list of all those classes
     */
    private final Map<String, Set<IView>> _history = new HashMap<>();

	private final Vector<BaseView> _views = new Vector<>(); 
	
	private static ViewFactory _instance;
		
	private ViewFactory() {
		_dispatcher.start();
	}
	
	/**
	 * Adds a view
	 * 
	 * @param controller The controller to add
	 * @param unique If the controller should be added into the exposed cache
	 */
	private void Add(BaseView view, boolean unique) { 
	    String viewName = view.getClass().getName();
	    
	    Set<IView> views = _history.get(viewName);
	    if(views == null) {
	        views = new HashSet<IView>();
	        _history.put(viewName, views);
	    }
	    views.add(view);
	    
	    if(!unique) {
	        _views.add(view);
	    }
	}
	
	public synchronized static ViewFactory instance() {
		if(_instance == null) {
			_instance = new ViewFactory();
		}
		return _instance;
	}
	
	public <T extends BaseView> T get(Class<T> viewClass, boolean unique, Object...args) {
		
		// Get the list of arguments together
		Class<?>[] argsClass = new Class<?>[args.length];
		for(int i = 0; i < args.length; ++i) {
			argsClass[i] = args[i].getClass();
		}
		
		// If its unique then we don't add it to our list of created items we just construct and
		// return it
		if(unique) {
			try {	
				T view = viewClass.getConstructor(argsClass).newInstance(args);
				Add(view, unique);
				return view;
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		else {
			for(BaseView item : _views) {
				if(item.getClass() == viewClass) {
					return (T)item;
				}
			}

			try {
				T view = viewClass.getConstructor(argsClass).newInstance(args);
				Add(view, unique);
				return view; 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
		
	@Override public void dispose() {
		for(BaseView view : _views) { // TODO - this needs to remove from _history
			view.dispose();
		}
		_instance = null;
	}

	@Override public <U extends BaseView> void SendMessage(Object sender, DispatcherOperation operation, Class<U> type, Object... args) {
		List<IView> resources = null;
		
		for(Set<IView> views : _history.values()) {
			if(views.iterator().next().getClass() == type) {
				resources = new ArrayList<>(views);
				break;
			} 
			continue;
		}

		DispatcherMessage<IView> message = new DispatcherMessage<IView>(sender, operation, resources, Arrays.asList(args));
		_dispatcher.add(message);
	}
}