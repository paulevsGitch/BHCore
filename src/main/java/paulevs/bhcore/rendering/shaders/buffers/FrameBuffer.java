package paulevs.bhcore.rendering.shaders.buffers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL30;
import paulevs.bhcore.interfaces.Disposable;
import paulevs.bhcore.util.DisposeUtil;

@Environment(EnvType.CLIENT)
public abstract class FrameBuffer implements Disposable {
	private final int fbo;
	
	/**
	 * Create new frame buffer.
	 * Frame buffers are used to render data into them directly instead of rendering on screen.
	 */
	public FrameBuffer() {
		unbind();
		fbo = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		DisposeUtil.addObject(this);
		bind();
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
	public void dispose() {
		GL30.glDeleteFramebuffers(fbo);
	}
	
	/**
	 * Check framebuffer status and throw an exception.
	 */
	protected void checkStatus() {
		int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
		if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
			String error = "";
			switch (status) {
				case GL30.GL_FRAMEBUFFER_UNSUPPORTED -> error = "GL_FRAMEBUFFER_UNSUPPORTED";
				case GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT -> error = "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT";
				case GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT -> error = "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT";
				case GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER -> error = "GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER";
				case GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE -> error = "GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE";
				case GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER -> error = "GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER";
			}
			if (error.isEmpty()) error = Integer.toString(status);
			throw new RuntimeException("Can't create a FrameBuffer (" + getClass().getName() + "): " + error);
		}
	}
}
