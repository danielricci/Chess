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
import views.BaseView;

public class ViewFactory implements IDestructable {

	private final Vector<BaseView> _views = new Vector<>(); 
	private static ViewFactory _instance;
		
	private ViewFactory() {
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
				return viewClass.getConstructor(argsClass).newInstance(args);
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
				_views.add(viewClass.getConstructor(argsClass).newInstance(args));
				return (T)_views.lastElement(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
		
	@Override public void dispose() {
		for(BaseView view : _views) {
			view.dispose();
		}
		_instance = null;
	}
}