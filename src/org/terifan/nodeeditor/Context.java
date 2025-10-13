package org.terifan.nodeeditor;

public class Context {
	private final NodeEditorPane mEditor;


	public Context(NodeEditorPane aEditor) {
		mEditor = aEditor;
	}


	public NodeEditorPane getEditor() {
		return mEditor;
	}

}
