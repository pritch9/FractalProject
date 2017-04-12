package code.Fractals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
	
	private double x, y;
	
	private double centerX, centerY;

	private double _upperXO, _lowerXO, _upperYO, _lowerYO;
	
	/**
	 * Escape distance for calculating the Fractal
	 */
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

		calculatePoints();
	}

	/**
	 * Set the max amount of passes
	 * 
	 * @param max
	 *            Maximum amount of passes
	 */
	public void setMax(int max) {
		_max = max;
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

		if (_upperXO == 0) {
			_upperXO = _upperX;
			_lowerXO = _lowerX;
			_upperYO = _upperY;
			_lowerYO = _lowerY;
			centerX = _upperXO - (_upperXO-_lowerXO)/2;
			centerY = _upperYO - (_upperYO-_lowerYO)/2;
		}
	}

	/**
	 * Returns the 2-D array of points
	 * 
	 * @return int[][] points
	 */
	public int[][] getPoints() {
		return _points;
	}

	/**
	 * Call to calculate points.
	 */
	private void calculatePoints() {
		this.x = _upperX - ((_upperX-_lowerX)/2);
		this.y = _upperY - ((_upperY-_lowerY)/2);
		_points = new int[_cols][_rows]; // deine the return array// [X][Y]
		
		// The or loops iterate through the entire 2D array table
		int threads = 128;
		ExecutorService e = Executors.newFixedThreadPool(threads);
		List<PointRunner> list = new ArrayList<PointRunner>();
		for(int x = 0; x < threads; x++){
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
		for(int x = 1; x < list.size(); x++){
			two = list.get(x).getPoints();
			one = this.append(one, two); 
		}
		_points = one;
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
		calculatePoints();
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
		return (int) Math.ceil((x - _lowerX) / ((_upperX - _lowerX) / this._cols));
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
		return (int) Math.floor((y - _lowerY) / ((_upperY - _lowerY) / this._rows));
	}

	/**
	 * Gets the name of the set
	 * 
	 * @return name example: "Mandelbrot"
	 */
	public String getName() {
		return this._name;
	}

	public int getNumRows() {
		return this._rows;
	}

	public int getNumCols() {
		return this._cols;
	}

	public int getEscapeTime(int col, int row) {
		return _points[col][row];
	}

	public int getEscapeTime(double x, double y) {
		return getEscapeTime(getCol(x), getRow(y));
	}

	public void zoom(Point p1, Point p2) {
		setBounds(getX(p1.x), getX(p2.x), getY(p1.y), getY(p2.y));

		this.calculatePoints();
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	
	public double getWidth(){
		return _upperX-_lowerX;
	}
	
	public double getHeight(){
		return _upperY-_lowerY;
	}

	public int[][] getNextZoomIn(double x, double y) {
		double w = _upperX - _lowerX;
		double h = _upperY - _lowerY;

		w -= w * .05;
		h -= h * .05;

		_lowerX = x - w / 2;
		_upperX = x + w / 2;
		_lowerY = y - h / 2;
		_upperY = y + h / 2;
		this.x = x;
		this.y = y;
		this.calculatePoints();
		return this._points;
	}

	public void reset() {
		this._lowerX = _lowerXO;
		this._upperX = _upperXO;
		this._lowerY = _lowerYO;
		this._upperY = _upperYO;
		this.calculatePoints();
	}


	public boolean fullView() {
		return getWidth() >= (_upperXO - _lowerXO) && getHeight() >= (_upperYO - _lowerYO);
	}
	
	/**
	 * @author http://stackoverflow.com/users/100565/mebigfatguy
	 * @param a
	 * @param b
	 * @return
	 */
	public int[][] append(int[][] a, int[][] b) {
        int[][] result = new int[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

	public int[][] getNextZoomOut() {
		double w = _upperX - _lowerX;
		double h = _upperY - _lowerY;
		
		x = _upperX - (w/2);
		y = _upperY - (h/2);
		
		w += w * .1;
		h += h * .1;
		
		if(w >= (_upperXO - _lowerXO)) w = (_upperXO - _lowerXO);
		if(h >= (_upperYO - _lowerYO)) h = (_upperYO - _lowerYO);
		
		_lowerX = x - w / 2;
		_upperX = x + w / 2;
		_lowerY = y - h / 2;
		_upperY = y + h / 2;
		
		if(_upperX >= _upperXO){
			double change = _upperX-_upperXO;
			_lowerX -= change;
			_upperX = _upperXO;
		}
		
		if(_lowerX <= _lowerXO){
			double change = _lowerXO - _lowerX;
			_upperX += change;
			_lowerX = _lowerXO;
		}
		
		if(_upperY >= _upperYO){
			double change = _upperY-_upperYO;
			_lowerY -= change;
			_upperY = _upperYO;
		}
		
		if(_lowerY <= _lowerYO){
			double change = _lowerYO - _lowerY;
			_upperY += change;
			_lowerY = _lowerYO;
		}
		
		this.calculatePoints();
		return this._points;
	}

	public double getCenterX(){
		return this.centerX;
	}
	
	public double getCenterY() {
		return this.centerY;
	}

	public void shift(double xDist, double yDist) {
		this.x += xDist;
		this.y += yDist;
	}

}
