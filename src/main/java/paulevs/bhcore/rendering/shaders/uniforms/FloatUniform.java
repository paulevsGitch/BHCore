package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.opengl.GL20;

public class FloatUniform extends Uniform {
	protected float value;
	
	public FloatUniform(int id) {
		super(id);
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
	@Override
	public void bind() {
		GL20.glUniform1f(getID(), value);
	}
}
