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

package game.controllers;

import java.util.Vector;

import game.interfaces.IDestructable;

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
	
	public <T extends BaseController> T getController(Class<T> controllerClass) {
		
		for(BaseController item : _controllers) {
			if(item.getClass() == controllerClass) {
				return (T)item;
			}
		}

		try {
	        _controllers.add(controllerClass.getConstructor().newInstance());	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return (T)_controllers.lastElement();
	}

	@Override public void destroy() {
		for(BaseController controller : _controllers) {
			controller.destroy();
		}
		_instance = null;
	}	
}