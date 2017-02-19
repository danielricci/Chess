package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import engine.communication.internal.command.ItemComponent;
import engine.communication.internal.dispatcher.DispatcherOperation;
import engine.factories.ViewFactory;
import engine.managers.LocalizationManager;
import engine.managers.LocalizationManager.Resources;
import views.TileView;

public class NearestNeighbourItem extends ItemComponent {

	public NearestNeighbourItem(JComponent parent) {
		super(new JCheckBoxMenuItem(LocalizationManager.Get(Resources.HighlightNeighbors)), parent);
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