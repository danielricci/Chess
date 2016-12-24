package api;

import java.util.Observer;

import communication.internal.dispatcher.DispatchOperation;
import models.GameModel;

public interface IView extends IDestructable, Observer {
	public void render();
	public void refresh(GameModel model);
	public void register();
	public boolean isValidListener(DispatchOperation... operation);
}