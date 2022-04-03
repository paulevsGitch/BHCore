package paulevs.bhcore.rendering.shaders;

import org.lwjgl.opengl.GL20;

public enum ShaderType {
	VERTEX(GL20.GL_VERTEX_SHADER),
	FRAGMENT(GL20.GL_FRAGMENT_SHADER);
	
	private final int id;
	
	ShaderType(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
}
