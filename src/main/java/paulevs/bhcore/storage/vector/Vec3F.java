package paulevs.bhcore.storage.vector;

import net.minecraft.util.maths.MathHelper;
import paulevs.bhcore.util.MathUtil;

import java.util.Locale;
import java.util.Objects;

public class Vec3F {
	public float x;
	public float y;
	public float z;

	public Vec3F() {}

	public Vec3F(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float lengthSqr() {
		return x * x + y * y + z * z;
	}

	public float length() {
		return MathHelper.sqrt(lengthSqr());
	}

	public float distanceSqr(Vec3F vector) {
		float dx = x - vector.x;
		float dy = y - vector.y;
		float dz = z - vector.z;
		return dx * dx + dy * dy + dz * dz;
	}

	public float distance(Vec3F vector) {
		return MathHelper.sqrt(distanceSqr(vector));
	}

	public Vec3F add(float value) {
		return add(value, value, value);
	}

	public Vec3F add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vec3F add(Vec3F vector) {
		return add(vector.x, vector.y, vector.z);
	}

	public Vec3F set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vec3F set(Vec3F vector) {
		return set(vector.x, vector.y, vector.z);
	}

	public Vec3F subtract(float value) {
		return subtract(value, value, value);
	}

	public Vec3F subtract(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Vec3F subtract(Vec3F vector) {
		return subtract(vector.x, vector.y, vector.z);
	}

	public Vec3F divide(float value) {
		this.x /= value;
		this.y /= value;
		this.z /= value;
		return this;
	}

	public Vec3F cross(Vec3F vector) {
		float nx = this.y * vector.z - this.z * vector.y;
		float ny = this.z * vector.x - this.x * vector.z;
		float nz = this.x * vector.y - this.y * vector.x;
		return set(nx, ny, nz);
	}

	public Vec3F normalize() {
		float l = lengthSqr();
		return l > 0 ? this.divide(MathHelper.sqrt(l)) : this;
	}

	public Vec3F multiply(float value) {
		return multiply(value, value, value);
	}

	public Vec3F multiply(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	public Vec3F rotateX(float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float ny = y * cos - z * sin;
		float nz = z * cos + y * sin;
		return set(x, ny, nz);
	}

	public Vec3F rotateY(float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float nx = x * cos - z * sin;
		float nz = z * cos + x * sin;
		return set(nx, y, nz);
	}

	public Vec3F rotateZ(float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float nx = x * cos - y * sin;
		float ny = y * cos + x * sin;
		return set(nx, ny, z);
	}

	public Vec3F invert() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
		return this;
	}
	
	public Vec3F lerp(Vec3F b, float delta) {
		this.x = MathUtil.lerp(this.x, b.x, delta);
		this.y = MathUtil.lerp(this.y, b.y, delta);
		this.z = MathUtil.lerp(this.z, b.z, delta);
		return this;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "[%f, %f, %f]", x, y, z);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof Vec3F)) return false;
		Vec3F vec = (Vec3F) obj;
		return x == vec.x && y == vec.y && z == vec.z;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
	
	@Override
	public Vec3F clone() {
		return new Vec3F(x, y, z);
	}
}
