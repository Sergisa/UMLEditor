package org.terifan.boxcomponentpane;

import examples.MandelbrotExample;
import org.terifan.nodeeditor.Connection;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;
import org.terifan.nodeeditor.graphics.Popup;
import org.terifan.nodeeditor.graphics.SplineRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.util.ArrayList;
import java.util.function.Function;

import static org.terifan.nodeeditor.Styles.SELECTION_RECTANGLE_STROKE;


public class DiagramView extends JComponent {
	@Serial
	private final static long serialVersionUID = 1L;
	private final boolean DEBUG = MandelbrotExample.DEBUG;
	private double mScale;
	private Point2D.Double mScroll;
	private Point mDragStartLocation;
	private Point mDragEndLocation;
	private Rectangle mSelectionRectangle;
	private NodeModel mModel;
	private ArrayList<DiagramView> mSelectedBoxes;
	private transient Function<String, BufferedImage> mIconProvider;

	private transient Popup mPopup;
	private boolean mRemoveInConnectionsOnDrop;

	public DiagramView(NodeModel aModel) {
		mSelectedBoxes = new ArrayList<>();
		mScale = 1;
		mModel = aModel;
		setFocusable(true);
		setupListeners();
		//mBindings = new HashMap<>();
		mRemoveInConnectionsOnDrop = true;
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					if (!getSelectedNodes().isEmpty()) {
						//getModel().removeComponents((List<Node>) getSelectedNodes());
						repaint();
					}
				}
			}
		});
	}

	protected void setupListeners() {
	}

	public double getScale() {
		return mScale;
	}

	public DiagramView setScale(double aScale) {
		mScale = aScale;
		return this;
	}

	public NodeModel getModel() {
		return mModel;
	}

	public ArrayList<DiagramView> getSelectedNodes() {
		return mSelectedBoxes;
	}

	/**
	 * Move all nodes to the center of the screen
	 */
	public DiagramView center() {
		if (mModel.getComponents().isEmpty()) {
			return this;
		}

		Rectangle bounds = new Rectangle(mModel.getComponents().get(0).getBounds());
		for (BoxComponent box : mModel.getComponents()) {
			box.layout();
			bounds.add(box.getBounds());
		}

		int dx = -(int) bounds.getCenterX();
		int dy = -(int) bounds.getCenterY();

		for (BoxComponent box : mModel.getComponents()) {
			box.getBounds().translate(dx, dy);
		}

		mScroll = null; // will be centered when pane is repainted
		return this;
	}

	@Override
	public Dimension getPreferredSize() {
		Rectangle bounds = null;
		for (Node box : mModel.getComponents()) {
			box.layout();
			if (bounds == null) {
				bounds = box.getBounds();
			} else {
				bounds.add(box.getBounds());
			}
		}

		return bounds.getSize();
	}

	protected void paintBackground(Graphics2D aGraphics) {
		int w = getWidth();
		int h = getHeight();
		int sx = (int) mScroll.x;
		int sy = (int) mScroll.y;

		float gcr = Styles.PANE_GRID_COLOR_3.getRed() / 255f;
		float gcg = Styles.PANE_GRID_COLOR_3.getGreen() / 255f;
		float gcb = Styles.PANE_GRID_COLOR_3.getBlue() / 255f;

		aGraphics.setColor(Styles.PANE_BACKGROUND_COLOR);
		aGraphics.fillRect(0, 0, w, h);

		for (int i = 0; i < 10; i++) {
			double s = mScale * Math.pow(5, i);
			if (s > 15 && s < w) {
				aGraphics.setColor(new Color(gcr, gcg, gcb, Math.min((float) (s / 200), 1f)));
				drawGrid(aGraphics, w, h, s);
			}
		}

		aGraphics.setColor(Styles.PANE_GRID_COLOR_1);
		aGraphics.drawLine(0, sy - 1, w, sy - 1);
		aGraphics.drawLine(0, sy + 1, w, sy + 1);
		aGraphics.drawLine(sx - 1, 0, sx - 1, h);
		aGraphics.drawLine(sx + 1, 0, sx + 1, h);
		aGraphics.setColor(Styles.PANE_GRID_COLOR_2);
		aGraphics.drawLine(0, sy, w, sy);
		aGraphics.drawLine(sx, 0, sx, h);
	}

	private void drawGrid(Graphics2D aGraphics, int aW, int aH, double aScale) {
		int xi = (int) ((mScroll.x - aW / 2) / aScale);
		int yi = (int) ((mScroll.y - aH / 2) / aScale);
		int wr = (int) Math.ceil(1 + aW / 2 / aScale);
		int hr = (int) Math.ceil(1 + aH / 2 / aScale);

		for (int i = 0; i < wr; i++) {
			int x0 = (int) ((-i - xi) * aScale + mScroll.x);
			int x1 = (int) ((+i - xi) * aScale + mScroll.x);
			aGraphics.drawLine(x0, 0, x0, aH);
			aGraphics.drawLine(x1, 0, x1, aH);
		}
		for (int i = 0; i < hr; i++) {
			int y0 = (int) ((-i - yi) * aScale + mScroll.y);
			int y1 = (int) ((+i - yi) * aScale + mScroll.y);
			aGraphics.drawLine(0, y0, aW, y0);
			aGraphics.drawLine(0, y1, aW, y1);
		}
	}

	@Override
	protected void paintComponent(Graphics aGraphics) {
		if (mScroll == null) {
			mScroll = new Point.Double(getWidth() / 2.0, getHeight() / 2.0);
		}

		for (Node box : mModel.getComponents()) {
			box.layout();
		}

		Graphics2D g = (Graphics2D) aGraphics;
		AffineTransform oldTransform = g.getTransform();

		paintBackground(g);

		g.translate((int) mScroll.x, (int) mScroll.y);
		paintBoxComponents(g);
		paintSelectionRectangle(g);
		g.setTransform(oldTransform);

		paintOverlay(g);
	}

	protected void paintBoxComponents(Graphics2D aGraphics) {
		aGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);


		aGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		aGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		for (Node box : mModel.getComponents()) {
			paintBoxComponent(aGraphics, box, mSelectedBoxes.contains(box));
		}

		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		if (mPopup != null) {
			paintBoxComponent(aGraphics, mPopup, false);
		}

		for (Connection<Property> connection : mModel.getConnections()) {
			Color start = Styles.CONNECTOR_COLOR_INNER_FOCUSED;
			SplineRenderer.drawSpline(aGraphics, connection, getScale(), Styles.CONNECTOR_COLOR_OUTER, start, start);
		}
	}

	protected void paintSelectionRectangle(Graphics2D aGraphics) {
		if (mSelectionRectangle != null) {
			aGraphics.setColor(Styles.PANE_SELECTION_RECTANGLE_BACKGROUND);
			aGraphics.fillRect(mSelectionRectangle.x, mSelectionRectangle.y, mSelectionRectangle.width + 1, mSelectionRectangle.height + 1);
			aGraphics.setColor(Styles.PANE_SELECTION_RECTANGLE_LINE);
			aGraphics.setStroke(SELECTION_RECTANGLE_STROKE);
			aGraphics.draw(mSelectionRectangle);
		}
	}

	protected void paintOverlay(Graphics2D aGraphics) {
	}

	protected void paintBoxComponent(Graphics2D aGraphics, Renderable aComponent, boolean aSelected) {
		Rectangle bounds = aComponent.getBounds();
		int x = (int) (bounds.x * mScale);
		int y = (int) (bounds.y * mScale);
		int width = (int) (bounds.width * mScale);
		int height = (int) (bounds.height * mScale);

		if (aGraphics.hitClip(x, y, width, height)) {
			AffineTransform ot = aGraphics.getTransform();

			AffineTransform transform = aGraphics.getTransform();
			transform.translate(x, y);
			transform.scale(mScale, mScale);

			Graphics2D ig = (Graphics2D) aGraphics.create();
			ig.setTransform(transform);
			ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			ig.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

			aComponent.paintComponent(this, ig, bounds.width, bounds.height, aSelected);
			if (DEBUG) {
				aGraphics.setColor(Color.RED);
				aGraphics.draw(aComponent.getBounds());
				System.out.println("RED Bounds ON: " + aComponent.getBounds());
			}
			aGraphics.setTransform(ot);
		}
	}

	public Point calcMousePoint(Point aPoint) {
		return new Point(
			(int) ((aPoint.x - mScroll.x) / mScale),
			(int) ((aPoint.y - mScroll.y) / mScale)
		);
	}

	public DiagramView setPopup(Popup aPopup) {
		mPopup = aPopup;
		return this;
	}
}
