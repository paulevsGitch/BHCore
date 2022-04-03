package paulevs.bhcore.rendering.shaders.buffers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import paulevs.bhcore.rendering.textures.Texture2D;

@Environment(EnvType.CLIENT)
public class TextureBuffer implements AutoCloseable {
	private Texture2D texture;
	private final int fbo;
	
	/**
	 * Create new texture buffer to use in shaders.
	 * Buffers are used to render data into them directly instead of rendering on screen.
	 * @param texture {@link Texture2D} to render buffer in.
	 */
	public TextureBuffer(Texture2D texture) {
		this.texture = texture;
		fbo = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture.getID(), 0);
		unbind();
	}
	
	/**
	 * Changes size of current buffer.
	 * @param width buffer width (X axis).
	 * @param height buffer height (Y axis).
	 */
	public void resize(int width, int height) {
		if (texture.getWidth() != width || texture.getHeight() != height) {
			texture.resize(width, height);
		}
	}
	
	/**
	 * Binds current buffer. All render calls after that will be applied to buffer.
	 */
	public void bind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
	}
	
	/**
	 * Unbinds any buffer (set binded buffer to zero)
	 */
	public static void unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	@Override
	public void close() throws Exception {
		GL30.glDeleteFramebuffers(fbo);
		texture.close();
	}
}
