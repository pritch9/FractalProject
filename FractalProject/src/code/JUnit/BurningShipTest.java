package code.JUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.BurningShip;

public class BurningShipTest {
	

	private BurningShip fractal;
	private int max;
	private int[][] points;
	
	@Before
	public void setup(){
		fractal = new BurningShip();
		max = fractal.getMaxEscapeTime();
		points = fractal.calculateAndBlock();
	}
	
	@Test
	public void testXCoordinates(){
		assertEquals(-1.8, fractal.getX(0), 0.001);
		assertEquals(-1.75, fractal.getX(256), 0.001);
		assertEquals(-1.7, fractal.getX(512), 0.001);
		assertEquals(0, fractal.getCol(-1.8));
		assertEquals(256, fractal.getCol(-1.75));
		assertEquals(512, fractal.getCol(-1.7));
	}

	@Test
	public void testYCoordinates(){
		assertEquals(-0.08, fractal.getY(0), 0.001);
		assertEquals(-0.0275, fractal.getY(256),0.001);
		assertEquals(0.025, fractal.getY(512), 0.001);
		assertEquals(0, fractal.getRow(-0.08));
		assertEquals(256, fractal.getRow(-0.0275));
		assertEquals(512, fractal.getRow(0.025));
	}
	
	@Test
	public void testMaxEscapes(){
		int passes = fractal.calculate(-1.7443359374999874, -0.017451171875000338);
		assertEquals(max, passes);
	}
	
	@Test
	public void test10Escapes(){
		fractal.setMaxEscapeDistance(3.0);
		int passes = fractal.calculate(-1.701, 0.0030136986301371603);
		assertEquals(10, passes);
		fractal.setMaxEscapeDistance(2.0);
	}
	
	@Test
	public void testSingleEscapes(){
		for(int x = 0; x < points.length; x++){
			for(int y = 0; y < points[x].length; y++){
				assertTrue(fractal.calculate(fractal.getX(x), fractal.getY(y)) > 1);
			}
		}
	}

	@Test
	public void testMaxEscapesWithNewMaxEscapeTime(){
		fractal.setMaxEscapeTime(135);
		int passes = fractal.calculate(-1.7443359374999874, -0.017451171875000338);
		assertEquals(135, passes);
		fractal.setMaxEscapeTime(255);
	}
	
	@Test
	public void testCorrectArrayLength(){
		assertEquals(512, points.length);
		for(int x = 0; x < points.length; x++){
			assertEquals(512, points[x].length);
		}
	}
}
