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

package views;

import java.awt.BorderLayout;
import java.util.logging.Level;

import engine.api.IView;
import engine.core.factories.AbstractSignalFactory;
import engine.core.factories.ViewFactory;
import engine.core.mvc.view.PanelView;
import engine.utils.io.logging.Tracelog;
import game.structure.GameMode;

public class MainWindowView extends PanelView {
	
	private final GameMode _gameMode;
	
	public MainWindowView(GameMode gameMode) {
		_gameMode = gameMode;
	}
	
	@Override public void initializeComponents() {
		setLayout(new BorderLayout());
	}

	@Override public void initializeComponentBindings() {
	}
	
	@Override public void render() {
		super.render();
		
		ViewFactory viewFactory = AbstractSignalFactory.getFactory(ViewFactory.class);
		IView boardView = null;
		if(_gameMode == GameMode.GAME) {
			boardView = viewFactory.get(BoardView.class, true);
		}
		else if(_gameMode == GameMode.DEBUG){
			boardView = viewFactory.get(BoardViewTester.class, true);
		}
			
		if(boardView != null) {
			add(boardView.getContainerClass());
			boardView.render();	
		}
		else {
			Tracelog.log(Level.SEVERE, true, "Cannot load the game because the board view could not be created.");
		}
	}
}