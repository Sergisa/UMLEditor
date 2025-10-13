package org.terifan.nodeeditor;

import org.terifan.boxcomponentpane.BoxComponentPane;
import org.terifan.nodeeditor.graphics.Popup;
import org.terifan.nodeeditor.graphics.SplineRenderer;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.util.ArrayList;
import java.util.function.Function;


public class NodeEditorPane extends BoxComponentPane<Node, NodeEditorPane> {
	@Serial
	private static final long serialVersionUID = 1L;

	private transient Function<String, BufferedImage> mIconProvider;

	private transient Property mClickedItem;
	private transient Popup mPopup;
	private transient Connection mSelectedConnection;
	private transient Connector mConnectorDragFrom;

	private boolean mConnectorSelectionAllowed;
	private boolean mRemoveInConnectionsOnDrop;


	public NodeEditorPane(NodeModel aModel) {
		super(aModel);

		//mBindings = new HashMap<>();
		mRemoveInConnectionsOnDrop = true;
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					if (!getSelectedNodes().isEmpty()) {
						getModel().removeComponents(getSelectedNodes());
						repaint();
					}
				}
			}
		});
		setIconProvider(Styles::loadIcon);
	}

	public NodeEditorPane setIconProvider(Function<String, BufferedImage> aProvider) {
		mIconProvider = aProvider;
		return this;
	}


	public Function<String, BufferedImage> getIconProvider() {
		return mIconProvider;
	}


	@Override
	protected void setupListeners() {
		NodeEditorMouseListener mouseListener = new NodeEditorMouseListener(this);
		addMouseMotionListener(mouseListener);
		addMouseListener(mouseListener);
		addMouseWheelListener(mouseListener);
	}


	@Override
	public NodeModel getModel() {
		return (NodeModel) super.getModel();
	}


	public boolean isRemoveInConnectionsOnDrop() {
		return mRemoveInConnectionsOnDrop;
	}


	public NodeEditorPane setRemoveInConnectionsOnDrop(boolean aRemoveInConnectionsOnDrop) {
		mRemoveInConnectionsOnDrop = aRemoveInConnectionsOnDrop;
		return this;
	}


	public boolean isConnectorSelectionAllowed() {
		return mConnectorSelectionAllowed;
	}


	public NodeEditorPane setConnectorSelectionAllowed(boolean aConnectorSelectionAllowed) {
		mConnectorSelectionAllowed = aConnectorSelectionAllowed;
		return this;
	}


	public Popup getPopup() {
		return mPopup;
	}


	public NodeEditorPane setPopup(Popup aPopup) {
		mPopup = aPopup;
		return this;
	}


	public Property getClickedItem() {
		return mClickedItem;
	}


	public void setClickedItem(Property aClickedItem) {
		mClickedItem = aClickedItem;
	}


	public Connection getSelectedConnection() {
		return mSelectedConnection;
	}


	public void setSelectedConnection(Connection aSelectedConnection) {
		mSelectedConnection = aSelectedConnection;
	}


	public Connector getConnectorDragFrom() {
		return mConnectorDragFrom;
	}


	public void setConnectorDragFrom(Connector aConnectorDragFrom) {
		mConnectorDragFrom = aConnectorDragFrom;
	}


	public Connector findNearestConnector(Point aPoint, Node aPrioritizeNode, boolean aDropTarget) {
		Connector nearest = null;
		double dist = aDropTarget ? 16 : 8;

		for (Node node : (ArrayList<Node>) getModel().getComponents()) {
			if (mConnectorDragFrom != null && mConnectorDragFrom.getProperty().getNode() == node) {
				continue;
			}

			Rectangle b = node.getBounds();
			int x = aPoint.x - b.x;
			int y = aPoint.y - b.y;

			for (Property item : node.getProperties()) {
				for (Connector c : (ArrayList<Connector>) item.getConnectors()) {
					double dx = x - c.getBounds().getCenterX();
					double dy = y - c.getBounds().getCenterY();
					double d = Math.sqrt(dx * dx + dy * dy);
					if (d < dist && (aPrioritizeNode == null || node == aPrioritizeNode || nearest == null)) {
						nearest = c;
						dist = d;
					}
				}
			}
		}

		return nearest;
	}


	@Override
	protected void paintBoxComponents(Graphics2D aGraphics) {
		NodeModel model = (NodeModel) getModel();

		aGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		for (Connection connection : model.getConnections()) {
			if (connection != mSelectedConnection) {
				ArrayList<Node> selectedBoxes = getSelectedNodes();
				boolean selected = selectedBoxes.contains(connection.getOut().getProperty().getNode()) || selectedBoxes.contains(connection.getIn().getProperty().getNode());

				Color start = selected ? Styles.CONNECTOR_COLOR_INNER_FOCUSED : connection.mOut.getColor();
				Color end = selected ? Styles.CONNECTOR_COLOR_INNER_FOCUSED : connection.mIn.getColor();

				SplineRenderer.drawSpline(aGraphics, connection, getScale(), Styles.CONNECTOR_COLOR_OUTER, start, end);
			}
		}

		super.paintBoxComponents(aGraphics);

		if (getDragEndLocation() != null) {
			if (mConnectorDragFrom.getDirection() == Direction.OUT) {
				SplineRenderer.drawSpline(aGraphics, getDragStartLocation(), getDragEndLocation(), getScale(), Styles.CONNECTOR_COLOR_OUTER, Styles.CONNECTOR_COLOR_INNER_DRAGGED, Styles.CONNECTOR_COLOR_INNER_DRAGGED);
			} else {
				SplineRenderer.drawSpline(aGraphics, getDragEndLocation(), getDragStartLocation(), getScale(), Styles.CONNECTOR_COLOR_OUTER, Styles.CONNECTOR_COLOR_INNER_DRAGGED, Styles.CONNECTOR_COLOR_INNER_DRAGGED);
			}
		}

		if (mSelectedConnection != null) {
			SplineRenderer.drawSpline(aGraphics, mSelectedConnection, getScale(), Styles.CONNECTOR_COLOR_OUTER_SELECTED, mSelectedConnection.mOut.getColor(), mSelectedConnection.mIn.getColor());
		}

		if (mPopup != null) {
			paintBoxComponent(aGraphics, mPopup, false);
		}
	}
}
