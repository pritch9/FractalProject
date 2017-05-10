package code.UI;

import javax.swing.SwingUtilities;

public class Driver {

	private static FractalViewer _fractalViewer;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> _fractalViewer = new FractalViewer()); // initial Fractal Viewer which does the rest
	}

	public static void reset() {
		_fractalViewer.dispose();
		_fractalViewer = new FractalViewer();
	}

}
