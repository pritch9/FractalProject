package code.Fractals;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import code.UI.FractalViewer;
import code.UI.threading.FractalWorker;
import code.UI.threading.PointRunner;
import edu.buffalo.fractal.ComputePool;
import edu.buffalo.fractal.WorkerResult;

public abstract class Fractal {

	/**
	 * Bounds for calculating fractal
	 */
	private double[] bounds;

	/**
	 * Original bounds of Fractal. Copied on reset
	 */
	private double[] originalBounds;

	/**
	 * Original double dimensions
	 */
	private double fullWidth, fullHeight;

	/**
	 * Maximum escape time
	 */
	private int maxEscapeTime;

	/**
	 * Maximum escape distance
	 */
	private double maxEscapeDistance;

	/**
	 * Resolution used to generate fractal
	 */
	private Dimension resolution;

	/**
	 * Site to see. Click "Zoom In" when fully zoomed out to visit site.
	 */
	public double coolX, coolY;

	/**
	 * Name of the set (E.g "Mandelbrot Set)
	 */
	private String name;

	/**
	 * Center coordinate
	 */
	private double x, y;

	/**
	 * True if fractal should be displayed
	 */
	private boolean show = false;

	// ********************** //
	// **** CONSTRUCTORS **** //
	// ********************** //

	/**
	 * Constructor with default dimension ( 512x512 )
	 * 
	 * @param name
	 *            The name of the set
	 * @param x1
	 *            X bound
	 * @param x2
	 *            X bound
	 * @param y1
	 *            Y bound
	 * @param y2
	 *            Y bound
	 * @param display
	 *            True if fractal is to be displayed in FractalPanel
	 * 
	 */
	public Fractal(String name, double x1, double x2, double y1, double y2, boolean show) {
		this(name, new Dimension(512, 512), x1, x2, y1, y2, show);
	}

	/**
	 * Full contructor.
	 * 
	 * @param name
	 *            Name of the Set
	 * @param resolution
	 *            Resolution at which to be generated
	 * @param x1
	 *            X bound
	 * @param x2
	 *            X bound
	 * @param y1
	 *            Y bound
	 * @param y2
	 *            Y bound
	 * @param display
	 *            True if fractal is to be displayed in FractalPanel
	 */
	public Fractal(String name, Dimension resolution, double x1, double x2, double y1, double y2, boolean show) {
		this.name = name;
		this.resolution = resolution;
		this.setupDefaults(x1, x2, y1, y2);
		this.show = show;
	}

	// ************************************* //
	// **** FRACTAL CALCULATION METHODS **** //
	// ************************************* //

	/**
	 * Calculation method. Each fractal has a specific algorithm to calculate
	 * escape time.
	 * 
	 * @param xCalc
	 *            X coordinate
	 * @param yCalc
	 *            Y coordinate
	 * @return Escape time at (x,y)
	 */
	public abstract int calculate(double xCalc, double yCalc);

	/**
	 * Calculates and displays fractal using swing workers. All work done in the
	 * background. Non-blocking
	 */
	public void calculateAndShow() {
		if (!show) // do nothing if there is no GUI
			return;

		x = bounds[0] - (getWidth() / 2); // calculate the new center X
											// coordinate.
		y = bounds[2] - (getHeight() / 2); // calculate the new center Y
											// coordinate

		int threads = 128; // number of threads

		ComputePool pool = new ComputePool();
		pool.changePanel(FractalViewer.get().getFractalPanel());

		SwingWorker<WorkerResult, Void>[] instances = new FractalWorker[threads];

		int interval = resolution.height / threads; // number of rows to be
													// calculated

		for (int x = 0; x < threads; x++) {
			int rowStart = interval * x;
			instances[x] = new FractalWorker(Fractal.this, rowStart, interval); // create
																				// new
																				// SwingWorker
		}

		pool.generateFractal(resolution.height, instances); // generate fractal
	}

	/**
	 * Calculates, displays, and returns fractal array using point runners. All
	 * work done in background. <br />
	 * <br />
	 * <b>Warning:</b> Use only if blocking is necessary.
	 * 
	 * @return Fractal array
	 */
	public int[][] calculateAndBlock() {
		this.x = bounds[0] - (getWidth() / 2); // calculate the new center X
												// coordinate
		this.y = bounds[2] - (getHeight() / 2); // calculate the new center Y
												// coordinate

		int threads = 128; // number of threads

		ExecutorService e = Executors.newCachedThreadPool(); // thread pool
		List<PointRunner> list = new ArrayList<PointRunner>();
		for (int x = 0; x < threads; x++) {
			PointRunner uno = new PointRunner(this, resolution, threads, x); // create
																				// new
																				// point
																				// runner
																				// (like
																				// swing
																				// workers)
			e.execute(uno); // start point runner
			list.add(uno);
		}

		// Blocking
		e.shutdown();
		try {
			e.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		// end blocking

		// append all the int[][]'s
		int[][] one = list.get(0).getPoints(), two;
		for (int x = 1; x < list.size(); x++) {
			two = list.get(x).getPoints();
			one = this.append(one, two);
		}

		if (show) // Only show if there is a GUI
			FractalViewer.get().getFractalPanel().updateImage(one);

		return one; // final int[][]
	}

	// ****************************** //
	// **** FRACTAL ZOOM METHODS **** //
	// ****************************** //

	/**
	 * Used to zoom fractal into a specified rectangle
	 * 
	 * @param rectangle
	 *            Drawn by MouseListener
	 * @return 
	 */
	public boolean zoom(Rectangle rectangle) {
		// get the points from the rectangle

		int x1 = rectangle.x;
		int y1 = rectangle.y;

		int x2 = x1 + rectangle.width;
		int y2 = y1 + rectangle.height;
		
		// check if able to zoom in farther
		double minW = fullWidth * 1.0E-15;
		double minH = fullHeight * 1.0E-15;
		double xx1 = getX(x1);
		double xx2 = getX(x2);
		double yy1 = getY(y2);
		double yy2 = getY(y2);
		if(Math.abs(xx1 - xx2) < minW || Math.abs(yy1 - yy2) < minH){
			return false;
		}

		// set the new bounds
		bounds = getBounds(xx1, xx2, yy1, yy2);

		// show if GUI
		calculateAndShow();
		return true;
	}

	/**
	 * Zooms fractal out one step and displays fractal.<br />
	 * <br />
	 * <b>Warning:</b> this method is blocking
	 */
	public void zoomOut() {

		double w = getWidth(); // current width
		double h = getHeight(); // current height

		// update the center coordinates
		x = bounds[0] - (w / 2);
		y = bounds[2] - (h / 2);

		// calculate new width/height dimensions for next step
		w += w * .1;
		h += h * .1;

		// if full view, do not calculate, use original bounds
		if (fullView()) {
			w = fullWidth;
			h = fullHeight;
		}

		// set the new bounds
		bounds[0] = x + w / 2;
		bounds[1] = x - w / 2;
		bounds[2] = y + h / 2;
		bounds[3] = y - h / 2;

		// In order to recenter while zooming out, we recalculate the bounds to
		// keep from going outside original bounds
		for (int x = 0; x < 4; x++) {
			if (x % 2 == 0) { // bounds index 0, 2
				if (this.bounds[x] >= this.originalBounds[x]) {
					double change = this.bounds[0] - this.originalBounds[x];
					this.bounds[x + 1] -= change;
					this.bounds[x] = this.originalBounds[x];
				}
			} else { // bounds index 1, 3
				if (this.bounds[x] <= this.originalBounds[x]) {
					double change = this.originalBounds[x] - this.bounds[x];
					this.bounds[x - 1] += change;
					this.bounds[x] = this.originalBounds[x];
				}
			}
		}

		// if fully zoomed out, reset and use those bounds (guarantee)
		if (fullView())
			reset();

		// Recalculate and display with blocking so as to not overflow the
		// display
		this.calculateAndBlock();
	}

	/**
	 * Zooms fractal in one step and displays fractal.<br />
	 * <br />
	 * <b>Warning:</b> this method is blocking
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param move
	 *            True if motion to (x,y) is necessary
	 */
	public void zoomIn(double x, double y, boolean move) {
		double w = getWidth(); // current width
		double h = getHeight(); // current height

		// calculate next step
		w -= w * .05;
		h -= h * .05;

		// if using coolX and coolY, move to point
		if (move) {
			double xL = (x - this.x) * 0.1;
			double yL = (y - this.y) * 0.1;
			x = this.x + xL;
			y = this.y + yL;
		}

		// calculate new bounds
		bounds[0] = x + w / 2;
		bounds[1] = x - w / 2;
		bounds[2] = y + h / 2;
		bounds[3] = y - h / 2;

		// set the x and y
		this.x = x;
		this.y = y;

		// generate and display with blocking so as to not overflow display
		this.calculateAndBlock();
	}

	// *************************** //
	// **** FRACTAL UTILITIES **** //
	// *************************** //

	/**
	 * Used to append two 2D int arrays together
	 * 
	 * @param a
	 *            Array a
	 * @param b
	 *            Array b
	 * @return Appened arrays
	 */
	private int[][] append(int[][] a, int[][] b) {
		// a = [1][2][3][4][5][6][7]
		// b = [a][b][c][d][e][f][g]
		// 1: retVal = new [][][][][][][][][][][][][][]
		// 2: retVal = [1][2][3][4][5][6][7][][][][][][][]
		// 3: retVal = [1][2][3][4][5][6][7][a][b][c][d][e][f][g]
		// 4: return retVal
		int[][] retVal = new int[a.length + b.length][];
		System.arraycopy(a, 0, retVal, 0, a.length);
		System.arraycopy(b, 0, retVal, a.length, b.length);
		return retVal;
	}

	/**
	 * Tells whether or not the fractal is at full view (full zoom out)
	 * 
	 * @return True if fractal is fully zoomed out
	 */
	public boolean fullView() {
		return getHeight() >= fullHeight || getWidth() >= fullWidth;
	}

	/**
	 * Calculates bounds given two x and two y bounds. [upperX, lowerX, upperY,
	 * lowerY]
	 * 
	 * @param x1
	 *            X bound
	 * @param x2
	 *            X bound
	 * @param y1
	 *            Y bound
	 * @param y2
	 *            Y bound
	 * @return Array of bounds
	 */
	private double[] getBounds(double x1, double x2, double y1, double y2) {
		double[] retVal = new double[4];
		// [upperX, lowerX, upperY, lowerY]

		// order x and y bounds
		if (x1 < x2) {
			double t = x1;
			x1 = x2;
			x2 = t;
		}
		if (y1 < y2) {
			double t = y1;
			y1 = y2;
			y2 = t;
		}

		// set the bounds in array
		retVal[0] = x1;
		retVal[1] = x2;
		retVal[2] = y1;
		retVal[3] = y2;

		return retVal; // return array
	}

	/**
	 * Gets the column of a given X coordinate
	 * 
	 * @param x
	 *            X coordinate
	 * @return Column (-1 if invalid)
	 */
	public int getCol(double x) {
		if (x < bounds[1] || x > bounds[0]) {
			System.out.print("invalid x value!");
			return -1;
		}
		return (int) Math.ceil((x - bounds[1]) / ((bounds[0] - bounds[1]) / this.resolution.width));
	}

	/**
	 * Gets the row of a given Y coordinate
	 * 
	 * @param x
	 *            Y coordinate
	 * @return row (-1 if invalid)
	 */
	public int getRow(double y) {
		if (y < bounds[3] || x > bounds[2]) {
			System.out.print("invalid y value!");
			return -1;
		}
		return (int) Math.floor((y - bounds[3]) / ((bounds[2] - bounds[3]) / this.resolution.height));
	}

	/**
	 * Gets the original height of the fractal
	 * 
	 * @return height Height of fractal at full view
	 */
	public double getFullHeight() {
		return this.fullHeight;
	}

	/**
	 * Gets the original width of the fractal
	 * 
	 * @return width Width of fractal at full view
	 */
	public double getFullWidth() {
		return this.fullWidth;
	}

	/**
	 * Gets the current height of the fractal
	 * 
	 * @return height Current height
	 */
	public double getHeight() {
		return bounds[2] - bounds[3];
	}

	/**
	 * Gets the current width of the fractal
	 * 
	 * @return width Current width
	 */
	public double getWidth() {
		return bounds[0] - bounds[1];
	}

	/**
	 * Gets the double X coordinate for a given column
	 * 
	 * @param column
	 *            Column
	 * @return X coordinate
	 */
	public double getX(int column) {
		return ((bounds[0] - bounds[1]) / resolution.width) * column + bounds[1];
	}

	/**
	 * Gets the double Y coordinate for a given row
	 * 
	 * @param row
	 *            Row
	 * @return Y coordinate
	 */
	public double getY(int row) {
		return ((bounds[2] - bounds[3]) / resolution.width) * row + bounds[3];
	}

	/**
	 * Resets and displays fractal at original height/width
	 */
	public void reset() {
		this.bounds = Arrays.copyOf(originalBounds, 4); // set bounds to
														// original bounds
		calculateAndShow();
	}

	/**
	 * Run on initiation. Sets the default values for the fractal
	 * 
	 * - Max escape time: 255 - Max escape distance: 2.0
	 * 
	 * @param x1
	 *            X bound
	 * @param x2
	 *            X bound
	 * @param y1
	 *            Y bound
	 * @param y2
	 *            Y bound
	 */
	private void setupDefaults(double x1, double x2, double y1, double y2) {
		maxEscapeTime = 255; // default escape time
		maxEscapeDistance = 2.0; // default escape distance

		originalBounds = getBounds(x1, x2, y1, y2); // original bounds
		bounds = Arrays.copyOf(originalBounds, 4); // set bounds to original

		// calculate full width/height
		fullWidth = originalBounds[0] - originalBounds[1];
		fullHeight = originalBounds[2] - originalBounds[3];

		// calculate center coordinates
		x = bounds[0] - (fullWidth / 2);
		y = bounds[2] - (fullHeight / 2);
	}

	// ************************** //
	// **** ACCESSOR METHODS **** //
	// ************************** //

	/**
	 * Gets the current maximum escape distance
	 * 
	 * @return maxEscapeDistance maximum escape distance
	 */
	public double getMaxEscapeDistance() {
		return maxEscapeDistance;
	}

	/**
	 * Gets the current maximum escape time
	 * 
	 * @return maxEscapeTime maximum escape time
	 */
	public int getMaxEscapeTime() {
		return maxEscapeTime;
	}

	/**
	 * Gets the name of the fractal (E.g. "Mandelbrot Set")
	 * 
	 * @return name The name of the set
	 */
	public String getName() {
		return this.name + " Set";
	}

	/**
	 * Gets the current resolution of the fractal
	 * 
	 * @return resolution Dimension of resolution
	 */
	public Dimension getResolution() {
		return this.resolution;
	}

	// ************************* //
	// **** MUTATOR METHODS **** //
	// ************************* //

	/**
	 * Sets the maximum escape distance and updates the display
	 * 
	 * @param newEscapeDistance
	 *            New escape distance
	 */
	public void setMaxEscapeDistance(double newEscapeDistance) {
		this.maxEscapeDistance = newEscapeDistance;
		calculateAndShow(); // update if GUI
	}

	/**
	 * Sets the maximum escape time and updates the display
	 * 
	 * @param newEscapeTime
	 *            New escape time
	 */
	public void setMaxEscapeTime(int newEscapeTime) {
		this.maxEscapeTime = newEscapeTime;
		calculateAndShow(); // update if GUI
	}

	/**
	 * Sets the resolution and updates the display
	 * 
	 * @param newResolution
	 *            new resolution
	 */
	public void setResolution(Dimension newResolution) {
		this.resolution = newResolution;
		calculateAndShow(); // update if GUI
	}

}
