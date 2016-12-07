package api;

import controllers.BaseController;

public interface IBaseComponent {
	
	public boolean visibility();
	public boolean enabled();
	
	public void onExecute();

	public void bind(BaseController controller);
}