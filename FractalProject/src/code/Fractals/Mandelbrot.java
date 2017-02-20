package code.Fractals;

public class Mandelbrot {
	
	/**
	 * Max amount of passes
	 */
	private int _max;
	
	/**
	 * Dimensions of points
	 */
	private int _rows, _cols;
	
	/**
	 * Bounds
	 */
	private double _upperX,_lowerX, _upperY, _lowerY;
	
	/**
	 * Initialize with defaults:
	 * Max Passes: 255
	 * Bounds: 	X: [-2.15,0.6]
	 * 			Y: [-1.3,1.3]
	 */
	public Mandelbrot(){
		this(512, 512);
	}
	
	/**
	 * Set rows and columns upon construction
	 * 
	 * @param rows amount of rows in points array
	 * @param cols amount of columns in points array
	 */
	public Mandelbrot(int rows, int cols){
		_max = 255; // Default max passes
		
		// Defaults points dimensions to 512
		_rows = rows;
		_cols = cols;
		
		setBounds(0.6,-2.15,1.3,-1.3);
	}
	
	/**
	 * Set the max amount of passes
	 */
	public void setMax(int max){
		_max = max;
	}
	
	/**
	 * Sets the bounds of the set
	 */
	public void setBounds(double upperX, double lowerX, double upperY, double lowerY){
		// The next two if statements makes sure the upper bound is > the lower bound and swaps them if needed
		if(upperX < lowerX){
			double t = upperX;
			upperX = lowerX;
			lowerX = t;
		}
		if(upperY < lowerY){
			double t = upperY;
			upperY = lowerY;
			lowerY = t;
		}
		
		// Sets all the instance variables
		_upperX = upperX;
		_lowerX = lowerX;
		_upperY = upperY;
		_lowerY = lowerY;
	}

	/**
	 * Returns the 2-D array of points
	 * @return int[][] points
	 */
	public int[][] getPoints() {
		int[][] points = new int[_cols][_rows]; // define the return array [X][Y]
		
		double xCalc, yCalc, tX, tY, tmp, dist; // define calculation local variables
		
		// The for loops iterate through the entire 2D array table
		for(int rows = 0; rows < _rows; rows++){ // y
			for(int cols = 0; cols < _cols; cols++){ // x
				
				// convert rows and cols to cartesian plot
				xCalc = getX(cols);
				yCalc = getY(rows);
	
			
				tX = xCalc;
				tY = yCalc;
				
				
				int passes = 0; // Start passes at -1 because it passes once by default
				
				dist = Math.sqrt(xCalc*xCalc + yCalc*yCalc); // (0,0) has a distance of 0 from (0,0)
				
				while(dist < 2 && passes < _max){
					tmp = tX*tX - tY*tY + xCalc; // TEMP X: x' = x^2 - y^2 + (original x value)
					tY = 2.0 * tX * tY + yCalc; // y' = 2xy + (original y value)
					tX = tmp; // set X to TEMP X
					dist = Math.sqrt(tX*tX + tY*tY); // recalculate distance
					passes++; // add a pass
				}
				points[cols][rows] = passes; // When the while loop escapes, set the passes in the points 2D array
			}
		}
		return points;  // returned filled points array
	}

	/**\
	 * Returns the max amount of passes
	 * @return max
	 */
	public int getMaxEscapes() {
		return _max;
	}

	/**
	 * Returns the X coordinate for a given column
	 * @param col
	 * @return Cartesian X coordinate
	 */
	public double getX(int col){
		return ((_upperX - _lowerX) / _cols) * col + _lowerX;
	}
	
	/**
	 * Returns the Y coordinate for a given rows
	 * @param row
	 * @return Cartesian Y coordinate
	 */
	public double getY(int row) {
		return ((_upperY - _lowerY) / _rows) * row + _lowerY;
	}
	
	/**
	 * Returns the column for a given Cartesian x coordinate
	 * @param x coordinate
	 * @return column if x is contained by the bounds
	 */
	public int getCol(double x){
		if(x < _lowerX || x > _upperX) return -1;
		return (int)Math.floor(((x - _lowerX)*(_cols-1)) / (_upperX - _lowerX));
	}

	/**
	 * Returns the row for a given Cartesian y coordinate
	 * @param y coordinate
	 * @return column if y is contained by the bounds (-1 if not)
	 */
	public int getRow(double y) {
		if(y < _lowerY || y > _upperY) return -1;
		return (int) Math.floor(((y - _lowerY)* (_rows-1)) / (_upperY - _lowerY));
	}


}
