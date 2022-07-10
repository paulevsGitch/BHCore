package paulevs.bhcore.noise;

public interface Noise {
	double eval(double x, double z);
	double eval(double x, double y, double z);
}
