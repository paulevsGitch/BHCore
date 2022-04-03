package paulevs.bhcore.rendering.textures;

import org.lwjgl.opengl.GL11;

public enum TextureFilter {
	LINEAR(GL11.GL_LINEAR),
	NEAREST(GL11.GL_NEAREST);
	
	private final int id;
	
	TextureFilter(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
}
