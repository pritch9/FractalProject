package code.JUnit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.Multibrot;

public class MultibrotTest {
	
	// [x] [y]
	private int[][] _points;
	private Multibrot _multi;
	private int _max;
	
	@Before
	public void setup(){
		_multi = new Multibrot();
		_max = _multi.getMaxEscapes();
		_points = _multi.getPoints();
	}
	
	@Test
	public void testXCoordinates(){
		assertEquals(-1, _multi.getX(0), 0.001);
		assertEquals(0.0, _multi.getX(256), 0.001);
		assertEquals(1.0, _multi.getX(512), 0.001);
		assertEquals(0, _multi.getCol(-1.0));
		assertEquals(256, _multi.getCol(0.0));
		assertEquals(511, _multi.getCol(1.0));
	}

	@Test
	public void testYCoordinates(){
		assertEquals(-1.3, _multi.getY(0), 0.001);
		assertEquals(0.0, _multi.getY(256),0.001);
		assertEquals(1.3, _multi.getY(512), 0.001);
		assertEquals(0, _multi.getRow(-1.3));
		assertEquals(256, _multi.getRow(0.0));
		assertEquals(511, _multi.getRow(1.3));
	}
	
	@Test
	public void testMaxEscapes(){
		assertEquals(_max, _points[_multi.getCol(0.5859375)][_multi.getRow(0.24375000000000108)]);
	}
	
	@Test
	public void testSingleEscapes(){
		assertEquals(1, _points[_multi.getCol(0.9921875)][_multi.getRow(1.05625)]);
	}
	
	@Test
	public void testCorrectArrayLength(){
		assertEquals(512, _points.length);
		assertEquals(512, _points[0].length);
		assertEquals(512, _points[256].length);
		assertEquals(512, _points[511].length);
	}

}
