package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.IntBuffer;

public class Vec3IUniform extends Uniform {
	private IntBuffer buffer = BufferUtils.createIntBuffer(3);
	
	public Vec3IUniform(int id) {
		super(id);
	}
	
	public void set(int x, int y, int z) {
		buffer.put(x);
		buffer.put(y);
		buffer.put(z);
		buffer.rewind();
	}
	
	@Override
	public void bind() {
		GL20.glUniform1(getID(), buffer);
	}
}
