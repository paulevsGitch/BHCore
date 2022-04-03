package paulevs.bhcore.storage.vector;

import java.util.Locale;

public class Vec2F {
	public float x;
	public float y;
	
	public Vec2F() {
		this(0, 0);
	}
	
	public Vec2F(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2F set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vec2F set(Vec2F vector) {
		this.x = vector.x;
		this.y = vector.y;
		return this;
	}
	
	public Vec2F add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vec2F add(Vec2F vector) {
		return add(vector.x, vector.y);
	}
	
	public Vec2F subtract(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vec2F subtract(Vec2F vector) {
		return subtract(vector.x, vector.y);
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "[%f, %f]", x, y);
	}
	
	@Override
	public Vec2F clone() {
		return new Vec2F(x, y);
	}
}
