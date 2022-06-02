package paulevs.bhcore.util;

import net.modificationstation.stationapi.api.util.math.Direction;

public class MathUtil {
	public static final Direction[] DIRECTIONS = Direction.values();
	public static final Direction[] HORIZONTAL = {
		Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
	};
	
	/**
	 * Clamps value to specified range.
	 * @param value - Value to clamp.
	 * @param min - Minimum output value.
	 * @param max - Maximum output value
	 * @return clamped value [min-max]
	 */
	public static int clamp(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}
	
	/**
	 * Clamps value to specified range.
	 * @param value - Value to clamp.
	 * @param min - Minimum output value.
	 * @param max - Maximum output value
	 * @return clamped value [min-max]
	 */
	public static float clamp(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}
	
	/**
	 * Clamps value to specified range.
	 * @param value - Value to clamp.
	 * @param min - Minimum output value.
	 * @param max - Maximum output value
	 * @return clamped value [min-max]
	 */
	public static double clamp(double value, double min, double max) {
		return value < min ? min : value > max ? max : value;
	}
	
	/**
	 * Linear interpolation between two values.
	 * @param a - Start value.
	 * @param b - End value.
	 * @param delta - Value change [0.0-1.0]
	 * @return linear interpolation [a-b]
	 */
	public static float lerp(float a, float b, float delta) {
		return a + delta * (b - a);
	}
	
	/**
	 * Linear interpolation between two values.
	 * @param a - Start value.
	 * @param b - End value.
	 * @param delta - Value change [0.0-1.0]
	 * @return linear interpolation [a-b]
	 */
	public static double lerp(double a, double b, double delta) {
		return a + delta * (b - a);
	}
	
	/**
	 * Convert X Y Z coordinates into index of array for 16x16x16 section.
	 * @param x X coordinate [0-15]
	 * @param y Y coordinate [0-15]
	 * @param z Z coordinate [0-15]
	 * @return array index [0-4095]
	 */
	public static int getIndex16(int x, int y, int z) {
		return x << 8 | y << 4 | z;
	}
	
	/**
	 * Calculates closets power of two value to the given number (greater or equal).
	 * Values below zero will be ignored.
	 * Example output: 756 -> 1024, 186 -> 256.
	 * @param value source number.
	 * @return closest power of two number (greater or equal).
	 */
	public static int getClosestPowerOfTwo(int value) {
		if (value <= 0) return 0;
		byte index = 0;
		byte count = 0;
		for (byte i = 0; i < 32; i++) {
			byte bit = (byte) (value & 1);
			if (bit == 1) {
				index = i;
				count++;
			}
			value >>>= 1;
		}
		return count == 1 ? 1 << index : 1 << (index + 1);
	}
	
	public static int wrap(int value, int side) {
		int offset = value / side * side;
		if (offset > value) offset -= side;
		float delta = (float) (value - offset) / side;
		return (int) (delta * side);
	}
	
	/**
	 * Rotate direction clockwise.
	 * @param dir {@link Direction} to rotate
	 * @return rotated {@link Direction}
	 */
	public static Direction rotateCW(Direction dir) {
		Direction result = dir;
		switch (dir) {
			case NORTH -> result = Direction.EAST;
			case SOUTH -> result = Direction.WEST;
			case EAST -> result = Direction.SOUTH;
			case WEST -> result = Direction.NORTH;
		}
		return result;
	}
}
