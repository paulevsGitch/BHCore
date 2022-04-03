package paulevs.bhcore.storage.vector;

import java.util.Locale;

public class Vec3D {
	public double x;
	public double y;
	public double z;

	public Vec3D() {}

	public Vec3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double lengthSqr() {
		return x * x + y * y + z * z;
	}

	public double length() {
		return Math.sqrt(lengthSqr());
	}

	public double distanceSqr(Vec3D vector) {
		double dx = x - vector.x;
		double dy = y - vector.y;
		double dz = z - vector.z;
		return dx * dx + dy * dy + dz * dz;
	}

	public double distance(Vec3D vector) {
		return Math.sqrt(distanceSqr(vector));
	}
	
	public double distanceSqr(Vec3F vector) {
		double dx = x - vector.x;
		double dy = y - vector.y;
		double dz = z - vector.z;
		return dx * dx + dy * dy + dz * dz;
	}
	
	public double distance(Vec3F vector) {
		return Math.sqrt(distanceSqr(vector));
	}

	public Vec3D add(double value) {
		return add(value, value, value);
	}

	public Vec3D add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vec3D add(Vec3D vector) {
		return add(vector.x, vector.y, vector.z);
	}

	public Vec3D set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vec3D set(Vec3D vector) {
		return set(vector.x, vector.y, vector.z);
	}

	public Vec3D subtract(double value) {
		return subtract(value, value, value);
	}

	public Vec3D subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Vec3D subtract(Vec3D vector) {
		return subtract(vector.x, vector.y, vector.z);
	}

	public Vec3D divide(double value) {
		this.x /= value;
		this.y /= value;
		this.z /= value;
		return this;
	}

	public Vec3D cross(Vec3D vector) {
		double nx = this.y * vector.z - this.z * vector.y;
		double ny = this.z * vector.x - this.x * vector.z;
		double nz = this.x * vector.y - this.y * vector.x;
		return set(nx, ny, nz);
	}

	public Vec3D normalize() {
		double l = lengthSqr();
		return l > 0 ? this.divide(Math.sqrt(l)) : this;
	}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "[%f, %f, %f]", x, y, z);
	}

	public Vec3D multiply(double value) {
		return multiply(value, value, value);
	}

	public Vec3D multiply(double x, double y, double z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	public Vec3D rotateX(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double ny = y * cos - z * sin;
		double nz = z * cos + y * sin;
		return set(x, ny, nz);
	}

	public Vec3D rotateY(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double nx = x * cos - z * sin;
		double nz = z * cos + x * sin;
		return set(nx, y, nz);
	}

	public Vec3D rotateZ(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double nx = x * cos - y * sin;
		double ny = y * cos + x * sin;
		return set(nx, ny, z);
	}

	public Vec3D invert() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
		return this;
	}
}
