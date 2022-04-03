package paulevs.bhcore.rendering.textures;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Texture wrapping enum, defines texture behaviour on UV values outside of range [0.0-1.0].
 * <p><b style="color:#579bf4;">{@code REPEAT}</b> - texture will repeat up to infinity.</p>
 * <p><b style="color:#579bf4;">{@code CLAMP}</b> - there will be no data outside of the texture.</p>
 */
@Environment(EnvType.CLIENT)
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
