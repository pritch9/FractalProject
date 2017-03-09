package code.UI;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class FractalViewer extends JFrame{
	
	public FractalViewer(){
		setupJMenuBar();
		setupJFrame();
	}

	private void setupJFrame() {
		
	}

	private void setupJMenuBar() {
		
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
	}

}
