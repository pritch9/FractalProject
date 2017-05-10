package code.JUnit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.Multibrot;

public class MultibrotTest {

	private Multibrot fractal;
	private int max;

	@Before
	public void setup() {
		fractal = new Multibrot();
		max = fractal.getMaxEscapeTime();
	}

	@Test
	public void testXCoordinates() {
		assertEquals(-1, fractal.getX(0), 0.001);
		assertEquals(0.0, fractal.getX(256), 0.001);
		assertEquals(1.0, fractal.getX(512), 0.001);
		assertEquals(0, fractal.getCol(-1.0));
		assertEquals(256, fractal.getCol(0.0));
		assertEquals(512, fractal.getCol(1.0));
	}

	@Test
	public void testYCoordinates() {
		assertEquals(-1.3, fractal.getY(0), 0.001);
		assertEquals(0.0, fractal.getY(256), 0.001);
		assertEquals(1.3, fractal.getY(512), 0.001);
		assertEquals(0, fractal.getRow(-1.3));
		assertEquals(256, fractal.getRow(0.0));
		assertEquals(512, fractal.getRow(1.3));
	}

	@Test
	public void testMaxEscapes() {
		assertEquals(max, fractal.calculate(0.5859375, 0.24375000000000108));
	}

	@Test
	public void testSingleEscapes() {
		assertEquals(1, fractal.calculate(0.9921875, 1.05625));
	}

	@Test
	public void test10Escapes() {
		fractal.setMaxEscapeDistance(3.0);
		int passes = fractal.calculate(0.6953125, -0.533203125);
		assertEquals(10, passes);
		fractal.setMaxEscapeDistance(2.0);
	}

	@Test
	public void testMaxEscapesWithNewMaxEscapeTime() {
		fractal.setMaxEscapeDistance(2.0);
		fractal.setMaxEscapeTime(135);
		int passes = fractal.calculate(0.5859375, 0.24375000000000108);
		assertEquals(135, passes);
		fractal.setMaxEscapeTime(255);
	}

	@Test
	public void testCorrectArrayLength() {
		int[][] points = fractal.calculateAndBlock();
		assertEquals(512, points.length);
		for (int x = 0; x < points.length; x++) {
			assertEquals(512, points[x].length);
		}
	}

}
