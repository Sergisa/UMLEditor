package org.terifan.nodeeditor.widgets;

import examples.MandelbrotExample;
import org.terifan.boxcomponentpane.DiagramView;
import org.terifan.nodeeditor.Property;

import java.awt.*;
import java.io.Serial;


public class ValueProperty extends Property {
	private final boolean DEBUG = MandelbrotExample.DEBUG;
	private final int leftPadding = 0;
	private final int rightPadding = leftPadding;
	private final int bottomPadding = 4;
	private final int topPadding = bottomPadding;
	@Serial
	private static final long serialVersionUID = 1L;

	private Object mValue;


	public ValueProperty(String aLabel) {
		super(aLabel);

		mTextBox.setMargins(topPadding, leftPadding, bottomPadding, rightPadding);
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
	protected int getLeftPad() {
		return leftPadding;
	}

	@Override
	protected int getBottomPad() {
		return bottomPadding;
	}

	@Override
	protected int getRightPad() {
		return rightPadding;
	}

	@Override
	protected int getTopPad() {
		return topPadding;
	}

	@Override
	public String toString() {
		return "ValueProperty{" + super.toString() + '}';
	}
}
