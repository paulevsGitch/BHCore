package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.opengl.GL20;
import paulevs.bhcore.storage.vector.Vec2F;

public class Vec2FUniform extends Uniform {
	private Vec2F vector = new Vec2F();
	
	public Vec2FUniform(int id) {
		super(id);
	}
	
	public Vec2F getVector() {
		return vector;
	}
	
	public void set(float x, float y) {
		vector.set(x, y);
	}
	
	@Override
	public void bind() {
		GL20.glUniform2f(getID(), vector.x, vector.y);
	}
}
