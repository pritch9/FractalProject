package code.JUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.BurningShip;

public class BurningShipTest {
	
	// [x] [y]
	private int[][] _points;
	private BurningShip _burn;
	private int _max;
	
	@Before
	public void setup(){
		_burn = new BurningShip();
		_max = _burn.getMaxEscapes();
		_points = _burn.getPoints();
	}
	
	@Test
	public void testXCoordinates(){
		assertEquals(-1.8, _burn.getX(0), 0.001);
		assertEquals(-1.75, _burn.getX(256), 0.001);
		assertEquals(-1.7, _burn.getX(512), 0.001);
		assertEquals(0, _burn.getCol(-1.8));
		assertEquals(255, _burn.getCol(-1.75));
		assertEquals(511, _burn.getCol(-1.7));
	}

	@Test
	public void testYCoordinates(){
		assertEquals(-0.08, _burn.getY(0), 0.001);
		assertEquals(-0.0275, _burn.getY(256),0.001);
		assertEquals(0.025, _burn.getY(512), 0.001);
		assertEquals(0, _burn.getRow(-0.08));
		assertEquals(255, _burn.getRow(-0.0275));
		assertEquals(511, _burn.getRow(0.025));
	}
	
	@Test
	public void testMaxEscapes(){
		assertEquals(_max, _points[_burn.getCol(-1.7443359374999874)][_burn.getRow(-0.017451171875000338)]);
	}
	
	@Test
	public void testSingleEscapes(){
		for(int x = 0; x < _points.length; x++){
			for(int y = 0; y < _points[x].length; y++){
				assertTrue(_points[x][y] > 1);
			}
		}
	}
	
	@Test
	public void testCorrectArrayLength(){
		assertEquals(512, _points.length);
		assertEquals(512, _points[0].length);
		assertEquals(512, _points[256].length);
		assertEquals(512, _points[511].length);
	}

}
