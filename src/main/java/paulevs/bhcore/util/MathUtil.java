package paulevs.bhcore.util;

public class MathUtil {
	public static int clamp(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}
	
	public static float clamp(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}
	
	public static double clamp(double value, double min, double max) {
		return value < min ? min : value > max ? max : value;
	}
	
	public static float lerp(float a, float b, float delta) {
		return a + delta * (b - a);
	}
	
	public static double lerp(double a, double b, double delta) {
		return a + delta * (b - a);
	}
}
