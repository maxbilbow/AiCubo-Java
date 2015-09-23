package click.rmx.engine.math;


import static java.lang.Math.random;

public final class Tools {

	public static double rBounds(int min, int max) {
		
		return random() * (max - min) + min;
	}
	
	

}
