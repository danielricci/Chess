package views;

import java.util.Observer;

import interfaces.IDestructable;
import models.GameModel;

public interface IView extends IDestructable, Observer {
	public void render();
	public void refresh(GameModel model);
	public void registerListeners();
}