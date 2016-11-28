package api;

import controllers.BaseController;

public interface IBaseComponent {
	public boolean isVisible();
	public boolean isEnabled();
	public void onExecute();

	public void bind(BaseController controller);
}