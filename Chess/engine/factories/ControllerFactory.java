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

import java.util.Vector;

import api.IDestructable;
import controllers.BaseController;

public class ControllerFactory implements IDestructable {

	private final Vector<BaseController> _controllers = new Vector<>(); 
	private static ControllerFactory _instance;
	
	private ControllerFactory() {
	}
	
	public synchronized static ControllerFactory instance() {
		if(_instance == null) {
			_instance = new ControllerFactory();
		}	
		return _instance;
	}

	public <T extends BaseController> T get(Class<T> controllerClass, boolean unique, Object...args) {
		
		// Get the list of arguments together
		Class<?>[] argsClass = new Class<?>[args.length];
		for(int i = 0; i < args.length; ++i) {
			argsClass[i] = args[i].getClass();
		}
		
		// If its unique then we don't add it to our list of created items we just construct and
		// return it
		if(unique) {
			try {			
				return controllerClass.getConstructor(argsClass).newInstance(args);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		else {
			for(BaseController item : _controllers) {
				if(item.getClass() == controllerClass) {
					return (T)item;
				}
			}

			try {
				_controllers.add(controllerClass.getConstructor(argsClass).newInstance(args));
				return (T)_controllers.lastElement(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	@Override public void dispose() {
		for(BaseController controller : _controllers) {
			controller.dispose();
		}
		_instance = null;
	}	
}