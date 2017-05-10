package code.UI.threading;

import javax.swing.SwingWorker;

import code.Fractals.Fractal;
import edu.buffalo.fractal.WorkerResult;

public class FractalWorker extends SwingWorker<WorkerResult, Void>{
	
	private Fractal fractal;
	private int startRow;
	private int numberOfRows;
	
	/**
	 * Constructor
	 * @param fractal Fractal being generated
	 * @param startRow Number of the row started on
	 * @param numberOfRows Number of rows being calculated
	 */
	public FractalWorker(Fractal fractal, int startRow, int numberOfRows){
		this.fractal = fractal;
		this.startRow = startRow;
		this.numberOfRows = numberOfRows;
	}

	@Override
	protected WorkerResult doInBackground() throws Exception {
		int[][] retVal = new int[numberOfRows][fractal.getResolution().width]; // return array
		double xCalc, yCalc; // deine calculation local variables
		for (int row = 0; row < numberOfRows; row++) { // x
			for (int col = 0; col < fractal.getResolution().width; col++) { // y
				// convert rows and cols to cartesian plot
				xCalc = fractal.getX(startRow + row);
				yCalc = fractal.getY(col);

				int passes = fractal.calculate(xCalc, yCalc);
				
				retVal[row][col] = passes; // When the while loop
												// escapes, set
												// the passes in the points
												// 2D
												// array
			}
		}
		return new WorkerResult(startRow, retVal);
	}

}
