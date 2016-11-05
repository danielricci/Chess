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

package controllers;

import java.awt.Component;
import java.util.Vector;

import api.IController;
import views.BaseView;

public abstract class BaseController implements IController  {
	
	private final Vector<BaseView> _views = new Vector<>();
	
	public BaseController() {	
	}
	
	/* TODO - if we want this to work we need to change "getView" to getUnique like
	          we did in controllers factory
	public <T extends BaseView> BaseController(Class<T>... views) {
		this();
		for(Class<T> view : views) {
			_views.add(ViewFactory.instance().getView(viewType))			
		}
	}
	*/
	public BaseController(BaseView... views) {
		this();
		for(BaseView view : views) {
			if(!viewExists(view)) {
				_views.add(view);
			}			
		}
	}

	protected final <T extends BaseView> T getView(Class<T> viewClass) {	
		BaseView baseView = null;
		for(BaseView view : _views) {
			if(view.getClass() == viewClass) {
				baseView = view;
				break;
			}
		}
		return (T) baseView;
	}
	
	protected final <T extends BaseView> void setView(T view) {
		assert view != null : "Cannot add null view into basecontroller";
		if(!viewExists(view)) {
			_views.add(view);
		}
	}
	
	private boolean viewExists(BaseView view) {
		assert view != null : "Cannot pass a null view";
		boolean found = false;
		
		for(BaseView baseView : _views) {
			if(baseView.getClass() == view.getClass()) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	public final void attachTo(Component component) {
		for(BaseView view : _views) {
			component.addadd(view);
		}
	}
	
	@Override public void dispose() {
		for(BaseView view : _views) {
			view.dispose();
		}
		_views.clear();
	}
}