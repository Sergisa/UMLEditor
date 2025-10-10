package org.terifan.nodeeditor;

import org.terifan.nodeeditor.widgets.ImageProperty;

import java.awt.*;


public interface ImagePainter
{
	/**
	 * @return true if the image was painted
	 */
	boolean paintImage(NodeEditorPane aPane, Node aNode, ImageProperty aPropertyItem, Graphics aGraphics, Rectangle aBounds) throws Exception;
}
