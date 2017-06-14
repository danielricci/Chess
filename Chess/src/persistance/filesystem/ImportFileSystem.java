package persistance.filesystem;

import javax.xml.bind.annotation.XmlRootElement;

import engine.communication.external.filesystem.AbstractFileSystem;

@XmlRootElement(name = "FileSystem")
// TODO - this should match the engine configuration
public final class ImportFileSystem extends AbstractFileSystem {
	/**
     * Default no-arg constructor as-per serialization guidelines dictates
     */
    public ImportFileSystem() {
    }
}