package code.JUnit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.Multibrot;

public class MultibrotTest {
	
	private Multibrot _multi;
	private int _max;
	
	@Before
	public void setup(){
		_multi = new Multibrot(512,512);
		_max = _multi.getMaxEscapes();
	}
	
	@Test
	public void testXCoordinates(){
		assertEquals(-1, _multi.getX(0), 0.001);
		assertEquals(0.0, _multi.getX(256), 0.001);
		assertEquals(1.0, _multi.getX(512), 0.001);
		assertEquals(0, _multi.getCol(-1.0));
		assertEquals(256, _multi.getCol(0.0));
		assertEquals(512, _multi.getCol(1.0));
	}

	@Test
	public void testYCoordinates(){
		assertEquals(-1.3, _multi.getY(0), 0.001);
		assertEquals(0.0, _multi.getY(256),0.001);
		assertEquals(1.3, _multi.getY(512), 0.001);
		assertEquals(0, _multi.getRow(-1.3));
		assertEquals(256, _multi.getRow(0.0));
		assertEquals(512, _multi.getRow(1.3));
	}
	
	@Test
	public void testMaxEscapes(){
		assertEquals(_max, _multi.getEscapeTime(0.5859375, 0.24375000000000108));
	}
	
	@Test
	public void testSingleEscapes(){
		assertEquals(1, _multi.getEscapeTime(0.9921875, 1.05625));
	}
	
	@Test
	public void test10Escapes(){
		_multi.setEscapeDistance(3.0);
		int passes = _multi.getEscapeTime(0.6953125, -0.533203125);
		assertEquals(10, passes);
		_multi.setEscapeDistance(2.0);
	}
	
	@Test
	public void testCorrectArrayLength(){
		assertEquals(512, _multi.getPoints().length);
		assertEquals(512, _multi.getPoints()[0].length);
	}

}
