package code.Fractals;

public class Julia extends Fractal {
	
	public Julia(){
		this(512, 512);
	}
	
	public Julia(int rows, int cols){
		super("Julia", rows, cols, 1.7,-1.7,1.0,-1.0);
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