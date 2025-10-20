package org.terifan.view;

import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.Property;

import java.awt.*;
import java.awt.event.MouseEvent;

public interface NodeViewEventListener {
	public void onPaneClicked(MouseEvent event, Point aPoint);

	void onNodeClicked(MouseEvent event, Node node);

	void onPropertyClicked(MouseEvent event, Property clickedProperty);

	void onCoordinateShifting(Point startPoint, Point eventPoint);

	void onNodeMoving(Point startPoint, Point newPoint);

	void onStartSelectionRectangle(Point startPoint, boolean addingToSelection);

	void onExtendSelectionRectangle(Point startPoint, Point newPoint);

	void onSelectionRectangleEnd();
}
