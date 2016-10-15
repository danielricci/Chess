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

package game.views;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JPanel;

import game.IDestructable;
import game.controllers.BaseController;
import game.models.GameModel;

public abstract class BaseView extends JPanel implements IDestructable, Observer {

	private final Vector<BaseController> _controllers = new Vector<>();
	
	protected BaseView(){
		registerListeners();
	}
	
	protected BaseView(BaseController... controllers) {			
		this();
		for(BaseController controller : controllers) {
			if(!controllerExists(controller)) {
				_controllers.add(controller);
			}			
		}
	}
	
	protected final <T extends BaseController> T getController(Class<T> controllerClass) {	
		BaseController myController = null;
		for(BaseController controller : _controllers) {
			if(controller.getClass() == controllerClass) {
				myController = controller;
				break;
			}
		}
		return (T) myController;
	}
	
	public final <T extends BaseController> void setController(T controller) {
		assert controller != null : "Cannot add null controller into baseview";
		if(!controllerExists(controller)) {
			_controllers.add(controller);
		}
	}
	
	private boolean controllerExists(BaseController controller) {
		assert controller != null : "Cannot pass a null controller";
		boolean found = false;
		
		for(BaseController _controller : _controllers) {
			if(_controller.getClass() == controller.getClass()) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	protected abstract void registerListeners();
	public abstract void render();
	public abstract void refresh(GameModel model);
	@Override public abstract void update(Observable o, Object arg); 
	
	@Override public void destroy() {
		_controllers.clear();
		removeAll();
	}
}