package org.terifan.nodeeditor.graphics;

import org.terifan.nodeeditor.Connection;
import org.terifan.nodeeditor.Property;
import org.terifan.nodeeditor.Styles;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import static org.terifan.nodeeditor.Styles.TITLE_HEIGHT_PADDED;


public class SplineRenderer {

	public static void drawSpline(Graphics2D aGraphics, Connection<Property> aConnection, double aScale, Color aBackgroundColor, Color aStartColor, Color aEndColor) {
		drawSplineImpl(aGraphics, createSpline(aConnection), aScale, aBackgroundColor, aStartColor, aEndColor);
	}

	private static void drawSplineImpl(Graphics2D aGraphics, BSpline aSpline, double aScale, Color aBackgroundColor, Color aStartColor, Color aEndColor) {
		Stroke old = aGraphics.getStroke();

		float strokeScale = (float) Math.sqrt(aScale);
		BasicStroke STROKE_OUTER = new BasicStroke(Styles.CONNECTOR_STROKE_WIDTH_OUTER * strokeScale, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
		BasicStroke STROKE_INNER = new BasicStroke(Styles.CONNECTOR_STROKE_WIDTH_INNER * strokeScale, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

		Path2D.Double spline = createPath(aSpline, aScale, 0.0, 1.0);
		aGraphics.setStroke(STROKE_OUTER);
		aGraphics.setColor(aBackgroundColor);
		aGraphics.draw(spline);

		aGraphics.setStroke(STROKE_INNER);
		if (aStartColor.equals(aEndColor)) {
			//GradientPaint gp = new GradientPaint(25, 25, Color.red, 15, 25, Color.orange, true);
			//aGraphics.setPaint(gp);
			aGraphics.setColor(aStartColor);
			aGraphics.draw(spline);
		}

		aGraphics.setStroke(old);
	}

	private static Path2D.Double createPath(BSpline aSpline, double aScale, double aStart, double aEnd) {
		int segments = Math.max(20, (int) aSpline.getPoint(0).distance(aSpline.getPoint(1)) / 4);

		Path2D.Double path = new Path2D.Double(Path2D.WIND_EVEN_ODD, segments);

		boolean first = true;
		for (int i = (int) (segments * aStart); i < (int) (segments * aEnd) + 1; i++) {
			Point2D.Double pt = aSpline.getPoint(i / (double) (segments - 1));
			if (first) {
				first = false;
				path.moveTo(pt.x, pt.y);
			} else {
				path.lineTo(pt.x, pt.y);
			}
		}

		AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale(aScale, aScale);
		path.transform(affineTransform);

		return path;
	}

	private static BSpline createSpline(Connection<Property> relation) {
		Property fromProperty = relation.getFrom();
		Property toProperty = relation.getTo();
		Rectangle propertyFromCoordinator = fromProperty.getCoordinationAdapter();
		Rectangle propertyToCoordinator = toProperty.getCoordinationAdapter();

		int x0;
		int x1;

		int d0 = 16;
		int d1 = 16;

		int y0 = (int) propertyFromCoordinator.getBounds().getCenterY();
		int y1 = (int) propertyToCoordinator.getBounds().getCenterY();

		int nodePad = fromProperty.getNode().horizontalPadding;

		if (fromProperty.getNode().isMinimized()) {
			y0 = (int) fromProperty.getNode().getBounds().getY() + nodePad + (TITLE_HEIGHT_PADDED / 2);
		}
		if (toProperty.getNode().isMinimized()) {
			y1 = (int) toProperty.getNode().getBounds().getY() + nodePad + (TITLE_HEIGHT_PADDED / 2);
		}

		if (fromProperty.preferRightConnectionToProperty(toProperty)) {
			x0 = (int) fromProperty.getCoordinationAdapter().getRightCoordinate();
		} else {
			x0 = (int) propertyFromCoordinator.getX();
			d0 *= -1;
		}

		if (toProperty.preferRightConnectionToProperty(fromProperty)) {
			x1 = (int) toProperty.getCoordinationAdapter().getRightCoordinate();
		} else {
			d1 *= -1;
			x1 = (int) propertyToCoordinator.getX();
		}

		return new BSpline(
			new double[]{x0, x0 + d0, x1 + d1, x1},
			new double[]{y0, y0, y1, y1}
		);
	}
}
