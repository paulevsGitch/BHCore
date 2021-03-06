package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class FloatBufferUniform extends Uniform {
	protected FloatBuffer buffer;
	protected int size;
	
	public FloatBufferUniform(int id) {
		super(id);
	}
	
	public void setValues(float[] values) {
		if (buffer == null || size != values.length) {
			buffer = BufferUtils.createFloatBuffer(values.length);
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
