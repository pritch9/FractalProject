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
	 * Name of fractal set
	 */
	private String _name;

	/**
	 * Bounds
	 */
	private double _upperX, _lowerX, _upperY, _lowerY;

	private double _escapeDistance;

	/**
	 * Initialize with defaults: Max Passes: 255 Bounds: X: [-1.8,-1.7] Y:
	 * [-0.08,0.025]
	 * 
	 * @param lowerX
	 *            lower X Bound
	 * @param upperX
	 *            upper X Bound
	 * @param lowerY
	 *            lower Y Bound
	 * @param upperY
	 *            upper Y Bound
	 */
	public Fractal(String name, double lowerX, double upperX, double lowerY, double upperY) {
		this(name, 512, 512, upperX, lowerX, upperY, lowerY);
	}

	/**
	 * Set rows and columns upon construction
	 * 
	 * @param rows
	 *            amount of rows in points array
	 * @param cols
	 *            amount of columns in points array
	 * @param lowerX
	 *            lower X Bound
	 * @param upperX
	 *            upper X Bound
	 * @param lowerY
	 *            lower Y Bound
	 * @param upperY
	 *            upper Y Bound
	 */
	public Fractal(String name, int rows, int cols, double lowerX, double upperX, double lowerY, double upperY) {
		_name = name + " Set";
		
		_max = 255; // Default max passes

		_escapeDistance = 2;

		// Defaults points dimensions to 512
		_rows = rows;
		_cols = cols;

		setBounds(lowerX, upperX, lowerY, upperY);
	}

	/**
	 * Set the max amount of passes
	 * 
	 * @param max
	 *            Maximum amount of passes
	 */
	public void setMax(int max) {
		_max = max;
	}

	/**
	 * Sets the bounds of the set
	 * 
	 * @param lowerX
	 *            lower X Bound
	 * @param upperX
	 *            upper X Bound
	 * @param lowerY
	 *            lower Y Bound
	 * @param upperY
	 *            upper Y Bound
	 */
	public void setBounds(double upperX, double lowerX, double upperY, double lowerY) {
		// The next two i statements makes sure the upper bound is > the lower
		// bound and swaps them i needed
		if (upperX < lowerX) {
			double t = upperX;
			upperX = lowerX;
			lowerX = t;
		}
		if (upperY < lowerY) {
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
	 * 
	 * @return int[][] points
	 */
	public int[][] getPoints() {
		int[][] points = new int[_cols][_rows]; // deine the return array
												// [X][Y]

		double xCalc, yCalc; // deine calculation local variables

		// The or loops iterate through the entire 2D array table
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
		return points; // returned illed points array
	}

	/**
	 * Custom calculation or a single fractal point
	 * 
	 * @param xCalc
	 * @param yCalc
	 * @return number of passes
	 */
	public abstract int calculate(double xCalc, double yCalc);

	/**
	 * Returns the max amount of passes
	 * 
	 * @return max
	 */
	public int getMaxEscapes() {
		return _max;
	}

	/**
	 * Getter or the desired escape distance
	 * 
	 * @return escape distance (default: 2.0)
	 */
	public double getEscapeDistance() {
		return _escapeDistance;
	}

	/**
	 * Sets the escape distance
	 * 
	 * @param escapeDistance
	 */
	public void setEscapeDistance(double escapeDistance) {
		_escapeDistance = escapeDistance;
	}

	/**
	 * Returns the X coordinate or a given column
	 * 
	 * @param col
	 * @return Cartesian X coordinate
	 */
	public double getX(int col) {
		return ((_upperX - _lowerX) / _cols) * col + _lowerX;
	}

	/**
	 * Returns the Y coordinate or a given rows
	 * 
	 * @param row
	 * @return Cartesian Y coordinate
	 */
	public double getY(int row) {
		return ((_upperY - _lowerY) / _rows) * row + _lowerY;
	}

	/**
	 * Returns the column or a given Cartesian x coordinate
	 * 
	 * @param x
	 *            coordinate
	 * @return column i x is contained by the bounds
	 */
	public int getCol(double x) {
		if (x < _lowerX || x > _upperX) {
			System.out.println("The double (" + x + ") is out bounds for X! perhaps the bounds are incorrect?");
			return -1;
		}
		return (int) Math.floor((x - _lowerX) / ((_upperX - _lowerX) / this._cols));
	}

	/**
	 * Returns the row or a given Cartesian y coordinate
	 * 
	 * @param y
	 *            coordinate
	 * @return column i y is contained by the bounds (-1 i not)
	 */
	public int getRow(double y) {
		if (y < _lowerY || y > _upperY) {
			System.out.println("The double (" + y + ") is out bounds for Y! perhaps the bounds are incorrect?");
			return -1;
		}
		return (int) Math.ceil((y - _lowerY) / ((_upperY - _lowerY) / this._rows));
	}
	
	/**
	 * Gets the name of the set
	 * @return name example: "Mandelbrot"
	 */
	public String getName(){
		return this._name;
	}
	
	public int getNumRows(){
		return this._rows;
	}
	
	public int getNumCols(){
		return this._cols;
	}
}
