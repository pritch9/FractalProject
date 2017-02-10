package code.JUnit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.Mandelbrot;

public class MandelbrotTest {
	
	// [x] [y]
	private int[][] _points;
	private Mandelbrot _mand;
	private int _max;
	
	@Before
	public void setup(){
		_mand = new Mandelbrot();
		_max = _mand.getMaxEscapes();
		_points = _mand.getPoints();
	}
	
	@Test
	public void test(){
		assertEquals(_max, _points[256][256]);
		assertEquals(0, _points[511][511]);
	}

}
