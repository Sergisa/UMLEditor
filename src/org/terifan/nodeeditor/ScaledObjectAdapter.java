package org.terifan.nodeeditor;

import org.terifan.view.DiagramView;
import org.terifan.view.Renderable;

import java.awt.*;

public class ScaledObjectAdapter implements Renderable {
	double scale;
	Renderable aRenderableObject;

	public ScaledObjectAdapter(double scale) {
		this.scale = scale;
		//this.aRenderableObject = aRenderableObject;
	}

	public ScaledObjectAdapter setObject(Renderable aRenderable) {
		this.aRenderableObject = aRenderable;
		return this;
	}

	@Override
	public Rectangle getBounds() {
		Rectangle notScaledBounds = aRenderableObject.getBounds();

		return new Rectangle(
			(int) (notScaledBounds.x * scale),
			(int) (notScaledBounds.y * scale),
			(int) (notScaledBounds.width * scale),
			(int) (notScaledBounds.height * scale)
		);
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	@Override
	public void paintComponent(DiagramView aPane, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected) {

	}
}
