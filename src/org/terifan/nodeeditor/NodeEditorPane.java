package org.terifan.nodeeditor;

import org.terifan.boxcomponentpane.BoxComponentPane;
import org.terifan.nodeeditor.graphics.Popup;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.util.function.Function;


public class NodeEditorPane extends BoxComponentPane<Node, NodeEditorPane> {
	@Serial
	private static final long serialVersionUID = 1L;

	private transient Function<String, BufferedImage> mIconProvider;

	private transient Property mClickedItem;
	private transient Popup mPopup;
	private transient Connection mSelectedConnection;

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


	@Override
	protected void paintBoxComponents(Graphics2D aGraphics) {

		aGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		super.paintBoxComponents(aGraphics);

		if (mPopup != null) {
			paintBoxComponent(aGraphics, mPopup, false);
		}
	}
}
