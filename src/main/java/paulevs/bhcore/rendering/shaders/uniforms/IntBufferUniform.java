package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class IntBufferUniform extends Uniform {
	protected IntBuffer buffer;
	protected int size;
	
	public IntBufferUniform(int id) {
		super(id);
	}
	
	public void setValues(int[] values) {
		if (buffer == null || size != values.length) {
			buffer = BufferUtils.createIntBuffer(values.length);
			size = values.length;
		}
		buffer.put(values);
		buffer.flip();
	}
	
	@Override
	public void bind() {
		GL20.glUniform1(getID(), buffer);
	}
}
