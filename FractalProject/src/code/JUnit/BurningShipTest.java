package code.JUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.BurningShip;

public class BurningShipTest {
	

	private BurningShip _burn;
	private int _max;
	
	@Before
	public void setup(){
		_burn = new BurningShip(512,512);
		_max = _burn.getMaxEscapes();
	}
	
	@Test
	public void testXCoordinates(){
		assertEquals(-1.8, _burn.getX(0), 0.001);
		assertEquals(-1.75, _burn.getX(256), 0.001);
		assertEquals(-1.7, _burn.getX(512), 0.001);
		assertEquals(0, _burn.getCol(-1.8));
		assertEquals(256, _burn.getCol(-1.75));
		assertEquals(512, _burn.getCol(-1.7));
	}

	@Test
	public void testYCoordinates(){
		assertEquals(-0.08, _burn.getY(0), 0.001);
		assertEquals(-0.0275, _burn.getY(256),0.001);
		assertEquals(0.025, _burn.getY(512), 0.001);
		assertEquals(0, _burn.getRow(-0.08));
		assertEquals(256, _burn.getRow(-0.0275));
		assertEquals(512, _burn.getRow(0.025));
	}
	
	@Test
	public void testMaxEscapes(){
		assertEquals(_max, _burn.getEscapeTime(-1.7443359374999874, -0.017451171875000338));
	}
	
	@Test
	public void test10Escapes(){
		_burn.setEscapeDistance(3.0);
		int passes = _burn.getEscapeTime(-1.701, 0.0030136986301371603);
		assertEquals(10, passes);
		_burn.setEscapeDistance(2.0);
	}
	
	@Test
	public void testSingleEscapes(){
		for(int x = 0; x < _burn.getPoints().length; x++){
			for(int y = 0; y < _burn.getPoints()[x].length; y++){
				assertTrue(_burn.getEscapeTime(x, y) > 1);
			}
		}
	}

	@Test
	public void testMaxEscapesWithNewMaxEscapeTime(){
		_burn.setEscapeDistance(2.0);
		_burn.setMax(135);
		int passes = _burn.getEscapeTime(-1.7443359374999874, -0.017451171875000338);
		assertEquals(135, passes);
		_burn.setMax(255);
	}
	
	@Test
	public void testCorrectArrayLength(){
		assertEquals(512, _burn.getPoints().length);
		assertEquals(512, _burn.getPoints()[0].length);
	}

}
