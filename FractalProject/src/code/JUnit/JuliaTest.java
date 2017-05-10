package code.JUnit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.Julia;

public class JuliaTest {
	

	private Julia fractal;
	private int max;
	
	@Before
	public void setup(){
		fractal = new Julia();
		max = fractal.getMaxEscapeTime();
	}
	
	@Test
	public void testXCoordinates(){
		assertEquals(-1.7, fractal.getX(0), 0.001);
		assertEquals(0.0, fractal.getX(256), 0.001);
		assertEquals(1.7, fractal.getX(512), 0.001);
		assertEquals(0, fractal.getCol(-1.7));
		assertEquals(256, fractal.getCol(0.0));
		assertEquals(512, fractal.getCol(1.7));
	}

	@Test
	public void testYCoordinates(){
		assertEquals(-1.0, fractal.getY(0), 0.001);
		assertEquals(0.0, fractal.getY(256),0.001);
		assertEquals(1.0, fractal.getY(512), 0.001);
		assertEquals(0, fractal.getRow(-1.0));
		assertEquals(256, fractal.getRow(0.0));
		assertEquals(512, fractal.getRow(1.0000));
	}
	
	@Test
	public void testMaxEscapes(){
		assertEquals(max, fractal.calculate(1.0492187499999897, -0.234375));
	}
	
	@Test
	public void testSingleEscapes(){
		assertEquals(1, fractal.calculate(1.6933593749999853, 0.9765625));
	}
	
	@Test
	public void test10Escapes(){
		fractal.setMaxEscapeDistance(3.0);
		int passes = fractal.calculate(1.4609374999999998, -0.12109375); 
		assertEquals(10, passes);
		fractal.setMaxEscapeDistance(2.0);
	}
	

	@Test
	public void testMaxEscapesWithNewMaxEscapeTime(){
		fractal.setMaxEscapeTime(135);
		int passes = fractal.calculate(1.0492187499999897, -0.234375);
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
