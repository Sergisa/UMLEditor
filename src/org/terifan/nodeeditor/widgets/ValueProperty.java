package org.terifan.nodeeditor.widgets;

import examples.MandelbrotExample;
import org.terifan.boxcomponentpane.DiagramView;
import org.terifan.nodeeditor.Property;

import java.awt.*;
import java.io.Serial;


public class ValueProperty extends Property {
	private final boolean DEBUG = MandelbrotExample.DEBUG;
	@Serial
	private static final long serialVersionUID = 1L;

	private Object mValue;


	public ValueProperty(String aLabel) {
		super(aLabel);

		mTextBox.setMargins(2, 0, 2, 0);
	}


	@Override
	protected void paintComponent(DiagramView aPane, Graphics2D aGraphics, boolean aHover) {
		if (DEBUG) {
			aGraphics.setColor(Color.RED);
			aGraphics.draw(getBounds());
		}
		mTextBox
			.setBounds(getBounds())
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
	public String toString() {
		return "ValueProperty{" + super.toString() + '}';
	}
}
