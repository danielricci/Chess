package communication.internal.command.item;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import communication.internal.command.ItemComponent;
import communication.internal.dispatcher.DispatcherOperation;
import controllers.TileController;
import factories.ControllerFactory;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;

public class NearestNeighbourItem extends ItemComponent {

	public NearestNeighbourItem(JComponent parent) {
		super(new JCheckBoxMenuItem(ResourcesManager.Get(Resources.HighlightNeighbors)), parent);
	}

	@Override public void onExecute(ActionEvent actionEvent) {
		JCheckBoxMenuItem item = (JCheckBoxMenuItem) actionEvent.getSource();
		ControllerFactory.instance().SendMessage(
			this,
			DispatcherOperation.ToggleNeighborTiles, 
			TileController.class,
			item.isSelected()
		);		
	}
}