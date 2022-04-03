package paulevs.bhcore.rendering.shaders.buffers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import paulevs.bhcore.rendering.textures.Texture2D;

public class TextureBuffer implements AutoCloseable {
	private final Texture2D texture;
	private final int fbo;
	
	public TextureBuffer(Texture2D texture, int width, int height) {
		this.texture = texture;
		fbo = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture.getID(), 0);
		unbind();
	}
	
	public void resize(int width, int height) {
		texture.resize(width, height);
	}
	
	public void bind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
	}
	
	public static void unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	@Override
	public void close() throws Exception {
		GL30.glDeleteFramebuffers(fbo);
		texture.close();
	}
}
