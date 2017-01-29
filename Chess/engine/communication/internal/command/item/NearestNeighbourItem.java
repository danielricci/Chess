package communication.internal.command.item;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import communication.internal.command.ItemComponent;
import communication.internal.dispatcher.DispatcherOperation;
import factories.ViewFactory;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;
import views.TileView;

public class NearestNeighbourItem extends ItemComponent {

	public NearestNeighbourItem(JComponent parent) {
		super(new JCheckBoxMenuItem(ResourcesManager.Get(Resources.HighlightNeighbors)), parent);
	}

	@Override public void onExecute(ActionEvent actionEvent) {
		JCheckBoxMenuItem item = (JCheckBoxMenuItem) actionEvent.getSource();
		ViewFactory.instance().SendMessage(
			this,
			DispatcherOperation.ToggleNeighborTiles, 
			TileView.class,
			item.isSelected()
		);		
	}
}