package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.opengl.GL20;

public class IntUniform extends Uniform {
	protected int value;
	
	public IntUniform(int id) {
		super(id);
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public void bind() {
		GL20.glUniform1i(getID(), value);
	}
}
