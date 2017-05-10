package code.UI.threading;

import java.awt.Dimension;

import code.Fractals.Fractal;

public class PointRunner implements Runnable {

	/**
	 * 2D-array of points being calculated by the thread
	 */
	private int[][] points;

	/**
	 * Fractal reference
	 */
	private Fractal fractal;

	/**
	 * number of columns accounted for
	 */
	private int cs;
	private int cols;

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
	public PointRunner(Fractal fractal, Dimension resolution, int threads, int id) {
		cs = resolution.height / threads;
		this.id = id;
		this.fractal = fractal;
		this.cols = resolution.width;
		points = new int[cs][cols];
	}

	/**
	 * The generic run method of a Runnable. For PointRunner, this method will
	 * break down the section of points, and generate a 2D-array that will be
	 * merged with the other sections.
	 */
	@Override
	public void run() {
		double xCalc, yCalc; // deine calculation local variables
		for (int row = 0; row < cs; row++) { // y
			for (int col = 0; col < cols; col++) { // x
				// convert rows and cols to cartesian plot
				xCalc = fractal.getX((cs * id) + row);
				yCalc = fractal.getY(col);
				
				int passes = fractal.calculate(xCalc, yCalc);

				points[row][col] = passes; // When the while loop
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
		return this.points;
	}

}
