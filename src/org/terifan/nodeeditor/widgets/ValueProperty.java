package org.terifan.nodeeditor.widgets;

import org.terifan.nodeeditor.*;
import org.terifan.ui.Anchor;

import java.awt.*;
import java.io.Serial;


public class ValueProperty extends Property<ValueProperty> {
	@Serial
	private static final long serialVersionUID = 1L;

	private Object mValue;


	public ValueProperty(String aLabel) {
		super(aLabel);

		mTextBox.setMargins(2, 0, 2, 0);
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover) {
		mTextBox
			.setBounds(getBounds())
			.setAnchor(getConnectors().isEmpty() || getConnectors().get(0).getDirection() == Direction.IN ? Anchor.WEST : Anchor.EAST)
			.render(aGraphics);
	}


	public Object getValue() {
		return mValue;
	}


	public ValueProperty setValue(Object aValue) {
		mValue = aValue;
		return this;
	}


	@Override
	public Object execute(Context aContext) {
		Connector in = getConnector(Direction.IN);

		if (in != null) {
			return in.getConnectedProperties().get(0).execute(aContext);
		}

		return mValue;
	}


	@Override
	public String toString() {
		return "ValueProperty{" + super.toString() + '}';
	}
}
