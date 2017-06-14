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

package persistance.builder;

import java.io.InputStream;

import engine.communication.external.builder.AbstractBuilder;
import engine.communication.internal.persistance.IXMLCodec.XMLCodec;
import persistance.filesystem.ImportFileSystem;


/**
 * Data builder used for building data from the editor and
 * other data places
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class DataBuilder extends AbstractBuilder<ImportFileSystem> {
	
	@Override public boolean buildStart() {
		try {
			// Create the codec for the file system
			XMLCodec codec = new XMLCodec(ImportFileSystem.class);
				
			// Reference the data file
			InputStream inStream = getClass().getResourceAsStream("/generated/tilemap.xml");
			
			// Get the selected map file and unmarshal it
			Object fileSystem = codec.getUnmarshaller().unmarshal(inStream);
			
			// Create the file system 
			_fileSystem = (ImportFileSystem) fileSystem;

		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		// Return true to indicate everything went well
		return true;
	}
	
	@Override public void buildContent() {
	}

	@Override public void buildEnd() {
	}
}