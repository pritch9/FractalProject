package code.Fractals;

import java.awt.Dimension;

import code.UI.FractalViewer;

public class Mandelbrot extends Fractal{
	
	/**
	 * Most basic constructor
	 */
	public Mandelbrot(){
		this(new Dimension(512,512));
	}
	
	/**
	 * Constructor with dimension parameters
	 * @param rows number of rows
	 * @param cols number of columns
	 */
	public Mandelbrot(Dimension resolution){
		super("Mandelbrot", resolution, 0.6, -2.15, 1.3, -1.3, (FractalViewer.get() != null));
		this.coolX = -0.04459784684064594;
		this.coolY = -0.6544778177492164;
	}

	@Override
	public int calculate(double xCalc, double yCalc) {
		double tX, tY, tmp;
		double dist;
		tX = xCalc;
		tY = yCalc;
		
		
		int passes = 0; // Start passes at -1 because it passes once by default
		
		dist = Math.sqrt(xCalc*xCalc + yCalc*yCalc); // (0,0) has a distance of 0 from (0,0)
		
		while(dist <= this.getMaxEscapeDistance() && passes < this.getMaxEscapeTime()){
			tmp = tX*tX - tY*tY + xCalc; // TEMP X: x' = x^2 - y^2 + (original x value)
			tY = 2.0 * tX * tY + yCalc; // y' = 2xy + (original y value)
			tX = tmp; // set X to TEMP X
			dist = Math.sqrt(tX*tX + tY*tY); // recalculate distance
			
			passes++; // add a pass
		}
		return passes;
	}
}
