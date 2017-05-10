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
	private Rectangle zoomer;
	private boolean julia = false;

	public MouseListener(FractalViewer viewer, FractalPanel panel) {
		fractalPanel = panel;
		fractalViewer = viewer;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (!julia) {
			int x = arg0.getX();
			int y = arg0.getY();
			int g = 0;
			if (x > y) {
				g = x - zoomer.x;
			} else {
				g = y - zoomer.y;
			}
			g = Math.abs(g);

			zoomer.width = (x > zoomer.x) ? g : -g;
			zoomer.height = (y > zoomer.y) ? g : -g;
			fractalPanel.drawRectangle(zoomer);
		}
		else
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
		if (!julia)
			zoomer = new Rectangle(e.getPoint());
		else
			((Julia) fractalViewer.getCurrentFractal()).animate(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!julia) {
			fractalPanel.removeRect();
			if (Math.abs(zoomer.width) < 3 || Math.abs(zoomer.height) < 3) {
				fractalPanel.repaint();
				return;
			}
			fractalViewer.getCurrentFractal().zoom(zoomer);
			zoomer = null;
			fractalViewer.changeZoomButtonState(ZoomState.ZOOM_MID);
		}
	}

	public void setJuliaEnabled(boolean b) {
		julia = b;
	}

}
