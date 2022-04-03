package paulevs.bhcore.rendering.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public enum TextureWrapMode {
	REPEAT(GL11.GL_REPEAT),
	CLAMP(GL12.GL_CLAMP_TO_EDGE);
	
	private final int id;
	
	TextureWrapMode(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
}
