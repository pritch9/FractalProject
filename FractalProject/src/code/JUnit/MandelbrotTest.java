package code.JUnit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.Mandelbrot;

public class MandelbrotTest {
	
	private Mandelbrot _mand;
	private int _max;
	
	@Before
	public void setup(){
		_mand = new Mandelbrot(512, 512);
		_max = _mand.getMaxEscapes();
	}
	
	@Test
	public void testXCoordinates(){
		assertEquals(-2.15, _mand.getX(0), 0.001);
		assertEquals(-0.7749999999999, _mand.getX(256), 0.001);
		assertEquals(0.6, _mand.getX(512), 0.001);
		assertEquals(0, _mand.getCol(-2.15));
		assertEquals(256, _mand.getCol(-0.775));
		assertEquals(512, _mand.getCol(0.6));
	}

	@Test
	public void testYCoordinates(){
		assertEquals(-1.3, _mand.getY(0), 0.001);
		assertEquals(0.0, _mand.getY(256),0.001);
		assertEquals(1.3, _mand.getY(512), 0.001);
		assertEquals(0, _mand.getRow(-1.3));
		assertEquals(256, _mand.getRow(0.0));
		assertEquals(512, _mand.getRow(1.3));
	}
	
	@Test
	public void testMaxEscapes(){
		assertEquals(_max, _mand.getEscapeTime(0.3207031250000001, -0.07109374999999386));
	}
	
	@Test
	public void testSingleEscapes(){
		assertEquals(1, _mand.getEscapeTime(0.5946289062500001, 1.2949218750000122));
	}
	
	@Test
	public void test10Escapes(){
		_mand.setEscapeDistance(3.0);
		int passes = _mand.getEscapeTime(0.46007827788650374, -0.3383561643835661);
		assertEquals(10, passes);
		_mand.setEscapeDistance(2.0);
	}
	
	@Test
	public void testCorrectArrayLength(){
		assertEquals(512, _mand.getPoints().length);
		assertEquals(512, _mand.getPoints()[0].length);
	}

}
