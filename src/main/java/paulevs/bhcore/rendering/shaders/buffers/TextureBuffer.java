package paulevs.bhcore.rendering.shaders.buffers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import paulevs.bhcore.rendering.textures.Texture2D;

@Environment(EnvType.CLIENT)
public class TextureBuffer extends FrameBuffer {
	private Texture2D texture;
	
	/**
	 * Create new texture buffer to use in shaders.
	 * Buffers are used to render data into them directly instead of rendering on screen.
	 * @param texture {@link Texture2D} to render buffer in.
	 */
	public TextureBuffer(Texture2D texture) {
		this.texture = texture;
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture.getID(), 0);
		checkStatus();
		GL11.glDrawBuffer(GL11.GL_NONE);
		GL11.glReadBuffer(GL11.GL_NONE);
		unbind();
	}
	
	@Override
	public void resize(int width, int height) {
		if (texture.getWidth() != width || texture.getHeight() != height) {
			texture.resize(width, height);
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		texture.dispose();
	}
}
