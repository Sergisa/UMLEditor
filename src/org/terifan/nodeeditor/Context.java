package org.terifan.nodeeditor;

import org.terifan.boxcomponentpane.DiagramView;

public class Context {
	private final DiagramView mEditor;


	public Context(DiagramView aEditor) {
		mEditor = aEditor;
	}


	public DiagramView getEditor() {
		return mEditor;
	}

}
