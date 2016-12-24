package communication.internal.command.item;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import communication.internal.command.ItemComponent;
import controllers.BoardController;
import factories.ControllerFactory;
import managers.ResourcesManager;
import managers.ResourcesManager.Resources;

public class NearestNeighbourItem extends ItemComponent {

	public NearestNeighbourItem(JComponent parent) {
		super(new JCheckBoxMenuItem(ResourcesManager.Get(Resources.HighlightNeighbors)), parent);
	}

	@Override public void onExecute(ActionEvent actionEvent) {
		ControllerFactory.instance().get(BoardController.class, false).Test();		
	}
}