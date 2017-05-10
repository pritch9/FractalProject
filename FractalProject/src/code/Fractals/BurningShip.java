package code.Fractals;

import java.awt.Dimension;

import code.UI.FractalViewer;

public class BurningShip extends Fractal{
	
	/**
	 * Most basic constructor
	 */
	public BurningShip(){
		this(new Dimension(512,512));
	}
	
	/**
	 * Constructor with dimension parameters
	 * @param rows number of rows
	 * @param cols number of columns
	 */
	public BurningShip(Dimension resolution){
		super("Burning Ship", resolution, -1.8, -1.7, -0.08, 0.025, (FractalViewer.get() != null));
		this.coolX = -1.748492062910211;
		this.coolY = -0.022531386833453692;
	}

	@Override
	public int calculate(double xCalc, double yCalc) {
		double tX, tY, tmp, dist;
		tX = xCalc;
		tY = yCalc;
		int passes = 0; // Start passes at -1 because it passes once by default

		dist = Math.sqrt(tX * tX + tY * tY); // Calculate distance from origin

		while (dist <= this.getMaxEscapeDistance() && passes < this.getMaxEscapeTime()) {
			tmp = tX * tX - tY * tY + xCalc; // TEMP X: x' = x^2 - y^2 +
												// (original x value)
			tY = Math.abs(2.0 * tX * tY) + yCalc; // y' = Math.abs(2xy) +
													// (original y value)
			tX = tmp; // set X to TEMP X
			dist = Math.sqrt(tX * tX + tY * tY); // recalculate distance
			passes++; // add a pass
		}
		return passes;
	}
	
}
