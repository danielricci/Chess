package api;

import javax.swing.JMenu;

import controllers.BaseController;

public interface IMenuItem {
	public boolean isVisible();
	public boolean isEnabled();
	public void onExecute();

	public void bind(JMenu view);
	public void bind(BaseController controller);
}