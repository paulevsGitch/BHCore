package paulevs.bhcore.rendering.shaders.buffers;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import paulevs.bhcore.rendering.shaders.ShaderProgram;
import paulevs.bhcore.rendering.shaders.uniforms.TextureUniform;
import paulevs.bhcore.rendering.textures.Texture2D;
import paulevs.bhcore.rendering.textures.TextureType;

import java.nio.IntBuffer;

public class MultiBuffer implements AutoCloseable {
	private final IntBuffer drawBuffers;
	private final Texture2D[] textures;
	private final Texture2D depth;
	private final int fbo;
	
	private TextureUniform[] uniforms;
	
	protected MultiBuffer(int width, int height, boolean hasDepth, Texture2D[] textures, String[] textureNames, ShaderProgram program) {
		drawBuffers = BufferUtils.createIntBuffer(textures.length);
		this.textures = textures;
		
		fbo = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		
		this.depth = hasDepth ? new Texture2D(width, height, TextureType.DEPTH) : null;
		if (hasDepth) {
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depth.getID(), 0);
		}
		
		for (int i = 0; i < textures.length; i++) {
			int attachment = GL30.GL_COLOR_ATTACHMENT0 + i;
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, textures[i].getID(), 0);
			drawBuffers.put(attachment);
		}
		drawBuffers.flip();
		
		if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("Can't create a FrameBuffer (MultiBuffer): " + GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER));
		}
		
		GL20.glDrawBuffers(drawBuffers);
		unbind();
		
		uniforms = new TextureUniform[textures.length];
		for (int i = 0; i < uniforms.length; i++) {
			uniforms[i] = program.getUniform(textureNames[i], TextureUniform::new);
			uniforms[i].setTexture(textures[i]);
		}
	}
	
	public void resize(int width, int height) {
		if (depth != null) {
			depth.resize(width, height);
		}
		for (Texture2D texture: textures) {
			texture.resize(width, height);
		}
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
		for (Texture2D texture: textures) {
			texture.close();
		}
	}
}
