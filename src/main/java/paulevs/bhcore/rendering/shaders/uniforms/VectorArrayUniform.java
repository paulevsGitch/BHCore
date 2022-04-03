package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.opengl.GL20;
import paulevs.bhcore.storage.vector.Vec3F;

public class VectorArrayUniform extends FloatBufferUniform {
	public VectorArrayUniform(int id) {
		super(id);
	}
	
	public void setValues(Vec3F[] values) {
		float[] array = new float[values.length * 3];
		for (int i = 0; i < values.length; i++) {
			int index = i * 3;
			array[index++] = values[i].x;
			array[index++] = values[i].y;
			array[index] = values[i].z;
		}
		super.setValues(array);
	}
	
	@Override
	public void bind() {
		GL20.glUniform3(getID(), buffer);
	}
}
