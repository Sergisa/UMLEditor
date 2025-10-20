package org.terifan.view;

import java.awt.*;


public interface Renderable {
	Rectangle getBounds();
	void paintComponent(DiagramView aPane, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected);
}
