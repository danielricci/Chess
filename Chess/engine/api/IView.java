package api;

import models.GameModel;

public interface IView extends IDestructable, IReceivable {
	public void render();
	public void refresh(GameModel model);
	public void register();
}