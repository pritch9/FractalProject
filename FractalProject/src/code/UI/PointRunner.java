package code.UI;

import code.Fractals.Fractal;

public class PointRunner implements Runnable {

	/**
	 * 2D-array of points being calculated by the thread
	 */
	private int[][] _points;

	/**
	 * Fractal reference
	 */
	private Fractal _fractal;

	/**
	 * number of columns accounted for
	 */
	private int cs;

	/**
	 * id number of the thread
	 */
	private int id;

	/**
	 * Main constructor
	 * 
	 * @param fractal
	 *            reference to the fractal for calculate() function
	 * @param cols
	 *            number of columns
	 * @param threads
	 *            number of concurrent threads being run
	 * @param id
	 *            id number of the thread for this instantiation
	 */
	public PointRunner(Fractal fractal, int cols, int threads, int id) {
		cs = cols / threads;
		this.id = id;
		_fractal = fractal;
		_points = new int[cs][_fractal.getNumRows()];
	}

	/**
	 * The generic run method of a Runnable. For PointRunner, this method will
	 * break down the section of points, and generate a 2D-array that will be
	 * merged with the other sections.
	 */
	@Override
	public void run() {
		double xCalc, yCalc; // deine calculation local variables
		for (int cols = 0; cols < cs; cols++) { // x
			for (int rows = 0; rows < _fractal.getNumRows(); rows++) { // y
				// convert rows and cols to cartesian plot
				xCalc = _fractal.getX(cols + (id * cs));
				yCalc = _fractal.getY(rows);

				int passes = _fractal.calculate(xCalc, yCalc);

				_points[cols][rows] = passes; // When the while loop
												// escapes, set
												// the passes in the points
												// 2D
												// array
			}
		}
	}

	/**
	 * Getter of the calculated points
	 * @return points the 2D array of escape times.
	 */
	public int[][] getPoints() {
		return this._points;
	}

}
