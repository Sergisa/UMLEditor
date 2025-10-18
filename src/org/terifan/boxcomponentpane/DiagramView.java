package org.terifan.boxcomponentpane;

import examples.MandelbrotExample;
import org.terifan.nodeeditor.*;
import org.terifan.nodeeditor.graphics.Popup;
import org.terifan.nodeeditor.graphics.SplineRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serial;
import java.util.ArrayList;

import static org.terifan.nodeeditor.Styles.SELECTION_RECTANGLE_STROKE;


public class DiagramView extends JComponent {
	@Serial
	private final static long serialVersionUID = 1L;
	private final NodeModel mModel;
	private Rectangle mSelectionRectangle;
	private final ArrayList<Node> mSelectedBoxes;
	ScaledObjectAdapter scaledAdapter;
	private Point2D.Double coordinateShift;
	private double scale = 1;
	double scaleSpeed = 1.1;

	private transient Popup mPopup;
	private final boolean mRemoveInConnectionsOnDrop = true;

	public DiagramView(NodeModel aModel) {
		mSelectedBoxes = new ArrayList<>();
		mModel = aModel;
		setFocusable(true);
		scaledAdapter = new ScaledObjectAdapter(scale);
		MouseListener mouseListener = new MouseListener();
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addMouseWheelListener(mouseListener);
		enableEvents(AWTEvent.KEY_EVENT_MASK);
	}

	public void resetScale() {
		scale = 1;
		scaledAdapter.setScale(scale);
	}

	private void increaseScale(double value) {
		scale *= value;
		scaledAdapter.setScale(scale);
	}

	private void decreaseScale(double value) {
		scale /= value;
		scaledAdapter.setScale(scale);
	}

	public NodeModel getModel() {
		return mModel;
	}

	public ArrayList<Node> getSelectedNodes() {
		return mSelectedBoxes;
	}

	/**
	 * Move all nodes to the center of the screen
	 */
	public DiagramView centerItems() {
		if (mModel.getComponents().isEmpty()) {
			return this;
		}

		Rectangle bounds = new Rectangle(mModel.getComponents().getFirst().getBounds());
		for (BoxComponent<Node> box : mModel.getComponents()) {
			box.layout();
			bounds.add(box.getBounds());
		}

		int dx = -(int) bounds.getCenterX();
		int dy = -(int) bounds.getCenterY();

		for (BoxComponent<Node> box : mModel.getComponents()) {
			box.getBounds().translate(dx, dy);
		}

		coordinateShift = null; // will be centered when pane is repainted
		return this;
	}

	@Override
	protected void processKeyEvent(KeyEvent e) {
		if ((e.getID() == KeyEvent.KEY_PRESSED) && (e.getModifiersEx() == 0)) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_DELETE:
					if (!getSelectedNodes().isEmpty()) {
						getModel().removeComponents(getSelectedNodes());
					}
					break;
				case KeyEvent.VK_EQUALS:
				case KeyEvent.VK_ADD:
					increaseScale(scaleSpeed);
					break;
				case KeyEvent.VK_SUBTRACT:
				case KeyEvent.VK_MINUS:
					decreaseScale(scaleSpeed);
					break;
				case KeyEvent.VK_ESCAPE:
					getSelectedNodes().clear();
			}
			repaint();
		}
	}

	@Override
	public Dimension getPreferredSize() {
		Rectangle bounds = new Rectangle();
		for (Node box : mModel.getComponents()) {
			box.layout();
			bounds.add(box.getBounds());
		}
		return bounds.getSize();
	}

	protected void paintBackground(Graphics2D aGraphics) {
		int w = getWidth();
		int h = getHeight();
		int sx = (int) coordinateShift.x;
		int sy = (int) coordinateShift.y;

		float gcr = Styles.SECONDARY_GRID_COLOR.getRed() / 255f;
		float gcg = Styles.SECONDARY_GRID_COLOR.getGreen() / 255f;
		float gcb = Styles.SECONDARY_GRID_COLOR.getBlue() / 255f;

		aGraphics.setColor(Styles.PANE_BACKGROUND_COLOR);
		aGraphics.fillRect(0, 0, w, h);

		for (int i = 0; i < 10; i++) {
			double s = scale * Math.pow(5, i);
			if (s > 15 && s < w) {
				aGraphics.setColor(new Color(gcr, gcg, gcb, Math.min((float) (s / 200), 1f)));
				drawGrid(aGraphics, w, h, s);
			}
		}
		Stroke cachedStroke = aGraphics.getStroke();
		aGraphics.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		aGraphics.setColor(Styles.MAIN_AXIS_COLOR);
		aGraphics.drawLine(0, sy, w, sy);
		aGraphics.drawLine(sx, 0, sx, h);
		aGraphics.setStroke(cachedStroke);
	}

	private void drawGrid(Graphics2D aGraphics, int aW, int aH, double aScale) {
		int xi = (int) ((coordinateShift.x - (double) aW / 2) / aScale);
		int yi = (int) ((coordinateShift.y - (double) aH / 2) / aScale);
		int wr = (int) Math.ceil(1 + (double) aW / 2 / aScale);
		int hr = (int) Math.ceil(1 + (double) aH / 2 / aScale);

		for (int i = 0; i < wr; i++) {
			int x0 = (int) ((-i - xi) * aScale + coordinateShift.x);
			int x1 = (int) ((+i - xi) * aScale + coordinateShift.x);
			aGraphics.drawLine(x0, 0, x0, aH);
			aGraphics.drawLine(x1, 0, x1, aH);
		}
		for (int i = 0; i < hr; i++) {
			int y0 = (int) ((-i - yi) * aScale + coordinateShift.y);
			int y1 = (int) ((+i - yi) * aScale + coordinateShift.y);
			aGraphics.drawLine(0, y0, aW, y0);
			aGraphics.drawLine(0, y1, aW, y1);
		}
	}

	@Override
	protected void paintComponent(Graphics aGraphics) {
		if (coordinateShift == null) {
			coordinateShift = new Point.Double(getWidth() / 2.0, getHeight() / 2.0);
		}

		for (Node box : mModel.getComponents()) {
			box.layout();
		}

		Graphics2D g = (Graphics2D) aGraphics;
		AffineTransform oldTransform = g.getTransform();

		paintBackground(g);

		g.translate((int) coordinateShift.x, (int) coordinateShift.y);
		paintBoxComponents(g);
		paintSelectionRectangle(g);
		g.setTransform(oldTransform);
	}

	protected void paintBoxComponents(Graphics2D aGraphics) {
		aGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);


		aGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		aGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		for (Connection<Property> connection : mModel.getConnections()) {
			Color start = Styles.CONNECTOR_COLOR_INNER_FOCUSED;
			SplineRenderer.drawSpline(aGraphics, connection, scale, Styles.CONNECTOR_COLOR_OUTER, start, start);
		}

		for (Node box : mModel.getComponents()) {
			paintBoxComponent(aGraphics, box, mSelectedBoxes.contains(box));
		}

		aGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		if (mPopup != null) {
			paintBoxComponent(aGraphics, mPopup, false);
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

	protected void paintBoxComponent(Graphics2D aGraphics, Renderable aComponent, boolean aSelected) {
		Rectangle originalObjectBounds = aComponent.getBounds();
		Rectangle scaledBounds = scaledAdapter.setObject(aComponent).getBounds();

		if (aGraphics.hitClip(scaledBounds.x, scaledBounds.y, scaledBounds.width, scaledBounds.height)) {
			AffineTransform ot = aGraphics.getTransform();

			AffineTransform transform = aGraphics.getTransform();
			transform.translate(scaledBounds.x, scaledBounds.y);
			transform.scale(scale, scale);

			Graphics2D ig = (Graphics2D) aGraphics.create();
			ig.setTransform(transform);
			ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			ig.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

			aComponent.paintComponent(this, ig, originalObjectBounds.width, originalObjectBounds.height, aSelected);
			boolean DEBUG = MandelbrotExample.DEBUG;
			if (DEBUG) {
				aGraphics.setColor(Color.RED);
				aGraphics.draw(scaledAdapter.setObject(aComponent).getBounds());
			}
			aGraphics.setTransform(ot);
		}
	}

	/**
	 *
	 * @param aPoint Точка в системе координат Gui компонента. Система координат, растущая вправо и вниз из верхнего левого угла
	 * @return Возвращает точку в установленной системе координат
	 */
	public Point calcMousePoint(Point aPoint) {
		// получение точки в центрированной системе координат модели
		return new Point(
			(int) ((aPoint.x - coordinateShift.x) / scale),
			(int) ((aPoint.y - coordinateShift.y) / scale)
		);
	}

	public void setPopup(Popup aPopup) {
		mPopup = aPopup;
	}

	public class MouseListener extends MouseAdapter implements NodeViewEventListener {
		Point startPoint;
		Node hittedNode;

		@Override
		public void mousePressed(MouseEvent event) {
			startPoint = calcMousePoint(event.getPoint());
			Node node = getComponentAtPoint(event.getPoint());
			if (node != null) {
				hittedNode = node;
			} else if (SwingUtilities.isLeftMouseButton(event)) {
				onStartSelectionRectangle(startPoint, event.isControlDown());
			}
		}

		@Override
		public void mouseDragged(MouseEvent event) {
			if (hittedNode != null) {
				onNodeMoving(startPoint, calcMousePoint(event.getPoint()));
				mModel.moveTop(hittedNode);
				repaint();
			} else {
				if (SwingUtilities.isRightMouseButton(event)) {
					onCoordinateShifting(event);
				} else if (SwingUtilities.isLeftMouseButton(event)) {
					if (mSelectionRectangle != null) {
						onExtendSelectionRectangle(startPoint, calcMousePoint(event.getPoint()));
					}
				}
			}
			if (hittedNode != null || SwingUtilities.isRightMouseButton(event))
				startPoint = calcMousePoint(event.getPoint());
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent event) {
			hittedNode = null;
			if (mSelectionRectangle != null) {
				onSelectionRectangleEnd();
			}
			mSelectionRectangle = null;
			repaint();
		}

		@Override
		public void mouseClicked(MouseEvent event) {
			if (SwingUtilities.isRightMouseButton(event)) {
				mPopup = null;
				repaint();
				return;
			}

			Node node = getComponentAtPoint(event.getPoint());
			if (node != null) {
				onNodeClicked(event, node);
				repaint();
			} else {
				onPaneClicked(event, event.getPoint());
			}
		}

		@Override
		public void onPaneClicked(MouseEvent event, Point aPoint) {
			String templateMessage = "InCoordinateSystem: (%s, %s) \t ViewPoint: (%s, %s)";
			System.out.printf((templateMessage) + "%n",
				calcMousePoint(event.getPoint()).x,
				calcMousePoint(event.getPoint()).y,
				event.getX(),
				event.getY()
			);
			getSelectedNodes().clear();
		}

		@Override
		public void onNodeClicked(MouseEvent event, Node node) {
			String templateMessage = "<Node>\"%s\" (X,Y)(%s, %s) \t (W,H)(←·→%s, %s)";
			System.out.printf((templateMessage) + "%n",
				node.getTitle(),
				node.getBounds().x,
				node.getBounds().y,
				node.getBounds().width,
				node.getBounds().height
			);
			if (isMinimizeButtonPressed(node, event.getPoint())) {
				node.setMinimized(!node.isMinimized());
			}
			if (getSelectedNodes().size() == 1) {
				if (getSelectedNodes().getFirst() == node) getSelectedNodes().remove(node);
				else getSelectedNodes().set(0, node);
			} else {
				if (!event.isControlDown()) getSelectedNodes().clear();
				getSelectedNodes().add(node);
			}
		}

		@Override
		public void onCoordinateShifting(MouseEvent event) {
			coordinateShift.x += (event.getX() - startPoint.x);
			coordinateShift.y += (event.getY() - startPoint.y);
		}

		@Override
		public void onNodeMoving(Point startPoint, Point newPoint) {
			int dx = newPoint.x - startPoint.x;
			int dy = newPoint.y - startPoint.y;
			hittedNode.getBounds().translate(dx, dy);
		}

		@Override
		public void onStartSelectionRectangle(Point startPoint, boolean addingToSelection) {
			mSelectionRectangle = new Rectangle(startPoint);
		}

		@Override
		public void onExtendSelectionRectangle(Point startPoint, Point newPoint) {
			//TODO: попытаться использовать функцию Rectangle.add()
			int x0 = (int) (Math.min(startPoint.x, newPoint.x) * scale);
			int y0 = (int) (Math.min(startPoint.y, newPoint.y) * scale);
			int x1 = (int) (Math.max(startPoint.x, newPoint.x) * scale);
			int y1 = (int) (Math.max(startPoint.y, newPoint.y) * scale);
			mSelectionRectangle.setBounds(x0, y0, x1 - x0, y1 - y0);
		}

		@Override
		public void onSelectionRectangleEnd() {
			mSelectionRectangle.x /= scale;
			mSelectionRectangle.y /= scale;
			mSelectionRectangle.width /= scale;
			mSelectionRectangle.height /= scale;
			mSelectedBoxes.clear();
			for (Node node : getModel().getComponents()) {
				if (mSelectionRectangle.intersects(node.getBounds())) {
					mSelectedBoxes.add(node);
				}
			}
		}

		public Node getComponentAtPoint(Point aPoint) {
			return getModel().getComponentAt(calcMousePoint(aPoint));
		}

		public boolean isMinimizeButtonPressed(Node node, Point point) {
			return getMinimizeButtonBounds(node).contains(calcMousePoint(point));
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent event) {
			//сдвигаем плоскость туда, где стоит мышь
			coordinateShift.x -= event.getX();
			coordinateShift.y -= event.getY();
			if (event.getWheelRotation() == -1) {
				increaseScale(scaleSpeed);
				coordinateShift.x *= scaleSpeed;
				coordinateShift.y *= scaleSpeed;
			} else {
				decreaseScale(scaleSpeed);
				coordinateShift.x /= scaleSpeed;
				coordinateShift.y /= scaleSpeed;
			}

			coordinateShift.x += event.getX();
			coordinateShift.y += event.getY();
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			requestFocus();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			transferFocus();
		}

		protected Rectangle getMinimizeButtonBounds(Node aNode) {
			Rectangle b = aNode.getBounds();
			//TODO: обязательно переписать на константы, что бы не было чисел с неизвестным смыслом
			return new Rectangle(b.x + 11, b.y + 7, 20, 20);
		}
	}
}
