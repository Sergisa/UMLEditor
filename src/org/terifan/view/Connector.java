package org.terifan.view;

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

	public Connector(Rectangle bounds) {
		mBounds = bounds;
	}

	public Connector(P owner) {
		this(new Rectangle());
		this.owner = owner;
		mBounds.setSize(connectorRadius * 2, connectorRadius * 2);
		mColor = YELLOW;
	}

	public static <P> Connector<P> buildConnector(P owner) {
		return new Connector<>(owner);
	}

	@Override
	public Rectangle getBounds() {
		return mBounds;
	}

	@Override
	public void paintComponent(DiagramView aPane, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected) {
		aGraphics.setColor(mColor);
		aGraphics.fillOval(
			mBounds.x,
			mBounds.y,
			mBounds.width,
			mBounds.height
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
