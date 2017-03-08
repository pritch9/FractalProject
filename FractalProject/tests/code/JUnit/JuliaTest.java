package code.JUnit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import code.Fractals.Julia;

public class JuliaTest {
	
	// [x] [y]
	private int[][] _points;
	private Julia _julia;
	private int _max;
	
	@Before
	public void setup(){
		_julia = new Julia();
		_max = _julia.getMaxEscapes();
		_points = _julia.getPoints();
		for(int x = 0; x < 512; x++){
			for(int y = 0; y < 512; y++){
				if(_points[x][y] == _max){
					System.out.println(x + " " + y);
				}
			}
		}
	}
	
	@Test
	public void testXCoordinates(){
		assertEquals(-1.7, _julia.getX(0), 0.001);
		assertEquals(0.0, _julia.getX(256), 0.001);
		assertEquals(1.7, _julia.getX(512), 0.001);
		assertEquals(0, _julia.getCol(-1.7f));
		assertEquals(256, _julia.getCol(0.0f));
		assertEquals(511, _julia.getCol(1.7f));
	}

	@Test
	public void testYCoordinates(){
		assertEquals(-1.0, _julia.getY(0), 0.001);
		assertEquals(0.0, _julia.getY(256),0.001);
		assertEquals(1.0, _julia.getY(512), 0.001);
		assertEquals(0, _julia.getRow(-1.0f));
		assertEquals(256, _julia.getRow(0.0f));
		assertEquals(511, _julia.getRow(1.0000f));
	}
	
	@Test
	public void testMaxEscapes(){
		assertEquals(_max, _points[_julia.getCol(1.0492187499999897f)][_julia.getRow(-0.234375f)]);
	}
	
	@Test
	public void testSingleEscapes(){
		assertEquals(1, _points[_julia.getCol(1.6933593749999853f)][_julia.getRow(0.9765625f)]);
	}
	
	@Test
	public void testCorrectArrayLength(){
		assertEquals(512, _points.length);
		assertEquals(512, _points[0].length);
		assertEquals(512, _points[256].length);
		assertEquals(512, _points[511].length);
	}

}
