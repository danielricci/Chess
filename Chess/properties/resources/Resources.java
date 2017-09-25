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

package resources;

import engine.utils.globalisation.Localization;

/**
 * The resources information for localization and globalization
 *
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public final class Resources extends Localization<Resources.ResourceKeys> {
	
	private static Resources _instance;
	
	private Resources(){
	}
	
	public enum ResourceKeys {
		About("about"),
		AboutMessage("about_message"),
		CheckMate("checkmate_x"),
		Clear("clear"),
		Debug("debug"),
		DebugWindow("debug_window"),
		Exit("exit"),
		File("file"),
		GameIcon("game_icon"),
		Help("help"),
		Inspector("inspector"),
		MemClear("mem_clear"),
		MemPrint("mem_print"),
		MemRecall("mem_recall"),
		MemStore("mem_store"),
		NeighborTiles("neighbor_tiles"),
		NewGame("new_game"),
		NewGameDebug("new_game_debug"),
		Pieces("pieces"),
		Start("start"),
		Stop("stop"),
		Teams("teams"),
		TileIdentifier("tile_identifier"),
		Title("title"),
		Quit("quit"),
		QuitMessage("quit_question");
		 
		private String _key;
		
		ResourceKeys(String resourceKey) {
			_key = resourceKey;
		}
		
		@Override public String toString() {
			return _key;
		}
	}
	
	public static Resources instance(){
		if(_instance == null) {
			_instance = new Resources();
		}
		return _instance;
	}
}