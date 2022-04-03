package paulevs.bhcore.rendering.shaders.buffers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL30;

@Environment(EnvType.CLIENT)
public abstract class FrameBuffer implements AutoCloseable {
	private final int fbo;
	
	/**
	 * Create new frame buffer.
	 * Frame buffers are used to render data into them directly instead of rendering on screen.
	 */
	public FrameBuffer() {
		fbo = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
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
	
	/**
	 * Changes size of current buffer.
	 * @param width buffer width (X axis).
	 * @param height buffer height (Y axis).
	 */
	public abstract void resize(int width, int height);
	
	@Override
	public void close() {
		GL30.glDeleteFramebuffers(fbo);
	}
}
