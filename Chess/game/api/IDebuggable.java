package api;

public interface IDebuggable {
	
	public enum DebugOption {
		Debugger_PlayerTiles,
		Debugger_TileCoordinates, 
		Debugger_HighlightNeighbors
	}
	
  	public void debuggerSelection(DebugOption operation, boolean selected);
}
