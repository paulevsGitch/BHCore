package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class FloatBufferUniform extends Uniform {
	protected FloatBuffer buffer;
	
	public FloatBufferUniform(int id) {
		super(id);
	}
	
	public void setValues(float[] values) {
		buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
	}
	
	@Override
	public void bind() {
		GL20.glUniform1(getID(), buffer);
	}
}
