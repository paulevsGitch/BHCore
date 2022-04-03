package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.opengl.GL20;
import paulevs.bhcore.storage.vector.Vec3F;

public class VectorUniform extends Uniform {
	private Vec3F vector = new Vec3F();
	
	public VectorUniform(int id) {
		super(id);
	}
	
	public void setVector(Vec3F vector) {
		this.vector = vector;
	}
	
	public void set(double x, double y, double z) {
		set((float) x, (float) y, (float) z);
	}
	
	public void set(float x, float y, float z) {
		vector.set(x, y, z);
	}
	
	@Override
	public void bind() {
		GL20.glUniform3f(getID(), vector.x, vector.y, vector.z);
	}
}
