package api;

import controllers.BaseController;
import views.BaseView;

public interface IMenuItem {
	public boolean isVisible();
	public boolean isEnabled();
	public void onExecute();

	public void bind(BaseView view);
	public void bind(BaseController controller);
}