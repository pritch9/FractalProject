package code.UI;

import javax.swing.SwingUtilities;

public class Driver {

	/**
	 * Le fractal viewer instance
	 */
	private static FractalViewer fractalViewer;
	
	/**
	 * HELL YEAH
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> fractalViewer = new FractalViewer()); // initial Fractal Viewer which does the rest
	}

	/**
	 * Reset Program
	 */
	public static void reset() {
		fractalViewer.dispose();
		fractalViewer = new FractalViewer();
	}

}
