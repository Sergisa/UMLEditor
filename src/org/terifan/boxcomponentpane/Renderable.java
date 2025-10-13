package org.terifan.boxcomponentpane;

import java.awt.*;


public interface Renderable<T extends BoxComponent, U extends BoxComponentPane> {
	Rectangle getBounds();


	void paintComponent(U aPane, Graphics2D aGraphics, int aWidth, int aHeight, boolean aSelected);
}
