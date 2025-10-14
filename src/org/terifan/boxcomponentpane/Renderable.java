package org.terifan.boxcomponentpane;

import java.awt.*;


public interface Renderable {
	Rectangle getBounds();


	void paintComponent(NodeCanvasView aPane, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected);
}
