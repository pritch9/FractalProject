package code.JUnit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.Julia;

public class JuliaTest {
	

	private Julia _julia;
	private int _max;
	
	@Before
	public void setup(){
		_julia = new Julia(512,512);
		_max = _julia.getMaxEscapes();
	}
	
	@Test
	public void testXCoordinates(){
		assertEquals(-1.7, _julia.getX(0), 0.001);
		assertEquals(0.0, _julia.getX(256), 0.001);
		assertEquals(1.7, _julia.getX(512), 0.001);
		assertEquals(0, _julia.getCol(-1.7));
		assertEquals(256, _julia.getCol(0.0));
		assertEquals(512, _julia.getCol(1.7));
	}

	@Test
	public void testYCoordinates(){
		assertEquals(-1.0, _julia.getY(0), 0.001);
		assertEquals(0.0, _julia.getY(256),0.001);
		assertEquals(1.0, _julia.getY(512), 0.001);
		assertEquals(0, _julia.getRow(-1.0));
		assertEquals(256, _julia.getRow(0.0));
		assertEquals(512, _julia.getRow(1.0000));
	}
	
	@Test
	public void testMaxEscapes(){
		assertEquals(_max, _julia.getEscapeTime(1.0492187499999897, -0.234375));
	}
	
	@Test
	public void testSingleEscapes(){
		assertEquals(1, _julia.getEscapeTime(1.6933593749999853, 0.9765625));
	}
	
	@Test
	public void test10Escapes(){
		_julia.setEscapeDistance(3.0);
		int passes = _julia.getEscapeTime(1.4609374999999998, -0.12109375); // [475][221] values used: [>][<]
		assertEquals(10, passes);
		_julia.setEscapeDistance(2.0);
	}
	

	@Test
	public void testMaxEscapesWithNewMaxEscapeTime(){
		_julia.setEscapeDistance(2.0);
		_julia.setMax(135);
		int passes = _julia.getEscapeTime(1.0492187499999897, -0.234375);
		assertEquals(135, passes);
		_julia.setMax(255);
	}
	
	@Test
	public void testCorrectArrayLength(){
		assertEquals(512, _julia.getPoints().length);
		assertEquals(512, _julia.getPoints()[0].length);
	}

}
