package paulevs.bhcore.rendering.textures;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

/**
 * Texture filter enum, used to specify how texture will look like during rendering.
 * <p><b style="color:#579bf4;">{@code LINEAR}</b> - will result with blurry pixels (linear interpolation).</p>
 * <p><b style="color:#579bf4;">{@code NEAREST}</b> - will result with hard pixels.</p>
 */
@Environment(EnvType.CLIENT)
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
