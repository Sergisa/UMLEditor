package org.terifan.view;

import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.YELLOW;

public class Connector<P> implements Serializable, Renderable {
	@Serial
	private static final long serialVersionUID = 1L;

	private final int connectorRadius = Styles.NODE_BOX_MARGIN_LEFT;
	private Rectangle mBounds;
	protected Color mColor;

	private P owner;
	private int x;
	private int y;

	public Connector(Rectangle bounds) {
		mBounds = bounds;
	}

	public Connector() {
		this(new Rectangle());
		mColor = YELLOW;
	}

	public static <P> Connector<P> buildConnector(P owner) {
		Connector<P> connector = new Connector<>();
		connector.setOwner(owner);
		return connector;
	}

	@Override
	public Rectangle getBounds() {
		return mBounds;
	}

	@Override
	public void paintComponent(DiagramView aPane, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected) {
		aGraphics.setColor(mColor);
		Property property = (Property) owner;
		Node node = property.getNode();
		Property.MetaCoordinatorAdapter adapter = property.getCoordinationAdapter();
		aGraphics.fillOval(
			node.getBounds().x,
			adapter.y - connectorRadius / 2,
			connectorRadius * 2,
			connectorRadius * 2
		);
	}

	public P getOwner() {
		return owner;
	}

	public void setOwner(P owner) {
		this.owner = owner;
	}

	public enum Placement {
		TOP,
		BOTTOM,
		LEFT,
		RIGHT
	}
}
