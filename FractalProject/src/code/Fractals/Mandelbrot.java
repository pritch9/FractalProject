package code.Fractals;

public class Mandelbrot {
	
	private int _max;
	
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
		double cX, cY, tmp;
		for(int rows = 0; rows < 512; rows++){ // y
			for(int cols = 0; cols < 512; cols++){ // x
				cX = ((4.0/512.0) * cols) - 2.0;
				cY = ((4.0/512.0) * rows) - 2.0;
				if(rows == 256){
					System.out.println("(" + cols + ", " + rows + ") -> ("+cX + ", " + cY + ")");
				}
				int passes = 0;
				while(cX < 0.6 && cX > -2.15 && cY > -1.3 && cY < 1.3 && passes < _max){
					tmp = cX*cX - cY*cY + cX;
					cY = 2*cX*cY+cY;
					cX = tmp;
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
