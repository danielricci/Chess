package communication.internal.command.item;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import communication.internal.command.ItemComponent;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;

public class NearestNeighbourItem extends ItemComponent {

	public NearestNeighbourItem(JComponent parent) {
		super(new JCheckBoxMenuItem(ResourcesManager.Get(Resources.HighlightNeighbors)), parent);
	}

	@Override public void onExecute() {
	}
}