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

	private double[] bounds, originalBounds;
	private double fullWidth, fullHeight;

	private int maxEscapeTime;
	private double maxEscapeDistance;

	private Dimension resolution;

	public double coolX, coolY;

	private String name;

	private double x, y;
	
	private boolean show = false;

	// **********************//
	// **** CONSTRUCTORS ****//
	// **********************//

	public Fractal(String name, double x1, double x2, double y1, double y2, boolean show) {
		this(name, new Dimension(512, 512), x1, x2, y1, y2, show);
	}

	public Fractal(String name, Dimension resolution, double x1, double x2, double y1, double y2, boolean show) {
		this.name = name;
		this.resolution = resolution;
		this.setupDefaults(x1, x2, y1, y2);
		this.show = show;
	}

	// *************************************//
	// **** FRACTAL CALCULATION METHODS ****//
	// *************************************//

	public abstract int calculate(double xCalc, double yCalc);

	public void calculateAndShow() {
		if(!show) return;
		x = bounds[0] - (getWidth() / 2);
		y = bounds[2] - (getHeight() / 2);

		int threads = 128;

		ComputePool pool = new ComputePool();
		pool.changePanel(FractalViewer.get().getFractalPanel());
		SwingWorker<WorkerResult, Void>[] instances = new FractalWorker[threads];

		int interval = resolution.height / threads;

		for (int x = 0; x < threads; x++) {
			int rowStart = interval * x;
			instances[x] = new FractalWorker(Fractal.this, rowStart, interval);
		}

		pool.generateFractal(resolution.height, instances);
	}

	public int[][] calculateAndBlock() {
		this.x = bounds[0] - (getWidth() / 2);
		this.y = bounds[2] - (getHeight() / 2);

		// The or loops iterate through the entire 2D array table
		int threads = 128;

		ExecutorService e = Executors.newCachedThreadPool();
		List<PointRunner> list = new ArrayList<PointRunner>();
		for (int x = 0; x < threads; x++) {
			PointRunner uno = new PointRunner(this, resolution, threads, x);
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
		if(show)FractalViewer.get().getFractalPanel().updateImage(one);
		return one;
	}

	// ******************************//
	// **** FRACTAL ZOOM METHODS ****//
	// ******************************//

	public void zoom(Rectangle rectangle) {
		int x1 = rectangle.x;
		int y1 = rectangle.y;

		int x2 = x1 + rectangle.width;
		int y2 = y1 + rectangle.height;

		bounds = getBounds(getX(x1), getX(x2), getY(y1), getY(y2));

		calculateAndShow();
	}

	public void zoomOut() {

		double w = getWidth();
		double h = getHeight();

		x = bounds[0] - (w / 2);
		y = bounds[2] - (h / 2);

		w += w * .1;
		h += h * .1;

		if (fullView()) {
			w = fullWidth;
			h = fullHeight;
		}

		bounds[0] = x + w / 2;
		bounds[1] = x - w / 2;
		bounds[2] = y + h / 2;
		bounds[3] = y - h / 2;

		/*
		 * In order to recenter while zooming out, the center of the zoom must
		 * be shifted. This can be done more efficiently if we thought about it
		 * but right now it works.
		 * 
		 */

		for (int x = 0; x < 4; x++) {
			if (x % 2 == 0) {
				if (this.bounds[x] >= this.originalBounds[x]) {
					double change = this.bounds[0] - this.originalBounds[x];
					this.bounds[x + 1] -= change;
					this.bounds[x] = this.originalBounds[x];
				}
			} else {
				if (this.bounds[x] <= this.originalBounds[x]) {
					double change = this.originalBounds[x] - this.bounds[x];
					this.bounds[x-1] += change;
					this.bounds[x] = this.originalBounds[x];
				}
			}
		}

		if (fullView())
			reset();

		this.calculateAndBlock();
	}

	public void zoomIn(double x, double y, boolean move){
		double w = getWidth();
		double h = getHeight();

		w -= w * .05;
		h -= h * .05;

		if (move) {
			double xL = (x - this.x) * 0.1;
			double yL = (y - this.y) * 0.1;
			x = this.x + xL;
			y = this.y + yL;
		}

		bounds[0] = x + w / 2;
		bounds[1] = x - w / 2;
		bounds[2] = y + h / 2;
		bounds[3] = y - h / 2;

		this.x = x;
		this.y = y;
		
		this.calculateAndBlock();
	}
	
	// ***************************//
	// **** FRACTAL UTILITIES ****//
	// ***************************//

	private int[][] append(int[][] a, int[][] b) {
		int[][] result = new int[a.length + b.length][];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	public boolean fullView() {
		return getHeight() >= fullHeight || getWidth() >= fullWidth;
	}

	private double[] getBounds(double x1, double x2, double y1, double y2) {
		double[] retVal = new double[4];
		// [upperX, lowerX, upperY, lowerY]
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

		retVal[0] = x1;
		retVal[1] = x2;
		retVal[2] = y1;
		retVal[3] = y2;

		return retVal;
	}

	public int getCol(double x){
		if(x < bounds[1] || x > bounds[0]){
			System.out.print("invalid x value!");
			return -1;
		}
		return (int) Math.ceil((x - bounds[1]) / ((bounds[0] - bounds[1]) / this.resolution.width));
	}
	
	public int getRow(double y){
		if(y < bounds[3] || x > bounds[2]){
			System.out.print("invalid y value!");
			return -1;
		}
		return (int) Math.floor((y - bounds[3]) / ((bounds[2] - bounds[3]) / this.resolution.height));
	}
	
	public double getFullHeight(){
		return this.fullHeight;
	}
	
	public double getFullWidth(){
		return this.fullWidth;
	}
	
	public double getHeight() {
		return bounds[2] - bounds[3];
	}

	public double getWidth() {
		return bounds[0] - bounds[1];
	}

	public double getX(int column) {
		return ((bounds[0] - bounds[1]) / resolution.width) * column + bounds[1];
	}

	public double getY(int row) {
		return ((bounds[2] - bounds[3]) / resolution.width) * row + bounds[3];
	}

	public void reset() {
		this.bounds = Arrays.copyOf(originalBounds, 4);
		calculateAndShow();
	}

	private void setupDefaults(double x1, double x2, double y1, double y2) {
		maxEscapeTime = 255;
		maxEscapeDistance = 2.0;

		originalBounds = getBounds(x1, x2, y1, y2);
		bounds = Arrays.copyOf(originalBounds, 4);

		fullWidth = originalBounds[0] - originalBounds[1];
		fullHeight = originalBounds[2] - originalBounds[3];

		x = bounds[0] - (fullWidth / 2);
		y = bounds[2] - (fullHeight / 2);
	}

	// **************************//
	// **** ACCESSOR METHODS ****//
	// **************************//

	public String getName() {
		return this.name + " Set";
	}

	public int getMaxEscapeTime() {
		return maxEscapeTime;
	}

	public double getMaxEscapeDistance() {
		return maxEscapeDistance;
	}

	public Dimension getResolution() {
		return this.resolution;
	}

	// *************************//
	// **** MUTATOR METHODS ****//
	// *************************//

	public void setMaxEscapeTime(int newEscapeTime) {
		this.maxEscapeTime = newEscapeTime;
		calculateAndShow();
	}

	public void setMaxEscapeDistance(double newEscapeDistance) {
		this.maxEscapeDistance = newEscapeDistance;
		calculateAndShow();
	}

	public void setResolution(Dimension newResolution) {
		this.resolution = newResolution;
		calculateAndShow();
	}
}
