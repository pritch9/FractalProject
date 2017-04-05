package code.Fractals;

public class Julia extends Fractal {
	
	/**
	 * Most basic constructor
	 */
	public Julia(){
		this(512, 512);
	}
	
	/**
	 * Constructor with dimension parameters
	 * @param rows number of rows
	 * @param cols number of columns
	 */
	public Julia(int rows, int cols){
		super("Julia", rows, cols, 1.7,-1.7,1.0,-1.0);
		this.coolX = -0.09998952442329354;
		this.coolY = 0.6397321930046009;
	}
	
	public int calculate(double xCalc, double yCalc){
		double tX, tY, tmp;
		double dist;
		tX = xCalc;
		tY = yCalc;
		
		int passes = 0;
		dist = Math.sqrt(tX*tX + tY*tY);
		while(dist <= this.getEscapeDistance() && passes < this.getMaxEscapes()){
			tmp = tX*tX - tY*tY + -0.72689;
			tY = 2.0*tX*tY + 0.188887;
			tX = tmp;
			dist = Math.sqrt(tX*tX + tY*tY);
			passes++;
		}
		return passes;
	}
}