package code.Fractals;

public class Mandelbrot {
	
	private int _max;
	private final double MIN_X = -2.15;
	private final double MAX_X = 0.6;
	private final double MIN_Y = -1.3;
	private final double MAX_Y = 1.3;
	
	/**
	 * Automagically sets the max to 255.  Max can be set with setMax(int max)
	 */
	public Mandelbrot(){
		_max = 255;
	}
	
	/**
	 * Set the max amount of passes
	 */
	public void setMax(int max){
		_max = max;
	}

	/**
	 * Returns the 2-D array of points
	 * @return int[][] points
	 */
	public int[][] getPoints() {
		int[][] points = new int[512][512];
		double xCalc, yCalc, tX, tY, tmp, dist;
		for(int rows = 0; rows < 512; rows++){ // y
			for(int cols = 0; cols < 512; cols++){ // x
				// convert rows and cols to cartesian plot
				xCalc = ((4.0) / 512.0) * cols - 2;
				yCalc = ((4.0) / 512.0) * rows - 2;
				tX = 0;
				tY = 0;
				
				int passes = -1;
				dist = Math.sqrt(tX*tX + tY*tY);
				while(dist < 2 && passes < _max){
					tmp = tX*tX - tY*tY + xCalc;
					tY = 2.0 * tX * tY + yCalc;
					tX = tmp;
					dist = Math.sqrt(tX*tX + tY*tY);
					passes++;
				}
				points[cols][rows] = passes;
			}
		}
		return points;
	}

	/**\
	 * Returns the max amount of passes
	 * @return max
	 */
	public int getMaxEscapes() {
		return _max;
	}

}
