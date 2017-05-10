package code.Fractals;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.SwingUtilities;

import code.UI.FractalViewer;

public class Julia extends Fractal {

	private double pointX = 0.188887, pointY = -0.72689;
	private volatile boolean reset = false;

	/**
	 * Most basic constructor
	 */
	public Julia() {
		this(new Dimension(512, 512));
	}

	/**
	 * Constructor with dimension parameters
	 * 
	 * @param rows
	 *            number of rows
	 * @param cols
	 *            number of columns
	 */
	public Julia(Dimension resolution) {
		super("Julia", resolution, 1.7, -1.7, 1.0, -1.0, (FractalViewer.get() != null));
		this.coolX = -0.09998952442329354;
		this.coolY = 0.6397321930046009;
	}

	public int calculate(double xCalc, double yCalc) {
		double tX, tY, tmp;
		double dist;
		tX = xCalc;
		tY = yCalc;

		int passes = 0;
		dist = Math.sqrt(tX * tX + tY * tY);
		while (dist <= getMaxEscapeDistance() && passes < getMaxEscapeTime()) {
			tmp = tX * tX - tY * tY + pointY;
			tY = 2.0 * tX * tY + pointX;
			tX = tmp;
			dist = Math.sqrt(tX * tX + tY * tY);
			passes++;
		}
		return passes;
	}

	/**
	 * Julia needs a special reset.  First click is a soft reset, second is a hard reset.  The hard reset brings the fractal generation point back to the origina.
	 */
	@Override
	public void reset() {
		if (reset) {
			pointX = 0.188887;
			pointY = -0.72689;
		} else {
			System.out.println("Click reset again to go back to original Julia Set");
			reset = true;
		}
		super.reset();
	}

	@Override
	public boolean zoom(Rectangle rectangle) {
		reset = false; // allows for soft reset
		return super.zoom(rectangle);
	}

	/**
	 * Allows user to click around and change the generation point of Julia.
	 * @param x New X column
	 * @param y New Y row
	 */
	public void animate(int x, int y) {
		reset = false; // allows for soft reset
		this.pointX = getX(x); // new point X
		this.pointY = getY(y); // new point Y
		SwingUtilities.invokeLater(() -> calculateAndBlock()); // block
	}
}