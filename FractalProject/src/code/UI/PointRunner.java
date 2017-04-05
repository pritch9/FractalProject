package code.UI;

import code.Fractals.Fractal;

public class PointRunner implements Runnable{

	private int[][] _points;
	private Fractal _fractal;
	private int cs,id;
	
	public PointRunner(Fractal fractal, int cols, int threads, int id){
		cs = cols/threads;
		this.id = id;
		_fractal = fractal;
		_points = new int[cs][_fractal.getNumRows()];
	}
	
	@Override
	public void run() {
		double xCalc, yCalc; // deine calculation local variables
		for (int cols = 0; cols < cs; cols++) { // x
			for (int rows = 0; rows < _fractal.getNumRows(); rows++) { // y
				// convert rows and cols to cartesian plot
				xCalc = _fractal.getX(cols+(id*cs));
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
	
	public int[][] getPoints(){
		return this._points;
	}

}
