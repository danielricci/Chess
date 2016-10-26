package engine.managers;
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
		/** Define the list of resources below */
		ChessTitle("ChessTitle"),
		FileMenu("FileMenu"),
		ExitMenu("ExitMenu"),
		HelpMenu("HelpMenu"),
		AboutMenu("AboutMenu");
	
		/**-------------------------------------*/
		
		/**
		 * The key of the resource
		 */
		public final String key;
		
		/**
		 * Constructs an object of this class
		 * 
		 * @param key The key to use as a reference to the properties file
		 */
		private Resources(String key)
		{
			this.key = key;
		}
	}
	
	/**
	 * Constructs an object of this class
	 * 
	 * @param locale The locale to use
	 */
	private ResourcesManager(Locale locale)
	{
		_locale = locale;
	}
	
	/**
	 * Gets the singleton instance
	 * 
	 * @return The singleton instance
	 */
	public static ResourcesManager Instance()
	{
		if(_instance == null)
		{
			_instance = new ResourcesManager(null);
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
	public String Get(Resources resource)
	{
		ResourceBundle messages = ResourceBundle.getBundle("resources", _instance._locale);
		return messages.getString(resource.key);			
	}
}
