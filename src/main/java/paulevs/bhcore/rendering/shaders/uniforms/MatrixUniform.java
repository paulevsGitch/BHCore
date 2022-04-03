package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.opengl.GL20;
import paulevs.bhcore.storage.Matrix4x4;

public class MatrixUniform extends Uniform {
	private Matrix4x4 matrix;
	
	public MatrixUniform(int id) {
		super(id);
	}
	
	public void setMatrix(Matrix4x4 matrix) {
		this.matrix = matrix;
	}
	
	@Override
	public void bind() {
		GL20.glUniformMatrix4(getID(), false, matrix.getBuffer());
	}
}
