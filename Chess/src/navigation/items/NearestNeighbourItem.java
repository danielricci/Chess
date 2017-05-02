package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import engine.core.option.types.OptionItem;

public class NearestNeighbourItem extends OptionItem {

	public NearestNeighbourItem(JComponent parent) {
		super(new JCheckBoxMenuItem("Highlight Neighbours"), parent);
	}

	@Override public void onExecute(ActionEvent actionEvent) {
		//JCheckBoxMenuItem item = (JCheckBoxMenuItem) actionEvent.getSource();
		/*ViewFactory.instance().SendMessage(
			this,
			"ToggleNeighborTiles", 
			TileView.class,
			item.isSelected()
		);*/		
	}
}