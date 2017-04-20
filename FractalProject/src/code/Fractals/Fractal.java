package code.Fractals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import code.UI.FractalViewer;
import code.UI.PointRunner;

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
	 * Point of interest :)
	 */
	public double coolX, coolY;

	/**
	 * 2D array storing escape times
	 */
	private int[][] _points;

	/**
	 * Name of fractal set
	 */
	private String _name;

	/**
	 * Bounds
	 */
	private double _upperX, _lowerX, _upperY, _lowerY;

	/**
	 * Stores the current center point
	 */
	private double x, y;

	/**
	 * true if default bounds are set
	 */
	private boolean DEFAULTS_SET = false;

	/**
	 * Default bounds
	 */
	private double ORIGINAL_LOWER_X, ORIGINAL_LOWER_Y, ORIGINAL_UPPER_X, ORIGINAL_UPPER_Y;

	/**
	 * Escape distance for calculating the Fractal
	 */
	private double _escapeDistance;

	/**
	 * Initialize with defaults: Max Passes: 255 Bounds: X: [-1.8,-1.7] Y:
	 * [-0.08,0.025]
	 * 
	 * @param name
	 *            The name of the fractal set (E.g. "Mandelbrot")
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
	 * @param name
	 *            The name of the fractal set (E.g. "Mandelbrot")
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

		calculatePoints();
	}

	/**
	 * Set the max amount of passes
	 * 
	 * @param max
	 *            Maximum amount of passes
	 * @param calc
	 *            true if you want to recalculate points
	 */
	public void setMax(int max, boolean calc) {
		_max = max;
		if (calc)
			this.calculatePoints();
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

		if (!DEFAULTS_SET) {
			this.ORIGINAL_UPPER_X = _upperX;
			this.ORIGINAL_LOWER_X = _lowerX;
			this.ORIGINAL_UPPER_Y = _upperY;
			this.ORIGINAL_LOWER_Y = _lowerY;
			DEFAULTS_SET = true;
		}
	}

	/**
	 * Returns the 2-D array of points
	 * 
	 * @return points the 2D-array of escape times
	 */
	public int[][] getPoints() {
		return _points;
	}

	/**
	 * Call to calculate points.
	 */
	private void calculatePoints() {
		this.x = _upperX - (getWidth() / 2);
		this.y = _upperY - (getHeight() / 2);

		_points = new int[_cols][_rows]; // deine the return array// [X][Y]

		// The or loops iterate through the entire 2D array table
		int threads = Runtime.getRuntime().availableProcessors();

		ExecutorService e = Executors.newFixedThreadPool(threads);
		List<PointRunner> list = new ArrayList<PointRunner>();
		for (int x = 0; x < threads; x++) {
			PointRunner uno = new PointRunner(this, _cols, threads, x);
			e.execute(uno);
			list.add(uno);
		}

		e.shutdown();
		try {
			e.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		int[][] one = list.get(0).getPoints(), two;
		for (int x = 1; x < list.size(); x++) {
			two = list.get(x).getPoints();
			one = this.append(one, two);
		}
		_points = one;
	}

	/**
	 * Custom calculation or a single fractal point
	 * 
	 * @param xCalc
	 *            double x value of column
	 * @param yCalc
	 *            double y value of row
	 * @return the escape time of a point (xCalc, yCalc)
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
	 *            the new escape distance (0, infinity)
	 */
	public void setEscapeDistance(double escapeDistance) {
		_escapeDistance = escapeDistance;
		calculatePoints();
	}

	/**
	 * Returns the X coordinate or a given column
	 * 
	 * @param col
	 *            column number
	 * @return Cartesian X coordinate
	 */
	public double getX(int col) {
		return ((_upperX - _lowerX) / _cols) * col + _lowerX;
	}

	/**
	 * Returns the Y coordinate or a given rows
	 * 
	 * @param row
	 *            row number
	 * @return Cartesian Y coordinate
	 */
	public double getY(int row) {
		return ((_upperY - _lowerY) / _rows) * row + _lowerY;
	}

	/**
	 * Returns the column or a given Cartesian x coordinate
	 * 
	 * @param x
	 *            X coordinate
	 * @return column if x is contained by the bounds
	 */
	public int getCol(double x) {
		if (x < _lowerX || x > _upperX) {
			System.out.println("The double (" + x + ") is out bounds for X! perhaps the bounds are incorrect?");
			return -1;
		}
		return (int) Math.ceil((x - _lowerX) / ((_upperX - _lowerX) / this._cols));
	}

	/**
	 * Returns the row or a given Cartesian y coordinate
	 * 
	 * @param y
	 *            Y coordinate
	 * @return column if y is contained by the bounds (-1 i not)
	 */
	public int getRow(double y) {
		if (y < _lowerY || y > _upperY) {
			System.out.println("The double (" + y + ") is out bounds for Y! perhaps the bounds are incorrect?");
			return -1;
		}
		return (int) Math.floor((y - _lowerY) / ((_upperY - _lowerY) / this._rows));
	}

	/**
	 * Gets the name of the set
	 * 
	 * @return the name of the set (E.g. "Mandelbrot")
	 */
	public String getName() {
		return this._name;
	}

	/**
	 * Getter of the number of rows
	 * 
	 * @return the number of rows of the fractal
	 */
	public int getNumRows() {
		return this._rows;
	}

	/**
	 * Getter of the number of columns
	 * 
	 * @return the number of columns of the fractal
	 */
	public int getNumCols() {
		return this._cols;
	}

	/**
	 * Getter of the escape time of a specific point in the set
	 * 
	 * @param col
	 *            column number
	 * @param row
	 *            row number
	 * @return the escape time
	 */
	public int getEscapeTime(int col, int row) {
		return _points[col][row];
	}

	/**
	 * Getter of the escape time of a specific point in the set
	 * 
	 * @param x
	 *            The Cartesian X coordinate
	 * @param y
	 *            The Cartesian Y coordinate
	 * @return the escape time
	 */
	public int getEscapeTime(double x, double y) {
		return getEscapeTime(getCol(x), getRow(y));
	}

	public void zoom(Point p1, Point p2) {
		setBounds(getX(p1.x), getX(p2.x), getY(p1.y), getY(p2.y));

		this.calculatePoints();
	}

	/**
	 * Getter of the current center X coordinate
	 * 
	 * @return the current center X coordinate
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Getter of the current center Y coordinate
	 * 
	 * @return the current center Y coordinate
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Getter of the current width
	 * 
	 * @return the current width of the fractal bounds
	 */
	public double getWidth() {
		return _upperX - _lowerX;
	}

	/**
	 * Getter of the current height
	 * 
	 * @return the current height of the fractal bounds
	 */
	public double getHeight() {
		return _upperY - _lowerY;
	}

	/**
	 * Resets the fractal to it's former glory
	 */
	public void reset() {
		this._lowerX = this.ORIGINAL_LOWER_X;
		this._upperX = this.ORIGINAL_UPPER_X;
		this._lowerY = this.ORIGINAL_LOWER_Y;
		this._upperY = this.ORIGINAL_UPPER_Y;
		this.calculatePoints();
	}

	/**
	 * Tells whether or not the fractal is completely zoomed out
	 * 
	 * @return true if fractal is at original size
	 */
	public boolean fullView() {
		return getWidth() >= getFullWidth() && getHeight() >= getFullHeight();
	}

	/**
	 * Getter of the original width of the fractal
	 * 
	 * @return the original width of the fractal
	 */
	public double getFullWidth() {
		return this.ORIGINAL_UPPER_X - this.ORIGINAL_LOWER_X;
	}

	/**
	 * Getter of the original height of the fractal
	 * 
	 * @return the original height of the fractal
	 */
	public double getFullHeight() {
		return this.ORIGINAL_UPPER_Y - this.ORIGINAL_LOWER_Y;
	}

	/**
	 * @author http://stackoverflow.com/users/100565/mebigfatguy
	 * @param a
	 *            array #1
	 * @param b
	 *            array #2
	 * @return array a appended to array b
	 */
	public int[][] append(int[][] a, int[][] b) {
		int[][] result = new int[a.length + b.length][];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	/**
	 * Calculates the 2D-Array of Escape Times for the next zoomed out portion
	 * of the fractal animation
	 * 
	 * @return the 2D-Array of escape times
	 */
	public int[][] getNextZoomOut() {
		double w = getWidth();
		double h = getHeight();

		x = _upperX - (w / 2);
		y = _upperY - (h / 2);

		w += w * .1;
		h += h * .1;

		if (fullView()) {
			w = getFullWidth();
			h = getFullHeight();
		}

		_lowerX = x - w / 2;
		_upperX = x + w / 2;
		_lowerY = y - h / 2;
		_upperY = y + h / 2;

		/*
		 * In order to recenter while zooming out, the center of the zoom must
		 * be shifted. This can be done more efficiently if we thought about it
		 * but right now it works.
		 * 
		 */
		if (this._upperX >= this.ORIGINAL_UPPER_X) {
			double change = this._upperX - this.ORIGINAL_UPPER_X;
			this._lowerX -= change;
			this._upperX = this.ORIGINAL_UPPER_X;
		}

		if (this._lowerX <= this.ORIGINAL_LOWER_X) {
			double change = this.ORIGINAL_LOWER_X - this._lowerX;
			this._upperX += change;
			this._lowerX = this.ORIGINAL_LOWER_X;
		}

		if (this._upperY >= this.ORIGINAL_UPPER_Y) {
			double change = this._upperY - this.ORIGINAL_UPPER_Y;
			this._lowerY -= change;
			this._upperY = this.ORIGINAL_UPPER_Y;
		}

		if (this._lowerY <= this.ORIGINAL_LOWER_Y) {
			double change = this.ORIGINAL_LOWER_Y - this._lowerY;
			this._upperY += change;
			this._lowerY = this.ORIGINAL_LOWER_Y;
		}
		
		if(fullView()) reset();

		this.calculatePoints();
		return this._points;
	}

	/**
	 * Calculates the 2D-Array of Escape Times for the next zoomed in portion of
	 * the fractal animation
	 * 
	 * @param x
	 *            center X coordinate
	 * @param y
	 *            center X coordinate
	 * @param move
	 *            Specifies whether or not to translate to point.
	 * @return the 2D-Array of escape times
	 */
	public int[][] getNextZoomIn(double x, double y, boolean move) {
		double w = getWidth();
		double h = getHeight();

		w -= w * .05;
		h -= h * .05;

		if (move) {
			double xL = (x - this.getX()) * 0.1;
			double yL = (y - this.getY()) * 0.1;
			x = this.getX() + xL;
			y = this.getY() + yL;
		}

		_lowerX = x - w / 2;
		_upperX = x + w / 2;
		_lowerY = y - h / 2;
		_upperY = y + h / 2;

		this.x = x;
		this.y = y;
		this.calculatePoints();
		return this._points;
	}

}
