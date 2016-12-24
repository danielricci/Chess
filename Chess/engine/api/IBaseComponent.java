package api;

import java.awt.event.ActionEvent;

import controllers.BaseController;

public interface IBaseComponent {
	
	public boolean visibility();
	public boolean enabled();
	
	public void onExecute(ActionEvent actionEvent);

	public void bind(BaseController controller);
}