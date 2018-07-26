package org.geogebra.common.euclidian.background;

import org.geogebra.common.awt.GBasicStroke;
import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.euclidian.EuclidianStatic;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.main.settings.EuclidianSettings;

/**
 * Helper class for drawing the background
 * 
 * @author laszlo
 *
 */
public class DrawBackground {
	private EuclidianView view;
	private EuclidianSettings settings;
	private double gap;
	private GBasicStroke rulerStroke;

	/**
	 * 
	 * @param euclidianView
	 *            view
	 */
	public DrawBackground(EuclidianView euclidianView) {
		view = euclidianView;
		settings = view.getSettings();
	}

	/**
	 * Draws the background for MOW.
	 * 
	 * @param g2
	 *            graphics
	 */
	public void draw(GGraphics2D g2) {
		rulerStroke = EuclidianStatic.getStroke(settings.isRulerBold() ? 2f : 1f,
				settings.getRulerLineStyle());
		g2.setStroke(rulerStroke);
		gap = settings.getBackgroundRulerGap();
		switch (settings.getBackgroundType()) {
		case RULER:
			drawHorizontalLines(g2, 0, false);
			drawVerticalFrame(g2, 0, false);
			break;
		case SQUARE_BIG:
			gap = settings.getBackgroundRulerGap() / 2;
			drawSquaredBackground(g2, 0);
			break;
		case SQUARE_SMALL:
			drawSquaredSubgrid(g2, 0);
			drawSquaredBackground(g2, 0);
			break;
		case SVG:
			break;
		default:
			break;
		}
	}

	private void drawVerticalFrame(GGraphics2D g2, int i, boolean b) {
		double start = view.getYZero() % gap;
		// draw main grid
		g2.setColor(settings.getBgRulerColor());
		g2.startGeneralPath();

		double x = view.getXZero() - view.getWidth() / 2;
		double xEnd = x + view.getWidth();
		double yEnd = view.getHeight();
		double y = start - gap;
		g2.addStraightLineToGeneralPath(x, y, x, yEnd);
		g2.addStraightLineToGeneralPath(xEnd, y, xEnd, yEnd);
		g2.endAndDrawGeneralPath();
	}

	private void drawHorizontalLines(GGraphics2D g2, double xCrossPix1,
			boolean subgrid) {
		double start = view.getYZero() % gap;

		// draw main grid
		g2.setColor(subgrid ? settings.getBgSubLineColor()
				: settings.getBgRulerColor());
		g2.startGeneralPath();
		final double x = view.getXZero() - view.getWidth() / 2;
		double yEnd = view.getHeight();
		double y = start - gap;
		double width = view.getWidth() - (view.getWidth() % gap);

		if (subgrid) {
			double subGap = gap / 10;
			int lineCount = 0;
			while (y <= yEnd) {
				if (lineCount % 10 != 0) {
					addStraightLineToGeneralPath(g2, x, y, x + width, y);
				}
				y += subGap;
				lineCount++;
			}
		} else {
			while (y <= yEnd) {
				addStraightLineToGeneralPath(g2, x, y, x + width, y);
				y += gap;
			}
		}
		g2.endAndDrawGeneralPath();
	}

	private void drawVerticalLines(GGraphics2D g2, double xCrossPix1,
			boolean subgrid) {
		double start = view.getYZero() % gap;
		// draw main grid
		g2.setColor(subgrid ? settings.getBgSubLineColor()
				: settings.getBgRulerColor());
		g2.startGeneralPath();

		double x = view.getXZero() - view.getWidth() / 2;
		double xEnd = x + view.getWidth();
		double y = start - gap;
		double height = view.getHeight();

		if (subgrid) {
			double subGap = gap / 10;
			int lineCount = 0;
			xEnd = x + view.getWidth() - view.getWidth() % gap;
			while (x <= xEnd) {
				if (lineCount % 10 != 0) {
					addStraightLineToGeneralPath(g2, x, y, x, y + height);
				}
				x += subGap;
				lineCount++;
			}
		} else {
			while (x <= xEnd) {
				addStraightLineToGeneralPath(g2, x, y, x, y + height);
				x += gap;
			}
		}
		g2.endAndDrawGeneralPath();
	}

	private void drawSquaredBackground(GGraphics2D g2, double xCrossPix1) {
		drawHorizontalLines(g2, xCrossPix1, false);
		drawVerticalLines(g2, xCrossPix1, false);
	}

	private void drawSquaredSubgrid(GGraphics2D g2, double xCrossPix1) {
		drawHorizontalLines(g2, xCrossPix1, true);
		drawVerticalLines(g2, xCrossPix1, true);
	}

	private static void addStraightLineToGeneralPath(GGraphics2D g2, double x1,
			double y1, double x2, double y2) {
		g2.addStraightLineToGeneralPath(x1, y1, x2, y2);
	}
}
