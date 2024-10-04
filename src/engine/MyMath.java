package engine;

public class MyMath {

	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

}
