package code.UI.listeners;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import code.Fractals.Julia;
import code.UI.FractalViewer;
import code.UI.FractalViewer.ZoomState;
import edu.buffalo.fractal.FractalPanel;

public class MouseListener implements MouseMotionListener, java.awt.event.MouseListener {

	private FractalViewer fractalViewer;
	private FractalPanel fractalPanel;
	
	/**
	 * Zoom rectangle
	 */
	private Rectangle zoomer;
	
	/**
	 * True if julia center animation thing is enabled
	 */
	private boolean julia = false;

	/**
	 * Constructor
	 * @param viewer FractalViewer instance
	 * @param panel FractalPanel being observed
	 */
	public MouseListener(FractalViewer viewer, FractalPanel panel) {
		fractalPanel = panel;
		fractalViewer = viewer;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (!julia) { // not julia enabled
			// get mouse coords
			int x = arg0.getX();
			int y = arg0.getY();
			int g = 0;
			
			// find min width or height
			if (x > y) {
				g = x - zoomer.x;
			} else {
				g = y - zoomer.y;
			}
			g = Math.abs(g);

			// set width/height to min width or height
			zoomer.width = (x > zoomer.x) ? g : -g;
			zoomer.height = (y > zoomer.y) ? g : -g;
			
			fractalPanel.drawRectangle(zoomer); // draw rectangle on GUI
		}
		else // do the julia thing
			((Julia) fractalViewer.getCurrentFractal()).animate(arg0.getX(), arg0.getY());

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!julia) // start rectangle if not julia thingy
			zoomer = new Rectangle(e.getPoint());
		else // do julia thingy
			((Julia) fractalViewer.getCurrentFractal()).animate(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!julia) { // if not julia thingy
			fractalPanel.removeRect(); // stop displaying the rectangle
			// do not bother if rectangle is too tiny
			if (Math.abs(zoomer.width) < 3 || Math.abs(zoomer.height) < 3) {
				fractalPanel.repaint();
				return;
			}
			// try zooming
			if(fractalViewer.getCurrentFractal().zoom(zoomer)){
				// change zoom state to mid
				fractalViewer.changeZoomButtonState(ZoomState.ZOOM_MID);
			} else {
				fractalViewer.changeZoomButtonState(ZoomState.ZOOM_IN_MAX);
			}
			zoomer = null; // delete rectangle object
		}
	}

	/**
	 * Tells the mouse listener whether or not julia animation is intended to run
	 * @param b True if julia thing should be animated
	 */
	public void setJuliaEnabled(boolean b) {
		julia = b;
	}

}
