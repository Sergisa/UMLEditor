package org.terifan.nodeeditor;

import org.terifan.boxcomponentpane.DiagramView;
import org.terifan.ui.TextBox;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

import static org.terifan.nodeeditor.Styles.BOX_FOREGROUND_COLOR;
import static org.terifan.nodeeditor.Styles.BOX_FOREGROUND_SHADOW_COLOR;


public abstract class Property implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;


	private final Rectangle mBounds;
	private String title;
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
		title = aText;
		mTextBox.setText(aText);
		mPreferredSize.setSize(mTextBox.measure().getSize());
	}

	protected abstract void paintComponent(DiagramView aPane, Graphics2D aGraphics, boolean aHover);

	public String getTitle() {
		return title;
	}

	public String getId() {
		return mId;
	}

	public Property setId(String aId) {
		mId = aId;
		return this;
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

	public Property setText(String aText) {
		mTextBox.setText(aText);
		return this;
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

	protected abstract int getLeftPad();

	protected abstract int getRightPad();

	protected abstract int getTopPad();

	protected void connectionsChanged(DiagramView aPane, Point aClickPoint) {
	protected abstract int getBottomPad();
	}


	/**
	 * Should return true if the clicked point will perform an action. This method return false.
	 */
	protected boolean mousePressed(DiagramView aPane, Point aClickPoint) {
		return false;
	}

	protected void mouseReleased(DiagramView aPane, Point aClickPoint) {
	}

	protected void mouseDragged(DiagramView aPane, Point aClickPoint, Point aDragPoint) {
	}

	public void fireMouseReleased(DiagramView aPane, Point aPoint) {
		mouseReleased(aPane, aPoint);
	}


	@Override
	public String toString() {
		return "Property{" + "<" + mNode + "." + getTitle() + ">}";
	}

	/**
	 * Вычисляет координату свойства. Не Текста, а свойства
	 */
	public class MetaCoordinatorAdapter extends Rectangle {
		private int nodeHPAD = mNode.horizontalPadding;
		private int nodeVPAD = mNode.verticalPadding;

		@Override
		public double getX() {
			return nodeHPAD + mNode.getBounds().x + getLeftPad();
		}

		@Override
		public double getY() {
			return nodeVPAD + mNode.getBounds().y + mBounds.getY() + getTopPad();
		}

		@Override
		public double getCenterX() {
			return (this.getX() + (getWidth())) / 2;
		}

		@Override
		public double getCenterY() {
			return (this.getY() + (getHeight() - (2 * nodeHPAD))) / 2;
		}

		public double getRightCoordinate() {
			return getX() + mBounds.width;
		}

		public double getBottomCoordinate() {
			return getX() + mBounds.height;
		}

		@Override
		public Rectangle getBounds() {
			System.out.print("Node: " + mNode.getBounds());
			System.out.println("\t\t Property: " + mBounds);
			Rectangle bounds = new Rectangle();
			bounds.setLocation((int) getX(), (int) getY());

			System.out.println("\t\t Property AFTER: " + bounds);
			return bounds;
		}
	}
}
