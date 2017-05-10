package code.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import code.Fractals.BurningShip;
import code.Fractals.Fractal;
import code.Fractals.Julia;
import code.Fractals.Mandelbrot;
import code.Fractals.Multibrot;
import code.UI.listeners.MouseListener;
import edu.buffalo.fractal.FractalPanel;

@SuppressWarnings("serial")
public class FractalViewer extends JFrame {

	/**
	 * Static reference of the one and only FractalViewer
	 */
	private static FractalViewer i;

	/**
	 * Gets the static reference of the one and only FractalViewer
	 */
	public static FractalViewer get() {
		return i;
	}

	// ******************** //
	// **** COMPONENTS **** //
	// ******************** //

	/**
	 * Menu bar
	 */
	private JMenuBar menuBar;

	/**
	 * Fractal Panel
	 */
	private FractalPanel fractalPanel;

	/**
	 * Zoom Menu
	 */
	private JPanel zoomMenu;

	/**
	 * Zoom Menu buttons
	 */
	private JButton zoomIn, zoomOut, reset, julia;

	/**
	 * Zoom Menu message label
	 */
	private JLabel message;

	// ******************* //
	// **** VARIABLES **** //
	// ******************* //

	private Fractal current;
	private Fractal[] fractals;
	private int colorDensity = 255;
	private int colorModel = 1;
	private Dimension resolution;
	private MouseListener listener;
	private volatile boolean juliaAnimate = false;
	private volatile boolean zoom = false;

	/**
	 * States of Zoom Menu buttons
	 */
	public static enum ZoomState {
		ZOOM_MID, AUTO_ZOOM_MAX, ZOOM_OUT_MAX, ZOOM_IN_MAX, ZOOM_IN, ZOOM_OUT, JULIA
	};

	// ********************* //
	// **** COSNTRUCTOR **** //
	// ********************* //

	/**
	 * Basic constructor
	 */
	public FractalViewer() {
		i = this;
		this.setResizable(false);
		setupFractals();
		setupFractalPanel();
		setupJMenuBar();
		setupZoomMenu();
		setupJFrame();
	}

	// ************************** //
	// **** FRACTAL MUTATORS **** //
	// ************************** //

	/**
	 * Change the index color model (aka change the colors)
	 * 
	 * @param indexColorModel
	 *            number associated with index color model
	 */
	private void changeColor(int indexColorModel) {
		colorModel = indexColorModel;
		switch (colorModel) {
		case 1:
			fractalPanel.setIndexColorModel(ColorModelFactory.createBluesColorModel(colorDensity));
			break;
		case 2:
			fractalPanel.setIndexColorModel(ColorModelFactory.createGrayColorModel(colorDensity));
			break;
		case 3:
			fractalPanel.setIndexColorModel(ColorModelFactory.createRainbowColorModel(colorDensity));
			break;
		}

		if (!zoom) // recalculate only if gui is not zooming
			current.calculateAndShow();
	}

	/**
	 * Change the number of colors displayed by fractal panel
	 * 
	 * @param numberOfColors
	 *            New number of colors
	 */
	private void changeColorDensity(int numberOfColors) {
		colorDensity = numberOfColors;
		changeColor(colorModel);
	}
	
	/**
	 * Change the current fractal and display
	 * 
	 * @param f
	 *            Next current fractal
	 */
	private void changeFractal(Fractal f) {
		current = f; // set the current to f
		julia.setVisible(current.equals(fractals[1])); // display julia button
														// if Julia set is
														// chosen

		current.calculateAndShow();
	}

	/**
	 * reset the fractal
	 */
	private void resetFractal() {
		current.reset();
		changeZoomButtonState(ZoomState.ZOOM_OUT_MAX);
	}

	// ****************************** //
	// **** FRACTAL ZOOM METHODS **** //
	// ****************************** //

	/**
	 * Change the Zoom Menu buttons based on current GUI actions
	 * 
	 * @param state
	 *            State of the GUI and fractal zoom
	 */
	public void changeZoomButtonState(ZoomState state) {
		switch (state) {
		case ZOOM_MID:
			message.setText(current.getName());

			zoomIn.setText("Zoom In");
			zoomIn.setEnabled(true);

			zoomOut.setText("Zoom Out");
			zoomOut.setEnabled(true);

			reset.setEnabled(true);

			julia.setEnabled(false);

			menuBar.getMenu(1).setEnabled(true);
			menuBar.getMenu(3).setEnabled(true);
			break;
		case ZOOM_IN_MAX:
			message.setText("Can't zoom in any further!");
		case AUTO_ZOOM_MAX:
			zoomIn.setText("MAX");
			zoomIn.setEnabled(false);

			zoomOut.setText("Zoom Out");
			zoomOut.setEnabled(true);

			reset.setEnabled(true);

			julia.setEnabled(false);
			break;
		case ZOOM_OUT_MAX:
			message.setText(current.getName());
			zoomIn.setText("Zoom In");
			zoomIn.setEnabled(true);

			zoomOut.setText("Max");
			zoomOut.setEnabled(false);

			reset.setEnabled(true);

			julia.setText("Change center");
			julia.setEnabled(true);

			menuBar.getMenu(1).setEnabled(true);
			menuBar.getMenu(3).setEnabled(true);

			break;
		case ZOOM_IN:
			message.setText("Zooming in");
			zoomIn.setText("Stop");
			zoomIn.setEnabled(true);

			zoomOut.setText("Zoom Out");
			zoomOut.setEnabled(false);

			reset.setEnabled(false);

			julia.setEnabled(false);

			menuBar.getMenu(1).setEnabled(false);
			menuBar.getMenu(3).setEnabled(false);
			break;
		case ZOOM_OUT:
			message.setText("Zooming out");
			zoomIn.setText("Zoom In");
			zoomIn.setEnabled(false);

			zoomOut.setText("Stop");
			zoomOut.setEnabled(true);

			reset.setEnabled(false);

			julia.setEnabled(false);
			menuBar.getMenu(1).setEnabled(false);
			menuBar.getMenu(3).setEnabled(false);
			break;
		case JULIA:
			message.setText("Click to change the Julia Set!");
			zoomIn.setEnabled(false);

			zoomOut.setEnabled(false);

			reset.setEnabled(false);

			julia.setText("Stop");
			menuBar.getMenu(1).setEnabled(false);
			menuBar.getMenu(3).setEnabled(false);

			break;
		default:
			message.setText("Error: Unknown button state: " + state.toString());
			break;
		}
	}
	
	/**
	 * Zoom in to a given point
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param move
	 *            True if x and y are not the center
	 */
	public void zoomIn(double x, double y, boolean move) {
		changeZoomButtonState(ZoomState.ZOOM_IN);
		zoom = true;
		double minW = current.getFullWidth() * 1.0E-10;
		double minH = current.getFullHeight() * 1.0E-10;
		while (zoom && (current.getWidth() > minW && current.getHeight() > minH)) {
			try {
				SwingUtilities.invokeAndWait(() -> {
					current.zoomIn(x, y, move);
				});
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (zoom) {
			changeZoomButtonState(ZoomState.ZOOM_MID);
		} else {
			changeZoomButtonState(ZoomState.AUTO_ZOOM_MAX);
		}
		zoom = false;
		this.zoomOut.setEnabled(true);
	}

	/**
	 * Zooms fractal out until full view
	 */
	public void zoomOut() {
		changeZoomButtonState(ZoomState.ZOOM_OUT);
		// recenter.restart();
		zoom = true;
		while (zoom && !current.fullView()) {
			try {
				SwingUtilities.invokeAndWait(() -> {
					current.zoomOut();
				});
			} catch (InvocationTargetException | InterruptedException e) {
			}
		}
		if (zoom)
			changeZoomButtonState(ZoomState.ZOOM_MID);
		else
			changeZoomButtonState(ZoomState.ZOOM_OUT_MAX);
		zoom = false;
	}

	// ******************* //
	// **** ACCESSORS **** //
	// ******************* //

	/**
	 * Gets the current Fractal being displayed
	 * 
	 * @return Current Fractal
	 */
	public Fractal getCurrentFractal() {
		return current;
	}

	/**
	 * Gets the fractal element
	 * 
	 * @return The current fractal panel
	 */
	public FractalPanel getFractalPanel() {
		return fractalPanel;
	}

	// *********************** //
	// **** SETUP METHODS **** //
	// *********************** //

	/**
	 * Setup the fractal instantiations
	 */
	private void setupFractals() {
		resolution = new Dimension(512, 512);
		fractals = new Fractal[4];
		fractals[0] = new Mandelbrot(resolution);
		fractals[1] = new Julia(resolution);
		fractals[2] = new BurningShip(resolution);
		fractals[3] = new Multibrot(resolution);
		current = fractals[0];
	}

	/**
	 * Setup the fractal panel
	 */
	private void setupFractalPanel() {
		fractalPanel = new FractalPanel();
		fractalPanel.setSize(resolution);
		add(fractalPanel, BorderLayout.CENTER);

		// setup the mouselistener for the fractal panel
		listener = new MouseListener(this, fractalPanel);
		fractalPanel.addMouseMotionListener(listener);
		fractalPanel.addMouseListener(listener);

		changeColor(3); // set color and display current fractal
	}

	/**
	 * Setup the JFrame
	 */
	private void setupJFrame() {
		// "try" to make the program look pretty. Java look and feel is ugly AF
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Make the JFrame pack nice
		this.validate();
		this.pack();

		// center
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);

		// exit on close
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true); // show
	}

	/**
	 * Setup the Menu bar
	 */
	private void setupJMenuBar() {
		menuBar = new JMenuBar(); // instantiate menubar

		// File menu
		JMenu file = new JMenu("File");

		menuBar.add(file); // add file menu to menubar

		JMenuItem item1 = new JMenuItem("Restart Program"); // Change number of
															// colors
		item1.getAccessibleContext().setAccessibleDescription("basically restarts the program");
		item1.addActionListener((e) -> {
			zoom = false;
			Driver.reset();
		});
		file.add(item1); // add item

		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit); // add exit item to File menu

		exit.addActionListener(e -> System.exit(0)); // Exits the Program);
		// end file menu

		// fractal menu
		JMenu menu = new JMenu("Fractals"); // Fractal Menu
		menu.setMnemonic(KeyEvent.VK_F); // this is a cool nothing
		menu.getAccessibleContext().setAccessibleDescription("Change the fractal being viewed"); // description

		for (Fractal f : fractals) { // add item for every fractal
			JMenuItem i = new JMenuItem(f.getName());
			i.getAccessibleContext().setAccessibleDescription("Show the " + f.getName() + " in the viewer");
			i.addActionListener((e) -> changeFractal(f));
			menu.add(i);
		}
		menuBar.add(menu); // add Fractal menu to menubar
		// end fractal menu

		// color menu
		// Creates new menu item with description
		JMenu colorBar = new JMenu("Colors");
		colorBar.setMnemonic(KeyEvent.VK_F);
		colorBar.getAccessibleContext().setAccessibleDescription("Changes Fractal Colors");

		JMenuItem colorA = new JMenuItem("Blue"); // blue
		colorA.getAccessibleContext().setAccessibleDescription("Changes Fractals to color A");
		colorA.addActionListener((e) -> changeColor(1));
		colorBar.add(colorA);

		JMenuItem colorB = new JMenuItem("Gray"); // gray
		colorB.getAccessibleContext().setAccessibleDescription("Changes Fractals to color B");
		colorB.addActionListener((e) -> changeColor(2));
		colorBar.add(colorB);

		JMenuItem colorC = new JMenuItem("Rainbow"); // rainbow
		colorC.getAccessibleContext().setAccessibleDescription("Changes Fractals to color C");
		colorC.addActionListener((e) -> changeColor(3));
		colorBar.add(colorC);

		JMenuItem colorD = new JMenuItem("Rainbow 2"); // rainbow 2
		colorD.getAccessibleContext().setAccessibleDescription("Changes Fractals to color D");
		colorD.addActionListener((e) -> changeColor(4));
		colorBar.add(colorD);

		menuBar.add(colorBar); // add color menu to menubar
		// end color menu

		// options menu
		menu = new JMenu("Options");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("Optional Edits");

		JMenuItem escapeDistance = new JMenuItem("Change Escape Distance");
		escapeDistance.getAccessibleContext().setAccessibleDescription("Change the escape distance for the fractal");
		escapeDistance.addActionListener((e) -> {
			boolean wrong = true;
			while (wrong) {
				wrong = false;
				String s = JOptionPane.showInputDialog(
						"Enter a new escape distance ( > 0 )\n(current: " + current.getMaxEscapeDistance() + ")"); // pop
																													// up
																													// menu
				try {
					double d = Double.parseDouble(s); // convert to double
					if (d > 0) {
						current.setMaxEscapeDistance(d); // if double is > 0,
															// change escape
															// distance
					} else {
						wrong = true; // re-open menu
					}
				} catch (NumberFormatException e1) { // not number
					wrong = true; // re-open menu
				} catch (NullPointerException e2) {
				} // exit or cancel
			}
		});
		menu.add(escapeDistance); // add escape distance item

		JMenuItem maxEscapeTime = new JMenuItem("Change Max Escape Time");
		maxEscapeTime.getAccessibleContext().setAccessibleDescription("Change the max escape time for the fractal");
		maxEscapeTime.addActionListener((e) -> {
			boolean wrong = true;
			while (wrong) {
				wrong = false;
				String s = JOptionPane.showInputDialog(
						"Enter a new max escape time (0, 255]\n(current: " + current.getMaxEscapeTime() + ")"); // pop
																												// up
																												// menu
				try {
					int d = Integer.parseInt(s); // convert to double
					if (d > 0 /* && d <= 255 */) {
						current.setMaxEscapeTime(d); // if double is > 0, change
														// max escape
						// time
					} else {
						wrong = true; // re-open menu
					}
				} catch (NumberFormatException e1) { // not number
					if (e1.getMessage().trim().isEmpty() || e1.getMessage() != "null") // not
																						// exit
																						// or
																						// cancel
						wrong = true; // re-open menu
				} catch (NullPointerException e2) {
				} // exit or cancel
			}
		});
		menu.add(maxEscapeTime); // add max escape time item

		JMenuItem item = new JMenuItem("Change Color Density"); // Change number
																// of colors
		item.getAccessibleContext().setAccessibleDescription("Change the number of colors in the fractal");
		item.addActionListener((e) -> {
			boolean wrong = true;
			while (wrong) {
				wrong = false;
				String s = JOptionPane
						.showInputDialog("Enter a new color density (0,255]\n(current: " + colorDensity + ")"); // pop
																												// up
				try {
					int d = Integer.parseInt(s); // convert to integer
					if (d > 0 /* && d < 255 */) {
						changeColorDensity(d); // if > 0, change color density
					} else {
						wrong = true; // re-open pop-up
					}
				} catch (NumberFormatException e1) {
					if (e1.getMessage().trim().isEmpty() || e1.getMessage() != "null") // not
																						// exit
																						// or
																						// cancel
						wrong = true; // re-open
				} catch (NullPointerException e2) {
				}
			}
		});
		menu.add(item); // add item

		item = new JMenuItem("Reset Fractal"); // Change number of colors
		item.getAccessibleContext().setAccessibleDescription("Resets the fractal");
		item.addActionListener((e) -> {
			zoom = false;
			resetFractal();
		});
		menu.add(item); // add item
		menuBar.add(menu); // add menu
		// end options menu

		this.setJMenuBar(menuBar); // set menubar

	}

	/**
	 * Setup Zoom Menu
	 */
	private void setupZoomMenu() {
		JPanel b = new JPanel();
		b.setLayout(new BorderLayout());
		b.setBackground(Color.WHITE);
		b.setOpaque(true);
		JPanel z = new JPanel();
		z.setLayout(new FlowLayout());
		zoomOut = new JButton("Max");
		zoomOut.addActionListener((e) -> {
			if (!zoom) {
				new Thread(() -> {
					zoomOut(); // run new zoom out thread
				}).start();
			} else {
				zoom = false;
			}
		});
		zoomOut.setEnabled(false);

		z.add(zoomOut);

		zoomIn = new JButton("Zoom In");
		zoomIn.addActionListener((e) -> {
			if (!zoom) {
				new Thread(() -> { // run new zoom in thread
					if (current.fullView()) {
						zoomIn(current.coolX, current.coolY, true);
					} else
						zoomIn(current.getX(256), current.getY(256), false);
				}).start();
			} else {
				zoom = false;
			}
		});

		z.add(zoomIn);

		reset = new JButton("Reset");
		reset.addActionListener((e) -> {
			resetFractal(); // reset
		});
		z.add(reset);

		// only shown with julia set
		julia = new JButton("Change Center");
		julia.addActionListener((e) -> {
			if (!juliaAnimate) {
				listener.setJuliaEnabled((juliaAnimate = true));
				changeZoomButtonState(ZoomState.JULIA);
			} else {
				listener.setJuliaEnabled((juliaAnimate = false));
				changeZoomButtonState(ZoomState.ZOOM_OUT_MAX);
			}
		});
		julia.setVisible(false);
		z.add(julia);

		b.add(z, BorderLayout.SOUTH);
		JPanel top = new JPanel();
		message = new JLabel(current.getName());
		message.setFont(new Font("Times New Roman", Font.BOLD, 24));
		top.add(message);

		b.add(top, BorderLayout.NORTH);
		this.add(b, BorderLayout.SOUTH);
	}

}
