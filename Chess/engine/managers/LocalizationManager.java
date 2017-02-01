package managers;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Defines a class in charge of managing localization across an application
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public final class LocalizationManager {
	
	/**
	 * The single instance
	 */
	private static LocalizationManager _instance;
	
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
	private LocalizationManager()
	{
	}
	
	/**
	 * Gets the singleton instance
	 * 
	 * @return The singleton instance
	 */
	private static LocalizationManager Instance()
	{
		if(_instance == null)
		{
			_instance = new LocalizationManager();
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
