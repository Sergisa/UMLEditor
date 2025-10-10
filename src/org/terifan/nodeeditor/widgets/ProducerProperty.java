package org.terifan.nodeeditor.widgets;

import org.terifan.nodeeditor.Context;
import org.terifan.nodeeditor.Direction;
import org.terifan.nodeeditor.NodeEditorPane;
import org.terifan.nodeeditor.Property;
import org.terifan.ui.Anchor;

import java.awt.*;


public abstract class ProducerProperty extends Property<ProducerProperty>
{
	private static final long serialVersionUID = 1L;


	public ProducerProperty(String aLabel)
	{
		super(aLabel);
	}


	@Override
	protected void paintComponent(NodeEditorPane aPane, Graphics2D aGraphics, boolean aHover)
	{
		mTextBox
			.setBounds(getBounds())
			.setAnchor(getConnectors().isEmpty() || getConnectors().get(0).getDirection() == Direction.IN ? Anchor.WEST : Anchor.EAST)
			.render(aGraphics);
	}


	@Override
	public abstract Object execute(Context aContext);


	@Override
	public String toString()
	{
		return "ValueProperty{" + super.toString() + '}';
	}
}
