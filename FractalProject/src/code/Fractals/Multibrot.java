package code.Fractals;

public class Multibrot extends Fractal{
	
	public Multibrot(){
		this(512, 512);
	}
	
	public Multibrot(int rows, int cols){
		super(rows, cols,-1f,1f,-1.3f,1.3f);
	}

	@Override
	public int calculate(float xCalc, float yCalc) {
		float tX, tY, tmp;
		double dist;
		
		// Mandelbrot starts every test from (0,0) AKA the origin
		tX = xCalc;
		tY = yCalc;
		
		
		int passes = 0; // Start passes at 0
		
		dist = Math.sqrt(tX*tX + tY*tY); // Use math to find distance from 0
		
		while(dist < this.getEscapeDistance() && passes < this.getMaxEscapes()){
			tmp = (float) (tX*tX*tX - (3.0*tX*tY*tY) + xCalc); // TEMP X: x' = x^2 - y^2 + (original x value)
			tY = (float) (3.0 * tX*tX * tY - tY*tY*tY + yCalc); // y' = 2xy + (original y value)
			tX = tmp; // set X to TEMP X
			dist = Math.sqrt(tX*tX + tY*tY); // recalculate distance
			passes++; // add a pass
		}
		return passes;
	}

}
