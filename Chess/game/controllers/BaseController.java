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

import javax.swing.JFrame;

import api.IController;
import views.BaseView;

/**
 * This class holds base controller implementation functionality for all controllers
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 */
public abstract class BaseController implements IController  {
	
	/**
	 * The view assigned to the controller
	 */
	private BaseView _view;
	
	/**
	 * Constructs a BaseController instance
	 */
	public BaseController() {
	}
	
	/**
	 * Constructs a BaseController with a specified view
	 * 
	 * @param view The view to attach to this controller
	 */
	public BaseController(BaseView view) {
		setView(view);
	}
	
	/**
	 * Gets the view
	 * 
	 * @param viewClass The casting class
	 * @return The view
	 */
	protected final <T extends BaseView> T getView(Class<T> viewClass) {	
		return (T)_view;
	}
	
	/**
	 * Sets the view
	 * 
	 * @param view
	 */
	protected final <T extends BaseView> void setView(T view) {
		_view = view;
	}
	
	/**
	 * Attaches the specified JFrame instance to the currently set view
	 *  
	 * @param root The root JFrame of the application
	 */
	public final void attachTo(JFrame root) {
		if(_view != null) {
			root.add(_view);			
		}
	}
	
	/**
	 * Disposes the currently set view
	 */
	@Override public void dispose() {
		_view.dispose();
		_view = null;
	}
}