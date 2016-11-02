package managers;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Defines a class in charge of managing localization across an application
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public final class ResourcesManager {
	
	/**
	 * The single instance
	 */
	private static ResourcesManager _instance;
	
	/**
	 * The locale being used
	 */
	private Locale _locale = Locale.CANADA;
	
	/**
	 * Resources used to define what can be fetched from the properties
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 *
	 */
	public enum Resources
	{
		ChessTitle,
		ChessTitleDeveloper,
		FileMenu,
		ExitMenu,
		HelpMenu,
		AboutMenu,
		NewGame, 
		ResetPosition,
		HighlightNeighbors;
	}
	
	/**
	 * Constructs an object of this class
	 * 
	 */
	private ResourcesManager()
	{
	}
	
	/**
	 * Gets the singleton instance
	 * 
	 * @return The singleton instance
	 */
	private static ResourcesManager Instance()
	{
		if(_instance == null)
		{
			_instance = new ResourcesManager();
		}
		
		return _instance;
	}
	
	/**
	 * Gets the specified resource
	 * 
	 * @param resource The resource to get
	 * 
	 * @return The value of the resource specified
	 */
	public static String Get(Resources resource)
	{
		ResourceBundle messages = ResourceBundle.getBundle("internal.resources", Instance()._locale);
		return messages.getString(resource.toString().toLowerCase());			
	}
}
