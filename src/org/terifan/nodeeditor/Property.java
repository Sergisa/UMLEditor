package org.terifan.nodeeditor;

import org.terifan.boxcomponentpane.NodeCanvasView;
import org.terifan.ui.TextBox;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

import static org.terifan.nodeeditor.Styles.BOX_FOREGROUND_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_FOREGROUND_SHADOW_COLOR;


public abstract class Property<T extends Property> implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;


	private final Rectangle mBounds;

	protected Node mNode;
	protected Dimension mPreferredSize;
	protected boolean mUserSetSize;
	protected String mId;
	protected String mModelId;
	protected TextBox mTextBox;


	public Property() {
		mPreferredSize = new Dimension();
		mBounds = new Rectangle();
		mTextBox = new TextBox("")
			.setFont(Styles.BOX_ITEM_FONT)
			.setShadow(BOX_FOREGROUND_SHADOW_COLOR, 1, 1)
			.setForeground(BOX_FOREGROUND_COLOR);
	}


	protected Property(String aText) {
		this();

		mTextBox.setText(aText);
		mPreferredSize.setSize(mTextBox.measure().getSize());
	}


	protected abstract void paintComponent(NodeCanvasView aPane, Graphics2D aGraphics, boolean aHover);


	public String getId() {
		return mId;
	}


	public T setId(String aId) {
		mId = aId;
		return (T) this;
	}


	void bind(Node aNode) {
		mNode = aNode;
	}


	public Node getNode() {
		return mNode;
	}


	public String getText() {
		return mTextBox.getText();
	}


	public T setText(String aText) {
		mTextBox.setText(aText);
		return (T) this;
	}


	protected Dimension measure() {
		if (!mUserSetSize && mTextBox.isLayoutRequired()) {
			mPreferredSize.setSize(mTextBox.measure().getSize());
		}

		return (Dimension) mPreferredSize.clone();
	}


	public Dimension getPreferredSize() {
		return mPreferredSize;
	}


	public void setPreferredSize(Dimension aPreferredSize) {
		mUserSetSize = true;
		mPreferredSize.setSize(aPreferredSize);
	}


	public Rectangle getBounds() {
		return mBounds;
	}


	protected void connectionsChanged(NodeCanvasView aPane, Point aClickPoint) {
	}


	/**
	 * Should return true if the clicked point will perform an action. This method return false.
	 */
	protected boolean mousePressed(NodeCanvasView aPane, Point aClickPoint) {
		return false;
	}


	protected void mouseReleased(NodeCanvasView aPane, Point aClickPoint) {
	}


	protected void mouseDragged(NodeCanvasView aPane, Point aClickPoint, Point aDragPoint) {
	}


	// ugly, remove somehow
	public void fireMouseReleased(NodeCanvasView aPane, Point aPoint) {
		mouseReleased(aPane, aPoint);
	}


	@Override
	public String toString() {
		return "Property{" + "mId=" + getId() + ", Node=" + mNode + "}";
	}
}
