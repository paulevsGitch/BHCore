package paulevs.bhcore.noise;

import net.minecraft.util.maths.MathHelper;

import java.util.Arrays;
import java.util.Random;

public class VoronoiNoise implements Noise {
	private final Random random = new Random(0);
	private final float[] buffer = new float[27];
	private final int seed;
	
	public VoronoiNoise(int seed) {
		this.seed = seed;
	}
	
	@Override
	public double eval(double x, double z) {
		return getValue(x, z);
	}
	
	@Override
	public double eval(double x, double y, double z) {
		return getValue(x, y, z);
	}
	
	public float getValue(double x, double z) {
		getDistances(x, z, buffer);
		return buffer[0];
	}
	
	public float getValue(double x, double y, double z) {
		getDistances(x, y, z, buffer);
		return buffer[0];
	}
	
	public float getF1F3(double x, double y, double z) {
		getDistances(x, y, z, buffer);
		return buffer[0] / buffer[2];
	}
	
	public void getDistances(double x, double y, double z, float[] buffer) {
		int x1 = MathHelper.floor(x);
		int y1 = MathHelper.floor(y);
		int z1 = MathHelper.floor(z);
		float sdx = (float) (x - x1);
		float sdy = (float) (y - y1);
		float sdz = (float) (z - z1);
		
		byte index = 0;
		float[] point = new float[3];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int k = -1; k < 2; k++) {
					getPoint(seed, x1 + i, y1 + j, z1 + k, random, point);
					float dx = point[0] + i - sdx;
					float dy = point[1] + j - sdy;
					float dz = point[2] + k - sdz;
					float distance = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
					buffer[index++] = distance;
				}
			}
		}
		Arrays.sort(buffer);
	}
	
	public void getDistances(double x, double z, float[] buffer) {
		int x1 = MathHelper.floor(x);
		int z1 = MathHelper.floor(z);
		float sdx = (float) (x - x1);
		float sdz = (float) (z - z1);
		
		byte index = 0;
		float[] point = new float[2];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				getPoint(seed, x1 + i, z1 + j, random, point);
				float dx = point[0] + i - sdx;
				float dz = point[1] + j - sdz;
				float distance = MathHelper.sqrt(dx * dx + dz * dz);
				buffer[index++] = distance;
			}
		}
		Arrays.sort(buffer);
	}
	
	public float getValue(int seed, double x, double z, Random random) {
		int x1 = MathHelper.floor(x);
		int z1 = MathHelper.floor(z);
		float sdx = (float) (x - x1);
		float sdz = (float) (z - z1);
		
		float distance = 10;
		float[] point = new float[2];
		int[] pointResult = new int[2];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				getPoint(seed, x1 + i, z1 + j, random, point);
				float dx = point[0] + i - sdx;
				float dz = point[1] + j - sdz;
				float d = MathHelper.sqrt(dx * dx + dz * dz);
				if (d < distance) {
					distance = d;
					pointResult[0] = i;
					pointResult[1] = j;
				}
			}
		}
		
		random.setSeed(hash(x1 + pointResult[0], seed, z1 + pointResult[1]));
		return random.nextFloat();
	}
	
	private void getPoint(int seed, int x, int y, int z, Random random, float[] point) {
		random.setSeed(hash(x, y + seed, z));
		point[0] = random.nextFloat();
		point[1] = random.nextFloat();
		point[2] = random.nextFloat();
	}
	
	private void getPoint(int seed, int x, int z, Random random, float[] point) {
		random.setSeed(hash(x, seed, z));
		point[0] = random.nextFloat();
		point[1] = random.nextFloat();
	}
	
	private long hash(int x, int y, int z) {
		return net.modificationstation.stationapi.api.util.math.MathHelper.hashCode(x, y, z);
	}
}
