package code.JUnit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.Mandelbrot;

public class MandelbrotTest {
	
	private Mandelbrot fractal;
	private int max;
	
	@Before
	public void setup(){
		fractal = new Mandelbrot();
		max = fractal.getMaxEscapeTime();
	}
	
	@Test
	public void testXCoordinates(){
		assertEquals(-2.15, fractal.getX(0), 0.001);
		assertEquals(-0.7749999999999, fractal.getX(256), 0.001);
		assertEquals(0.6, fractal.getX(512), 0.001);
		assertEquals(0, fractal.getCol(-2.15));
		assertEquals(256, fractal.getCol(-0.775));
		assertEquals(512, fractal.getCol(0.6));
	}

	@Test
	public void testYCoordinates(){
		assertEquals(-1.3, fractal.getY(0), 0.001);
		assertEquals(0.0, fractal.getY(256),0.001);
		assertEquals(1.3, fractal.getY(512), 0.001);
		assertEquals(0, fractal.getRow(-1.3));
		assertEquals(256, fractal.getRow(0.0));
		assertEquals(512, fractal.getRow(1.3));
	}
	
	@Test
	public void testMaxEscapes(){
		assertEquals(max, fractal.calculate(0.3207031250000001, -0.07109374999999386));
	}
	
	@Test
	public void testSingleEscapes(){
		assertEquals(1, fractal.calculate(0.5946289062500001, 1.2949218750000122));
	}
	
	@Test
	public void test10Escapes(){
		fractal.setMaxEscapeDistance(3.0);
		int passes = fractal.calculate(0.46007827788650374, -0.3383561643835661);
		assertEquals(10, passes);
		fractal.setMaxEscapeDistance(2.0);
	}
	
	@Test
	public void testMaxEscapesWithNewMaxEscapeTime(){
		fractal.setMaxEscapeTime(135);
		int passes = fractal.calculate(0.3207031250000001, -0.07109374999999386);
		assertEquals(135, passes);
		fractal.setMaxEscapeTime(255);
	}
	
	@Test
	public void testCorrectArrayLength(){
		int[][] points = fractal.calculateAndBlock();
		assertEquals(512, points.length);
		for(int x = 0; x < points.length; x++){
			assertEquals(512, points[x].length);
		}
	}

}
