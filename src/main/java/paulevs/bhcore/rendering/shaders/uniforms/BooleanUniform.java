package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.opengl.GL20;

public class BooleanUniform extends Uniform {
	private boolean value;
	
	public BooleanUniform(int id) {
		super(id);
	}
	
	public void setValue(boolean value) {
		this.value = value;
	}
	
	@Override
	public void bind() {
		GL20.glUniform1i(getID(), value ? 1 : 0);
	}
}
