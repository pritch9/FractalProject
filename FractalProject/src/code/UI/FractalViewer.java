package code.UI;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import code.Fractals.BurningShip;
import code.Fractals.Fractal;
import code.Fractals.Julia;
import code.Fractals.Mandelbrot;
import code.Fractals.Multibrot;
import edu.buffalo.fractal.FractalPanel;

public class FractalViewer extends JFrame{
	private JMenuBar _menuBar;
	private Fractal[] _fractals;
	private Fractal _current;
	private FractalPanel _fractalPanel;
	
	public FractalViewer(){
		setupFractals();
		setupJMenuBar();
		setupFractalPanel();
		setupJFrame();
	}

	private void setupFractalPanel() {
		_fractalPanel = new FractalPanel();
		_fractalPanel.setIndexColorModel(ColorModelFactory.createGrayColorModel(50));
		_fractalPanel.updateImage(_current.getPoints());
		this.add(_fractalPanel);
	}

	private void setupJFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		this.pack();
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}

	private void setupJMenuBar() {
		
		_menuBar = new JMenuBar();
		
		/*
		
		// Fractal menu
		menu = new JMenu("Fractals"); // new JMenu
		menu.setMnemonic(KeyEvent.VK_F); // not sure if this does anything
		menu.getAccessibleContext().setAccessibleDescription("I scream for Ice Cream!"); // description for menu
		
		// fractal menu items
		menuItem = new JMenuItem("Mandelbrot Set"); // Menu option text
		menuItem.getAccessibleContext().setAccessibleDescription("Show the Mandelbrot Set in the viewer"); // menu option description
		menuItem.addActionListener((e)->{ changeFractal(MANDELBROT); }); // action listener
		menu.add(menuItem); // add the item to the menu

		
		*/
		
		
		JMenu menu = new JMenu("Fractals");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("Change the fractal being viewed");
		
		for(Fractal f : _fractals){
			JMenuItem i = new JMenuItem(f.getName());
			i.getAccessibleContext().setAccessibleDescription("Show the " + f.getName() + " in the viewer");
			i.addActionListener((e) -> { changeFractal(f); });
			menu.add(i);
		}
		_menuBar.add(menu);

		// Creates new menu item with description
		JMenu colorBar = new JMenu("Colors");
		colorBar.setMnemonic(KeyEvent.VK_F);
		colorBar.getAccessibleContext().setAccessibleDescription("Changes Fractal Colors");
		
		JMenuItem colorA = new JMenuItem("Color A");
		colorA.getAccessibleContext().setAccessibleDescription("Changes Fractals to color A");
		colorA.addActionListener(null); //null for now
		colorBar.add(colorA);
		
		JMenuItem colorB = new JMenuItem("Color B");
		colorB.getAccessibleContext().setAccessibleDescription("Changes Fractals to color B");
		colorB.addActionListener(null); //null for now
		colorBar.add(colorB);
		
		JMenuItem colorC = new JMenuItem("Color C");
		colorC.getAccessibleContext().setAccessibleDescription("Changes Fractals to color C");
		colorC.addActionListener(null); //null for now
		colorBar.add(colorC);
		
		JMenuItem colorD = new JMenuItem("Color D");
		colorD.getAccessibleContext().setAccessibleDescription("Changes Fractals to color D");
		colorD.addActionListener(null); //null for now
		colorBar.add(colorD);
		
		_menuBar.add(colorBar);
		
		
		this.setJMenuBar(_menuBar);
	}
	
	private void changeFractal(Fractal f) {
		_current = f;
		
	}

	private void setupFractals(){
		_fractals = new Fractal[4];
		_fractals[0] = new Mandelbrot();
		_fractals[1] = new Julia();
		_fractals[2] = new BurningShip();
		_fractals[3] = new Multibrot();
		_current = _fractals[0];
	}
}
