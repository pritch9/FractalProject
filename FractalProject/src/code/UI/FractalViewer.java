package code.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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

public class FractalViewer extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5603717614547419262L;
	
	private JMenuBar _menuBar;
	private Fractal[] _fractals;
	private Fractal _current;
	private FractalPanel _fractalPanel;
	private JPanel _animationPanel;
	private Timer _t;
	private int _colors = 255;
	private int _colorNumber;

	private int _tInc = 1;
	
	public FractalViewer(){
		setupFractals();
		setupJMenuBar();
		setupFractalPanel();
		setupJFrame();
	}

	private void setupFractalPanel() {
		_fractalPanel = new FractalPanel();
		this.changeColor(3);
		this.add(_fractalPanel, BorderLayout.CENTER);
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
		_fractalPanel.setMinimumSize(_fractalPanel.getSize());
		_fractalPanel.setMaximumSize(_fractalPanel.getSize());
		_fractalPanel.setPreferredSize(_fractalPanel.getSize());
	}

	private void setupJMenuBar() {
		
		_menuBar = new JMenuBar();
		
        JMenu file = new JMenu("File");
        _menuBar.add(file);
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
	            System.exit(0);}
        }); 
        
		
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
		
		JMenuItem colorA = new JMenuItem("Blue");
		colorA.getAccessibleContext().setAccessibleDescription("Changes Fractals to color A");
		colorA.addActionListener((e) -> changeColor(1));
		colorBar.add(colorA);
		
		JMenuItem colorB = new JMenuItem("Gray");
		colorB.getAccessibleContext().setAccessibleDescription("Changes Fractals to color B");
		colorB.addActionListener((e) -> changeColor(2));
		colorBar.add(colorB);
		
		JMenuItem colorC = new JMenuItem("Rainbow");
		colorC.getAccessibleContext().setAccessibleDescription("Changes Fractals to color C");
		colorC.addActionListener((e) -> changeColor(3));
		colorBar.add(colorC);
		
		JMenuItem colorD = new JMenuItem("Green");
		colorD.getAccessibleContext().setAccessibleDescription("Changes Fractals to color D");
		colorD.addActionListener((e) -> changeColor(4));
		colorBar.add(colorD);
		
		_menuBar.add(colorBar);
		
		menu = new JMenu("Options");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("Optional Edits");
		JMenuItem escapeDistance = new JMenuItem("Change Escape Distance");
		escapeDistance.getAccessibleContext().setAccessibleDescription("Change the escape time for the fractal");
		escapeDistance.addActionListener((e)->{ 
			boolean wrong = true;
			while(wrong) {
				wrong = false;
				String s = JOptionPane.showInputDialog("Enter a new escape distance ( > 0 )\n(default: 255)");
				try {
					double d = Double.parseDouble(s);
					if(d > 0){
						changeED(d);
					} else {
						wrong = true;
					}
				} catch (NumberFormatException e1){
					wrong = true;
				} catch (NullPointerException e2){}
			}
		});
		menu.add(escapeDistance);
		JMenuItem item = new JMenuItem("Change Color Density");
		item.getAccessibleContext().setAccessibleDescription("Change the amount of colors in the fractal");
		item.addActionListener((e) -> {
			boolean wrong = true;
			while(wrong) {
				wrong = false;
				String s = JOptionPane.showInputDialog("Enter a new color density ( > 0 )\n(current: " + _colors + ")");
				try {
					int d = Integer.parseInt(s);
					if(d > 0){
						changeColorDensity(d);
					} else {
						wrong = true;
					}
				} catch (NumberFormatException e1){
					if(e1.getMessage().trim().isEmpty() || e1.getMessage() != "null") wrong = true;
				} catch (NullPointerException e2){}
			}
		});
		menu.add(item);
		_menuBar.add(menu);
		/*
		menu = new JMenu("Animations");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("Optional Edits");
		item = new JMenuItem("Colors");
		item.getAccessibleContext().setAccessibleDescription("Change the amount of colors in the fractal");
		item.addActionListener((e) -> {
			_animationPanel = new JPanel();
			BorderLayout b = new BorderLayout();
			b.setHgap(0);
			b.setVgap(0);
			_animationPanel.setLayout(b);
			JPanel bottom = new JPanel();
			JPanel top = new JPanel();
			JPanel padding = new JPanel();
			padding.setPreferredSize(new Dimension(10, 15));

			BorderLayout g = new BorderLayout();
			g.setHgap(0);
			top.setLayout(g);
			JLabel label = new JLabel(" " + " Color Density Animation");
			label.setFont(new Font("sans-serif", Font.BOLD, 24));
			JButton ex = new JButton("X");
			ex.setPreferredSize(new Dimension(40,40));
			ex.setOpaque(false);
			ex.setBorderPainted(false);
			ex.setFocusable(false);
			ex.setContentAreaFilled(false);
			ex.setFont(new Font("sans-serif", Font.PLAIN, 10));
			ex.addActionListener((e3)->{
				this.getContentPane().remove(_animationPanel);
				_animationPanel = null;
				this.revalidate();
				this.pack();
				this.repaint();
			});
			
			top.add(label, BorderLayout.WEST);
			top.add(ex, BorderLayout.EAST);
			
			JButton start = new JButton("Start");
			JButton stop = new JButton("Stop");
			BorderLayout layout = new BorderLayout();
			layout.setHgap(0);
			layout.setVgap(0);
			bottom.setLayout(layout);
			bottom.add(start, BorderLayout.WEST);
			bottom.add(stop, BorderLayout.EAST);
			start.setPreferredSize(new Dimension(getWidth()/2-3, 50));
			stop.setPreferredSize(new Dimension(getWidth()/2-3, 50));
			
			start.addActionListener((g1)->{
				_t = new Timer(0, (h) -> {
					if(this._colors >= this._current.getMaxEscapes() || this._colors <= Math.abs(_tInc)*8) _tInc = -_tInc;
					this._colors += _tInc;
					this.changeColor(_colorNumber);
					_t.setDelay(this._current.getMaxEscapes()*3 / _colors - 3);
					System.out.println(_t.getDelay());
				});
				_t.setDelay(this._current.getMaxEscapes()*3 / _colors);
				_t.start();
			});
			
			stop.addActionListener((e1)->{
				if(_t.isRunning()){
					_t.stop();
					_colors = 255;
					this.changeColor(_colorNumber);
				}
			});
			
			_animationPanel.add(top, BorderLayout.NORTH);
			_animationPanel.add(padding, BorderLayout.CENTER);
			_animationPanel.add(bottom, BorderLayout.SOUTH);
			
			this.add(_animationPanel, BorderLayout.SOUTH);
			this.pack();
			this.revalidate();
			this.repaint();
		});
		menu.add(item);
		_menuBar.add(menu);
		*/
		this.setJMenuBar(_menuBar);
	}
	
	private void changeED(double d) {
		_current.setEscapeDistance(d);
		_fractalPanel.updateImage(_current.getPoints());
	}
	
	private void changeColorDensity(int d){
		_colors = d;
		changeColor(_colorNumber);
		_fractalPanel.updateImage(_current.getPoints());
	}

	private void changeFractal(Fractal f) {
		_current = f;
		_fractalPanel.updateImage(_current.getPoints());
	}

	private void setupFractals(){
		_fractals = new Fractal[4];
		_fractals[0] = new Mandelbrot();
		_fractals[1] = new Julia();
		_fractals[2] = new BurningShip();
		_fractals[3] = new Multibrot();
		_current = _fractals[0];
	}
	
	private void changeColor(int num){
		switch(num){
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
		_fractalPanel.updateImage(_current.getPoints());
		_colorNumber = num;
	}
}
