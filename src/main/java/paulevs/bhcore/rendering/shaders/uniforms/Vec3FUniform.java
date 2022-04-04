package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.opengl.GL20;
import paulevs.bhcore.storage.vector.Vec3F;

public class Vec3FUniform extends Uniform {
	private Vec3F vector = new Vec3F();
	
	public Vec3FUniform(int id) {
		super(id);
	}
	
	public Vec3F getVector() {
		return vector;
	}
	
	public void set(float x, float y, float z) {
		vector.set(x, y, z);
	}
	
	@Override
	public void bind() {
		GL20.glUniform3f(getID(), vector.x, vector.y, vector.z);
	}
}
