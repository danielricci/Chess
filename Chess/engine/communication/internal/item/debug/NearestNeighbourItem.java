package communication.internal.item.debug;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import communication.internal.ItemComponent;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;

public class NearestNeighbourItem extends ItemComponent {

	public NearestNeighbourItem(JComponent parent) {
		super(new JCheckBoxMenuItem(ResourcesManager.Get(Resources.HighlightNeighbors)), parent);
	}

	@Override public void onExecute() {
	}
}