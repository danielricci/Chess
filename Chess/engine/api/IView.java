package api;

import models.GameModel;

public interface IView extends IReceivable, IDestructable {
	public void render();
	public void refresh(GameModel model);
	public void register();
}