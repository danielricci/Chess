package api;

import java.util.Observer;

import models.GameModel;

public interface IView extends Observer, IReceivable, IDestructable {
	public void render();
	public void refresh(GameModel model);
	public void register();
}