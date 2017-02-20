package code.Fractals;

public class Julia {
	
	/**
	 * Max amount of passes
	 */
	private int _max;
	
	/**
	 * Dimensions of points
	 */
	private int _rows, _cols;
	
	/**
	 * Bounds
	 */
	private double _upperX,_lowerX, _upperY, _lowerY;
	
	

	/**
	 * Initialize with defaults:
	 * Max Passes: 255
	 * Bounds: 	X: [-2.15,0.6]
	 * 			Y: [-1.3,1.3]
	 */
	public Julia(){
		this(512, 512);
	}
	
	/**
	 * Set rows and columns upon construction
	 * 
	 * @param rows amount of rows in points array
	 * @param cols amount of columns in points array
	 */
	public Julia(int rows, int cols){
		_max = 255; // Default max passes
		
		// Defaults points dimensions to 512
		_rows = rows;
		_cols = cols;
		
		setBounds(1.7,-1.7,1.0,-1.0);
	}
	
	/**
	 * Set the max amount of passes
	 */
	public void setMax(int max){
		_max = max;
	}
	
	/**
	 * Sets the bounds of the set
	 */
	public void setBounds(double upperX, double lowerX, double upperY, double lowerY){
		// The next two if statements makes sure the upper bound is > the lower bound and swaps them if needed
		if(upperX < lowerX){
			double t = upperX;
			upperX = lowerX;
			lowerX = t;
		}
		if(upperY < lowerY){
			double t = upperY;
			upperY = lowerY;
			lowerY = t;
		}
		
		// Sets all the instance variables
		_upperX = upperX;
		_lowerX = lowerX;
		_upperY = upperY;
		_lowerY = lowerY;
	}

	/**
	 * Returns the 2-D array of points
	 * @return int[][] points
	 */
	public int[][] getPoints() {
		int[][] points = new int[_cols][_rows];
		double xCalc, yCalc, tX, tY, tmp, dist;
		for(int rows = 0; rows < _rows; rows++){ // y
			for(int cols = 0; cols < _cols; cols++){ // x
				// convert rows and cols to cartesian plot
				xCalc = getX(cols);
				yCalc = getY(rows);
				tX = xCalc;
				tY = yCalc;
				
				int passes = 0;
				dist = Math.sqrt(tX*tX + tY*tY);
				while(dist < 2 && passes < _max){
					tmp = tX*tX - tY*tY + -0.72689;
					tY = 2.0 * tX * tY + 0.188887;
					tX = tmp;
					dist = Math.sqrt(tX*tX + tY*tY);
					passes++;
				}
				points[cols][rows] = passes;
			}
		}
		return points;
	}

	/**\
	 * Returns the max amount of passes
	 * @return max
	 */
	public int getMaxEscapes() {
		return _max;
	}

	/**
	 * Returns the X coordinate for a given column
	 * @param col
	 * @return Cartesian X coordinate
	 */
	public double getX(int col){
		return ((_upperX - _lowerX) / _cols) * col + _lowerX;
	}
	
	/**
	 * Returns the Y coordinate for a given rows
	 * @param row
	 * @return Cartesian Y coordinate
	 */
	public double getY(int row) {
		return ((_upperY - _lowerY) / _rows) * row + _lowerY;
	}
	
	/**
	 * Returns the column for a given Cartesian x coordinate
	 * @param x coordinate
	 * @return column if x is contained by the bounds
	 */
	public int getCol(double x){
		if(x < _lowerX || x > _upperX) return -1;
		return (int)Math.floor(((x - _lowerX)*(_cols-1)) / (_upperX - _lowerX));
	}

	/**
	 * Returns the row for a given Cartesian y coordinate
	 * @param y coordinate
	 * @return column if y is contained by the bounds (-1 if not)
	 */
	public int getRow(double y) {
		if(y < _lowerY || y > _upperY) return -1;
		return (int) Math.floor(((y - _lowerY)* (_rows-1)) / (_upperY - _lowerY));
	}

}
