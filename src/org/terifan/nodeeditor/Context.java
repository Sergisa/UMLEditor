package org.terifan.nodeeditor;

import org.terifan.boxcomponentpane.NodeCanvasView;

public class Context {
	private final NodeCanvasView mEditor;


	public Context(NodeCanvasView aEditor) {
		mEditor = aEditor;
	}


	public NodeCanvasView getEditor() {
		return mEditor;
	}

}
