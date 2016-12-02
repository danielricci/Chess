package communication;

public final class BaseComponentBuilder {

	private static BaseComponentBuilder _instance;
	
	private BaseComponentBuilder() {
	}
	
	public static BaseComponentBuilder instance() {
		if(_instance == null) {
			_instance = new BaseComponentBuilder();
		}
		
		return _instance;
	}
	
}