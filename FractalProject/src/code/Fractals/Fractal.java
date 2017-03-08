package code.Fractals;

public abstract class Fractal {

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
	private float _upperX, _lowerX, _upperY, _lowerY;

	private double _escapeDistance;

	/**
	 * Initialize with defaults: Max Passes: 255 Bounds: X: [-1.8,-1.7] Y:
	 * [-0.08,0.025]
	 * @param lowerX lower X Bound
	 * @param upperX upper X Bound
	 * @param lowerY lower Y Bound
	 * @param upperY upper Y Bound
	 */
	public Fractal(float lowerX, float upperX, float lowerY, float upperY) {
		this(512, 512, upperY, upperY, upperY, upperY);
	}

	/**
	 * Set rows and columns upon construction
	 * 
	 * @param rows amount of rows in points array
	 * @param cols amount of columns in points array
	 * @param lowerX lower X Bound
	 * @param upperX upper X Bound
	 * @param lowerY lower Y Bound
	 * @param upperY upper Y Bound
	 */
	public Fractal(int rows, int cols, float lowerX, float upperX, float lowerY, float upperY) {
		_max = 255; // Default max passes
		
		_escapeDistance = 2;

		// Defaults points dimensions to 512
		_rows = rows;
		_cols = cols;
		
		setBounds(lowerX, upperX, lowerY, upperY);
	}

	/**
	 * Set the max amount of passes
	 * @param max Maximum amount of passes
	 */
	public void setMax(int max) {
		_max = max;
	}

	/**
	 * Sets the bounds of the set
	 * @param lowerX lower X Bound
	 * @param upperX upper X Bound
	 * @param lowerY lower Y Bound
	 * @param upperY upper Y Bound
	 */
	public void setBounds(float upperX, float lowerX, float upperY, float lowerY) {
		// The next two if statements makes sure the upper bound is > the lower
		// bound and swaps them if needed
		if (upperX < lowerX) {
			float t = upperX;
			upperX = lowerX;
			lowerX = t;
		}
		if (upperY < lowerY) {
			float t = upperY;
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
	 * 
	 * @return int[][] points
	 */
	public int[][] getPoints() {
		int[][] points = new int[_cols][_rows]; // define the return array
												// [X][Y]

		float xCalc, yCalc; // define calculation local variables

		// The for loops iterate through the entire 2D array table
		for (int rows = 0; rows < _rows; rows++) { // y
			for (int cols = 0; cols < _cols; cols++) { // x

				// convert rows and cols to cartesian plot
				xCalc = getX(cols);
				yCalc = getY(rows);

				int passes = calculate(xCalc, yCalc);

				points[cols][rows] = passes; // When the while loop escapes, set
												// the passes in the points 2D
												// array
			}
		}
		return points; // returned filled points array
	}

	/**
	 * Custom calculation for a single fractal point
	 * @param xCalc
	 * @param yCalc
	 * @return number of passes
	 */
	public abstract int calculate(float xCalc, float yCalc);

	/**
	 * Returns the max amount of passes
	 * 
	 * @return max
	 */
	public int getMaxEscapes() {
		return _max;
	}
	
	/**
	 * Getter for the desired escape distance
	 * @return escape distance (default: 2.0)
	 */
	public double getEscapeDistance(){
		return _escapeDistance;
	}
	
	/**
	 * Sets the escape distance
	 * @param escapeDistance
	 */
	public void setEscapeDistance(float escapeDistance){
		_escapeDistance = escapeDistance;
	}

	/**
	 * Returns the X coordinate for a given column
	 * 
	 * @param col
	 * @return Cartesian X coordinate
	 */
	public float getX(int col) {
		return ((_upperX - _lowerX) / _cols) * col + _lowerX;
	}

	/**
	 * Returns the Y coordinate for a given rows
	 * 
	 * @param row
	 * @return Cartesian Y coordinate
	 */
	public float getY(int row) {
		return ((_upperY - _lowerY) / _rows) * row + _lowerY;
	}

	/**
	 * Returns the column for a given Cartesian x coordinate
	 * 
	 * @param x
	 *            coordinate
	 * @return column if x is contained by the bounds
	 */
	public int getCol(float x) {
		if(x < _lowerX || x > _upperX) {
			System.out.println("The float is out bounds! perhaps the bounds are incorrect?");
			return -1;
		}
		return  (int) Math.ceil(((x - _lowerX) * (_cols - 1)) / (_upperX - _lowerX));
	}

	/**
	 * Returns the row for a given Cartesian y coordinate
	 * 
	 * @param y
	 *            coordinate
	 * @return column if y is contained by the bounds (-1 if not)
	 */
	public int getRow(float y) {
		if(y < _lowerY || y > _upperY) {
			System.out.println("The float is out bounds! perhaps the bounds are incorrect?");
			return -1;
		}
		return (int) Math.ceil(((y - _lowerY) * (_rows - 1)) / (_upperY - _lowerY));
	}

}
