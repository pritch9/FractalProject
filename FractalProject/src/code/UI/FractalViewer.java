package code.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import code.Fractals.BurningShip;
import code.Fractals.Fractal;
import code.Fractals.Julia;
import code.Fractals.Mandelbrot;
import code.Fractals.Multibrot;
import edu.buffalo.fractal.FractalPanel;

public class FractalViewer extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5603717614547419262L;

	/**
	 * Menubar
	 */
	private JMenuBar _menuBar;

	/**
	 * Fractal references
	 */
	private Fractal[] _fractals;

	/**
	 * Fractal reference currently being displayed
	 */
	private Fractal _current;

	/**
	 * FractalPanel courtesy of @MHertz
	 */
	private FractalPanel _fractalPanel;

	/**
	 * Number of Colors used for color scheme
	 */
	private int _colors = 255;

	/**
	 * Current color scheme number
	 */
	private int _colorNumber;

	private Rectangle zoomer;

	private JButton zoomIn;
	private JButton zoomOut;
	private JButton reset;
	private JLabel message;
	private volatile boolean zoom = false;
	private Timer recenter;
	private int zooms = 0;

	/**
	 * Constructor setting up FractalViewer
	 */
	public FractalViewer() {
		this.setResizable(false);
		setupFractalPanel();
		setupFractals();
		setupJMenuBar();
		setupZoomMenu();
		setupJFrame();
	}

	private void setupZoomMenu() {
		JPanel b = new JPanel();
		b.setLayout(new BorderLayout());
		b.setBackground(Color.WHITE);
		b.setOpaque(true);
		JPanel z = new JPanel();
		z.setLayout(new FlowLayout());
		zoomOut = new JButton("Zoom Out");
		zoomOut.addActionListener((e) -> {
			if(!zoom){
				this.zoomOut.setText("Stop");
				new Thread(new Runnable() {
					public void run() {
						zoomOut(_current.getX(), _current.getY());
						// zoomIn.setText("Zoom In");
					}
				}).start();
			} else {
				zoomOut.setText("Continue");
				zoomIn.setEnabled(true);
				zoom = false;
			}
		});

		z.add(zoomOut);

		zoomIn = new JButton("Zoom In");
		zoomIn.addActionListener((e) -> {
			if (!zoom) {
				zoomOut.setEnabled(false);
				reset.setEnabled(false);
				new Thread(new Runnable() {
					public void run() {
						if (_current.fullView()) {
							zoomIn(_current.coolX, _current.coolY);
						} else
							zoomIn(_current.getX(), _current.getY());
					}
				}).start();
			} else {
				zoomOut.setEnabled(true);
				reset.setEnabled(true);
				zoom = false;
				zoomIn.setText("Continue");
			}
		});

		z.add(zoomIn);
		

		reset = new JButton("Reset");
		reset.addActionListener((e) -> {
			zoom = false;
			zooms = 0;
			_current.reset();
			_fractalPanel.updateImage(_current.getPoints());
		});

		z.add(reset);
		b.add(z, BorderLayout.SOUTH);
		JPanel top = new JPanel();
		message = new JLabel("Zoom Board");
		message.setFont(new Font("Times New Roman", Font.BOLD, 24));
		top.add(message);

		b.add(top, BorderLayout.NORTH);
		this.add(b, BorderLayout.SOUTH);
		
		recenter = new Timer(3, (e)->{
			int times = 15;
			
			double xDist = Math.abs(_current.getX() - _current.getCenterX())/times*((_current.getX() > _current.getCenterX()) ? -1 : 1);
			double yDist = Math.abs(_current.getY() - _current.getCenterY())/times*((_current.getY() > _current.getCenterY()) ? -1 : 1);
			
			_current.shift(xDist, yDist);
		});
		recenter.setDelay(10);
	}

	/**
	 * Sets up FractalPanel
	 */
	private void setupFractalPanel() {
		_fractalPanel = new FractalPanel();
		_fractalPanel.setLayout(null);
		this.add(_fractalPanel, BorderLayout.CENTER);
	}

	/**
	 * Sets up JFrame (this)
	 */
	private void setupJFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		this.validate();
		this.pack();
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		_fractalPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
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
				_fractalPanel.drawRectangle(zoomer);
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
			}

		});

		_fractalPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println(_current.getX(e.getX()) + " " + _current.getY(e.getY()));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				zoomer = new Rectangle(e.getPoint());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				Point p1 = new Point(zoomer.x, zoomer.y);
				Point p2 = new Point(zoomer.width + zoomer.x, zoomer.height + zoomer.y);
				_fractalPanel.removeRect();
				if(zoomer.width < 3 || zoomer.height < 3) {
					_fractalPanel.updateImage(_current.getPoints());
					return;
				}
				zoomer = null;
				_current.zoom(p1, p2);
				_fractalPanel.updateImage(_current.getPoints());
			}

		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (zoomer != null) {
			g.setColor(Color.BLACK);
			g.drawRect(zoomer.x, zoomer.y, zoomer.width, zoomer.height);
		}
	}

	/**
	 * Setting up JMenuBar
	 */
	private void setupJMenuBar() {

		_menuBar = new JMenuBar(); // instantiate menubar

		JMenu file = new JMenu("File");

		_menuBar.add(file); // add file menu to menubar

		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit); // add exit item to File menu

		exit.addActionListener(new ActionListener() { // Exit button action
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // Exits the Program
			}
		});

		JMenu menu = new JMenu("Fractals"); // Fractal Menu
		menu.setMnemonic(KeyEvent.VK_F); // this is a cool nothing
		menu.getAccessibleContext().setAccessibleDescription("Change the fractal being viewed"); // description

		for (Fractal f : _fractals) { // add item for every fractal
			JMenuItem i = new JMenuItem(f.getName());
			i.getAccessibleContext().setAccessibleDescription("Show the " + f.getName() + " in the viewer");
			i.addActionListener((e) -> {
				changeFractal(f);
			});
			menu.add(i);
		}
		_menuBar.add(menu); // add Fractal menu to menubar

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

		JMenuItem colorD = new JMenuItem("Green"); // green
		colorD.getAccessibleContext().setAccessibleDescription("Changes Fractals to color D");
		colorD.addActionListener((e) -> changeColor(4));
		colorBar.add(colorD);

		_menuBar.add(colorBar); // add color menu to menubar

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
						"Enter a new escape distance ( > 0 )\n(current: " + _current.getEscapeDistance() + ")"); // pop
																													// up
																													// menu
				try {
					double d = Double.parseDouble(s); // convert to double
					if (d > 0) {
						changeED(d); // if double is > 0, change escape distance
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
						"Enter a new max escape time ( > 0 )\n(current: " + _current.getMaxEscapes() + ")"); // pop
																												// up
																												// menu
				try {
					int d = Integer.parseInt(s); // convert to double
					if (d > 0) {
						changeMET(d); // if double is > 0, change max escape
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
				String s = JOptionPane.showInputDialog("Enter a new color density ( > 0 )\n(current: " + _colors + ")"); // pop
																															// up
				try {
					int d = Integer.parseInt(s); // convert to integer
					if (d > 0) {
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

		item = new JMenuItem("Reset"); // Change number of colors
		item.getAccessibleContext().setAccessibleDescription("basically restarts the program");
		item.addActionListener((e) -> {
			zoom = false;
			Driver.reset();
		});
		menu.add(item); // add item
		_menuBar.add(menu); // add menu
		this.setJMenuBar(_menuBar); // set menubar
	}

	/**
	 * 
	 * Change escape distance
	 * 
	 * @param d
	 *            new escape distance
	 */
	private void changeED(double d) {
		_current.setEscapeDistance(d);
		_fractalPanel.updateImage(_current.getPoints());
	}

	/**
	 * Change max escape time
	 * 
	 * @param d
	 *            new escape distance
	 */
	private void changeMET(int d) {

		// STILL CHANGES ESCAPE DISTANCE
		// UNSURE OF HOW TO CHANGE MAX EXCAPE TIME
		_current.setMax(d);
		_fractalPanel.updateImage(_current.getPoints());
	}

	/**
	 * Change number of colors
	 * 
	 * @param d
	 *            new number of colors
	 */
	private void changeColorDensity(int d) {
		_colors = d;
		changeColor(_colorNumber);
		_fractalPanel.updateImage(_current.getPoints());
	}

	/**
	 * Change displayed fractal
	 * 
	 * @param f
	 *            Fractal reference
	 */
	private void changeFractal(Fractal f) {
		_current = f;
		_fractalPanel.updateImage(_current.getPoints());
	}

	/**
	 * Store all Fractal references
	 */
	private void setupFractals() {
		_fractals = new Fractal[4];
		_fractals[0] = new Mandelbrot(_fractalPanel.getWidth(), _fractalPanel.getHeight());
		_fractals[1] = new Julia(_fractalPanel.getWidth(), _fractalPanel.getHeight());
		_fractals[2] = new BurningShip(_fractalPanel.getWidth(), _fractalPanel.getHeight());
		_fractals[3] = new Multibrot(_fractalPanel.getWidth(), _fractalPanel.getHeight());
		_current = _fractals[0];
		this.changeColor(3);
	}

	/**
	 * Chance color scheme 1 -> Blue 2 -> Gray 3 -> Rainbow 4 -> Green
	 * 
	 * @param num
	 *            number of color scheme
	 */
	private void changeColor(int num) {
		switch (num) {
		case 1:
			_fractalPanel.setIndexColorModel(ColorModelFactory.createBluesColorModel(_colors));
			break;
		case 2:
			_fractalPanel.setIndexColorModel(ColorModelFactory.createGrayColorModel(_colors));
			break;
		case 3:
			_fractalPanel.setIndexColorModel(ColorModelFactory.createRainbowColorModel(_colors));
			break;
		case 4:
			_fractalPanel.setIndexColorModel(ColorModelFactory.createGreenColorModel(_colors));
			break;
		}
		if(!zoom) _fractalPanel.updateImage(_current.getPoints());
		_colorNumber = num;
	}

	private void zoomOut(double x, double y) {
		zoom = true;
		zoomIn.setEnabled(false);
		zoomIn.setText("Zoom In");
		//recenter.restart();
		do {
			try {
				SwingUtilities.invokeAndWait(() -> {
					_fractalPanel.updateImage(_current.getNextZoomOut());
				});
				zooms--;
				int num = _colors + (zooms);
				_current.setMax(num);
				changeColor(num);
				System.out.println(_colors);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		} while (zoom && !_current.fullView());
		zoom = false;
		zooms = 0;
		_fractalPanel.updateImage(_current.getPoints());
		zoomIn.setEnabled(true);
	}
	
	private void zoomIn(double x, double y) {
		zoomIn.setText("Stop");
		zoom = true;
		while (zoom && (_current.getWidth() > 3.597122599785507E-14 && _current.getHeight() > 3.597122599785507E-14)) {
			try {
				SwingUtilities.invokeAndWait(() -> {
					_fractalPanel.updateImage(_current.getNextZoomIn(x, y));
				});
				zooms++;
				int num = _colors + (zooms);
				_current.setMax(num);
				changeColor(num);
				System.out.println(_colors+zooms + " " + _current.getMaxEscapes());
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(zoom){
			this.zoomIn.setText("MAX");
			this.zoomIn.setEnabled(false);
		}
		zoom = false;
		this.zoomOut.setEnabled(true);
	}
}
