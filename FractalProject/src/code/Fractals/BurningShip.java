package code.Fractals;

public class BurningShip extends Fractal{
	
	public BurningShip(){
		this(512, 512);
	}
	
	public BurningShip(int rows, int cols){
		super(rows, cols, -1.8, -1.7, -0.08, 0.025);
	}

	@Override
	public int calculate(double xCalc, double yCalc) {
		double tX, tY, tmp, dist;
		tX = xCalc;
		tY = yCalc;
		int passes = 0; // Start passes at -1 because it passes once by default

		dist = Math.sqrt(tX * tX + tY * tY); // Calculate distance from origin

		while (dist < this.getEscapeDistance() && passes < this.getMaxEscapes()) {
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
