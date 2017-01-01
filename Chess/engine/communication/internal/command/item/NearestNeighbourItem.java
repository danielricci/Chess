package communication.internal.command.item;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import api.Dispatchable.Message;
import api.Dispatchable.Message.MessageType;
import communication.internal.command.ItemComponent;
import controllers.TileController;
import factories.ControllerFactory;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;

public class NearestNeighbourItem extends ItemComponent {

	public NearestNeighbourItem(JComponent parent) {
		super(new JCheckBoxMenuItem(ResourcesManager.Get(Resources.HighlightNeighbors)), parent);
	}

	@Override public void onExecute(ActionEvent actionEvent) {
		Message<TileController> message = new Message<>(TileController.class);
		message.type = MessageType.Debug_RenderNeighborTiles;
		message.args[0] = true;
				
	    ControllerFactory.instance().SendMessage(message);
	}
}