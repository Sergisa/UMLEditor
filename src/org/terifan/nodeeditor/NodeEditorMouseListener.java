package org.terifan.nodeeditor;

import org.terifan.boxcomponentpane.BoxComponent;
import org.terifan.boxcomponentpane.BoxComponentMouseListener;
import org.terifan.nodeeditor.graphics.Popup;
import org.terifan.nodeeditor.graphics.SplineRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import static org.terifan.nodeeditor.Styles.MIN_HEIGHT;
import static org.terifan.nodeeditor.Styles.MIN_WIDTH;


class NodeEditorMouseListener extends BoxComponentMouseListener<Node, NodeEditorPane> {
	private boolean mIgnoreNextMouseRelease;
	private final boolean mConnectorSelectionAllowed = true;
	private final boolean mRemoveInConnectionsOnDrop = false;
	private Property mSelectedProperty;


	public NodeEditorMouseListener(NodeEditorPane aPane) {
		super(aPane);
	}

	@Override
	public void mouseMoved(MouseEvent aEvent) {
		Popup popup = mViewPort.getPopup();
		Point point = mViewPort.calcMousePoint(aEvent.getPoint());

		if (popup != null) {
			popup.mouseMoved(point);
		} else {
			super.mouseMoved(aEvent);
		}
	}


	@Override
	public void mousePressed(MouseEvent aEvent) {
		mViewPort.requestFocus();
		mViewPort.grabFocus();
		NodeModel model = mViewPort.getModel();
		Popup popup = mViewPort.getPopup();

		if (popup != null) {
			mIgnoreNextMouseRelease = true;
			popup.mousePressed(aEvent);
			return;
		}

		mDragPoint = aEvent.getPoint();
		mClickPoint = mViewPort.calcMousePoint(aEvent.getPoint());
		mSelectedNode = mViewPort.getModel().getComponentAt(mClickPoint);

		if (!SwingUtilities.isLeftMouseButton(aEvent)) {
			super.mousePressed(aEvent);
			return;
		}

		if (mCursor != Cursor.DEFAULT_CURSOR && mCursor != Cursor.HAND_CURSOR) {
			mStartBounds = new Rectangle(mSelectedNode.getBounds());

			mViewPort.getSelectedNodes().clear();
			mViewPort.getSelectedNodes().add(mSelectedNode);
			mViewPort.setSelectedConnection(null);
			mViewPort.repaint();
			return;
		}

		mSelectedProperty = mSelectedNode == null ? null : mSelectedNode.getPropertyAt(mClickPoint);

		if (mSelectedProperty != null) {
			model.moveTop(mSelectedNode);
		}

		if (mSelectedProperty != null && mSelectedProperty.mousePressed(mViewPort, mClickPoint)) {
			mViewPort.getSelectedNodes().clear();
			mViewPort.getSelectedNodes().add(mSelectedNode);
			mViewPort.repaint();
			return;
		}

		updateSelections(aEvent, mSelectedNode);

		if (!mIsClickedNode) {
			mViewPort.setSelectionRectangle(new Rectangle(mClickPoint));
		}

		super.mousePressed(aEvent);
	}


	@Override
	public void mouseReleased(MouseEvent aEvent) {
		Popup popup = mViewPort.getPopup();
		NodeModel model = mViewPort.getModel();

		if (popup != null) {
			if (mIgnoreNextMouseRelease) {
				popup.mouseReleased(aEvent);
			}
			mIgnoreNextMouseRelease = false;
			mSelectedProperty = null;
			mViewPort.setSelectionRectangle(null);
			return;
		}

		mClickPoint = mViewPort.calcMousePoint(aEvent.getPoint());

		if (mCursor != Cursor.DEFAULT_CURSOR && mCursor != Cursor.HAND_CURSOR) {
			updateCursor(Cursor.DEFAULT_CURSOR);
			return;
		}

		if (mSelectedProperty != null) {
			mSelectedProperty.mouseReleased(mViewPort, mClickPoint);
			mSelectedProperty = null;
			mViewPort.repaint();
			return;
		}

		Rectangle selectionRectangle = mViewPort.getSelectionRectangle();
		if (selectionRectangle != null) {
			if (!aEvent.isControlDown()) {
				mViewPort.getSelectedNodes().clear();
			}

			double scale = mViewPort.getScale();
			selectionRectangle.x /= scale;
			selectionRectangle.y /= scale;
			selectionRectangle.width /= scale;
			selectionRectangle.height /= scale;

			for (Node node : mViewPort.getModel().getComponents()) {
				if (selectionRectangle.intersects(node.getBounds())) {
					if (!mViewPort.getSelectedNodes().contains(node)) {
						mViewPort.getSelectedNodes().add(node);
					} else if (aEvent.isControlDown()) {
						mViewPort.getSelectedNodes().remove(node);
					}
				}
			}
			mViewPort.setSelectionRectangle(null);
		}

		mViewPort.repaint();
	}


	@Override
	public void mouseDragged(MouseEvent aEvent) {
		Popup popup = mViewPort.getPopup();

		if (popup != null) {
			return;
		}

		Rectangle selectionRectangle = mViewPort.getSelectionRectangle();
		Point2D.Double paneScroll = mViewPort.getPaneScroll();

		Point newPoint = mViewPort.calcMousePoint(aEvent.getPoint());

		if (mCursor != Cursor.DEFAULT_CURSOR && mCursor != Cursor.HAND_CURSOR && SwingUtilities.isLeftMouseButton(aEvent)) {
			resizeNode(mSelectedNode, newPoint);
			return;
		}

		if (mSelectedProperty != null) {
			mSelectedProperty.mouseDragged(mViewPort, mClickPoint, newPoint);
			return;
		}
		if (selectionRectangle != null) {
			int x0 = (int) (Math.min(mClickPoint.x, newPoint.x) * mViewPort.getScale());
			int y0 = (int) (Math.min(mClickPoint.y, newPoint.y) * mViewPort.getScale());
			int x1 = (int) (Math.max(mClickPoint.x, newPoint.x) * mViewPort.getScale());
			int y1 = (int) (Math.max(mClickPoint.y, newPoint.y) * mViewPort.getScale());

			selectionRectangle.setBounds(x0, y0, x1 - x0, y1 - y0);
		} else {
			Point oldPoint = mClickPoint;
			mClickPoint = newPoint;

			if (SwingUtilities.isMiddleMouseButton(aEvent)) {
				paneScroll.x += (aEvent.getX() - mDragPoint.x);
				paneScroll.y += (aEvent.getY() - mDragPoint.y);
				mDragPoint = aEvent.getPoint();
			} else if (mIsClickedNode || SwingUtilities.isRightMouseButton(aEvent)) {
				for (Node node : mViewPort.getSelectedNodes()) {
					Point pt = node.getBounds().getLocation();
					pt.x += mClickPoint.x - oldPoint.x;
					pt.y += mClickPoint.y - oldPoint.y;
					node.setLocation(pt.x, pt.y);
				}
			}
		}

		mViewPort.repaint();
	}


	@Override
	protected void updateMinimize(MouseEvent aEvent, Node aNode) {
		super.updateMinimize(aEvent, aNode);
		updateSelections(aEvent, aNode);
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent aEvent) {
		Popup popup = mViewPort.getPopup();

		if (popup != null) {
			popup.mouseWheelMoved(aEvent);
			return;
		}

		Point2D.Double scroll = mViewPort.getPaneScroll();

		scroll.x -= aEvent.getX();
		scroll.y -= aEvent.getY();

		if (aEvent.getWheelRotation() == 1) {
			mViewPort.setScale(mViewPort.getScale() * mZoomSpeed);
			scroll.x *= mZoomSpeed;
			scroll.y *= mZoomSpeed;
		} else {
			mViewPort.setScale(mViewPort.getScale() / mZoomSpeed);
			scroll.x /= mZoomSpeed;
			scroll.y /= mZoomSpeed;
		}

		scroll.x += aEvent.getX();
		scroll.y += aEvent.getY();

		mViewPort.repaint();
	}


	private void updateSelections(MouseEvent aEvent, Node aClickedNode) {
		NodeModel model = mViewPort.getModel();
		Node newSelection = null;
		Node newClicked = null;

		if (aClickedNode != null) {
			Rectangle shrunkBounds = new Rectangle(aClickedNode.getBounds());
			shrunkBounds.grow(-5, -4);

			if (shrunkBounds.contains(mClickPoint)) {
				newClicked = aClickedNode;

				boolean b = mViewPort.getSelectedNodes().contains(aClickedNode);
				if (aEvent.isControlDown()) {
					if (b) {
						mViewPort.getSelectedNodes().remove(aClickedNode);
					} else {
						newSelection = aClickedNode;
					}
				} else if (!b) {
					mViewPort.getSelectedNodes().clear();
					newSelection = aClickedNode;
				}
			}
		}

		mIsClickedNode = newClicked != null;

		if (newSelection != null) {
			mViewPort.getSelectedNodes().add(newSelection);
		}

		if (mIsClickedNode) {
			mViewPort.getModel().moveTop(newClicked);
			mViewPort.setSelectedConnection(null);
		} else if (mConnectorSelectionAllowed) {
			double dist = 50;
			Connection nearest = null;
			for (Connection c : model.getConnections()) {
				double d = SplineRenderer.distance(c, mClickPoint);
				if (d < dist) {
					dist = d;
					nearest = c;
				}
			}
			mViewPort.setSelectedConnection(nearest);
			if (nearest != null) {
				mViewPort.getSelectedNodes().clear();
			}
		}

		mViewPort.repaint();
	}


	@Override
	protected int getCursor(Point aPoint, BoxComponent aNode) {
		if (aNode == null) {
			return Cursor.DEFAULT_CURSOR;
		}

		boolean rx = aNode.isResizableHorizontal();
		boolean ry = aNode.isResizableVertical();

		if (!rx && !ry) {
			return Cursor.DEFAULT_CURSOR;
		}

		int PX = 5;
		int PY = 4;
		Rectangle bounds = aNode.getBounds();

		if (aPoint.y - PY < bounds.y + 2 * PY) {
			if (aPoint.x - PX < bounds.x + 2 * PX) {
				return rx ? ry ? Cursor.NW_RESIZE_CURSOR : Cursor.W_RESIZE_CURSOR : Cursor.N_RESIZE_CURSOR;
			}
			if (aPoint.x + PX >= bounds.x + bounds.width - 2 * PX) {
				return rx ? ry ? Cursor.NE_RESIZE_CURSOR : Cursor.E_RESIZE_CURSOR : Cursor.N_RESIZE_CURSOR;
			}
			if (aPoint.y - PY < bounds.y + PY && ry) {
				return Cursor.N_RESIZE_CURSOR;
			}
		} else if (aPoint.y + PY >= bounds.y + bounds.height - 2 * PY) {
			if (aPoint.x - PX < bounds.x + 2 * PX) {
				return rx ? ry ? Cursor.SW_RESIZE_CURSOR : Cursor.W_RESIZE_CURSOR : Cursor.S_RESIZE_CURSOR;
			}
			if (aPoint.x + PX >= bounds.x + bounds.width - 2 * PX) {
				return rx ? ry ? Cursor.SE_RESIZE_CURSOR : Cursor.E_RESIZE_CURSOR : Cursor.S_RESIZE_CURSOR;
			}
			if (aPoint.y + PY >= bounds.y + bounds.height - PY && ry) {
				return Cursor.S_RESIZE_CURSOR;
			}
		} else if (aPoint.x - PX < bounds.x + PX && rx) {
			return Cursor.W_RESIZE_CURSOR;
		} else if (aPoint.x + PX > bounds.x + bounds.width - PX && rx) {
			return Cursor.E_RESIZE_CURSOR;
		}

		return Cursor.DEFAULT_CURSOR;
	}


	private void resizeNode(Node aNode, Point aPoint) {
		Rectangle b = aNode.getBounds();

		int minWidth = Math.max(MIN_WIDTH, aNode.getMinimumSize().width);
		int minHeight = Math.max(MIN_HEIGHT, aNode.getMinimumSize().height);

		switch (mCursor) {
			case Cursor.W_RESIZE_CURSOR:
			case Cursor.NW_RESIZE_CURSOR:
			case Cursor.SW_RESIZE_CURSOR:
				int o = b.x;
				b.x = Math.min(mStartBounds.x - mClickPoint.x + aPoint.x, mStartBounds.x + mStartBounds.width - minWidth);
				b.width += o - b.x;
				break;
		}

		switch (mCursor) {
			case Cursor.N_RESIZE_CURSOR:
			case Cursor.NW_RESIZE_CURSOR:
			case Cursor.NE_RESIZE_CURSOR:
				int o = b.y;
				b.y = Math.min(mStartBounds.y - mClickPoint.y + aPoint.y, mStartBounds.y + mStartBounds.height - minHeight);
				b.height += o - b.y;
				break;
		}

		switch (mCursor) {
			case Cursor.SW_RESIZE_CURSOR:
			case Cursor.S_RESIZE_CURSOR:
			case Cursor.SE_RESIZE_CURSOR:
				b.height = mStartBounds.height - mClickPoint.y + aPoint.y;
				break;
		}

		switch (mCursor) {
			case Cursor.E_RESIZE_CURSOR:
			case Cursor.SE_RESIZE_CURSOR:
			case Cursor.NE_RESIZE_CURSOR:
				b.width = mStartBounds.width - mClickPoint.x + aPoint.x;
				break;
		}

		b.width = Math.min(aNode.getMaximumSize().width, Math.max(minWidth, b.width));
		b.height = Math.min(aNode.getMaximumSize().height, Math.max(minHeight, b.height));

		aNode.getBounds().setBounds(b);
		mViewPort.repaint();
	}
}
