package paulevs.bhcore.rendering.shaders.buffers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import paulevs.bhcore.rendering.shaders.ShaderProgram;
import paulevs.bhcore.rendering.shaders.uniforms.TextureUniform;
import paulevs.bhcore.rendering.textures.Texture2D;

import java.nio.IntBuffer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Environment(EnvType.CLIENT)
public class MultiBuffer extends FrameBuffer {
	private final TextureUniform[] uniforms;
	private final Texture2D[] textures;
	private final IntBuffer drawBuffers;
	private final Texture2D depth;
	
	/**
	 * Create new multiple buffer for multiple textures.
	 * Buffers are used to render data into them directly instead of rendering on screen.
	 * MultiBuffer allows output more different values from shader program.
	 * @param textures map of {@link Texture2D} with names to output data into.
	 * @param depthTexture {@link Texture2D} to use as depth map. Can be null.
	 * @param program {@link ShaderProgram} that will put its output into the buffer.
	 * Program uniforms will be linked to buffer textures using their names. Can be null.
	 */
	protected MultiBuffer(Map<String, Texture2D> textures, Texture2D depthTexture, ShaderProgram program) {
		final int size = textures.size();
		this.textures = textures.values().toArray(new Texture2D[size]);
		this.drawBuffers = size > 0 ? BufferUtils.createIntBuffer(size) : null;
		this.uniforms = new TextureUniform[size];
		this.depth = depthTexture;
		
		if (depthTexture != null) {
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture.getID(), 0);
		}
		
		if (size > 0) {
			for (byte i = 0; i < size; i++) {
				int attachment = GL30.GL_COLOR_ATTACHMENT0 + i;
				GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, this.textures[i].getID(), 0);
				drawBuffers.put(attachment);
			}
			drawBuffers.flip();
		}
		
		if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("Can't create a FrameBuffer (MultiBuffer): " + GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER));
		}
		
		if (size > 0 && program != null) {
			GL20.glDrawBuffers(this.drawBuffers);
			AtomicInteger idex = new AtomicInteger();
			textures.forEach((name, texture) -> {
				int i = idex.getAndIncrement();
				this.uniforms[i] = program.getUniform(name, TextureUniform::new);
				this.uniforms[i].setTexture(texture);
			});
		}
		
		unbind();
	}
	
	@Nullable
	public Texture2D getDepth() {
		return depth;
	}
	
	@Override
	public void resize(int width, int height) {
		if (depth != null) {
			depth.resize(width, height);
		}
		for (Texture2D texture: textures) {
			texture.resize(width, height);
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if (depth != null) {
			depth.dispose();
		}
		for (Texture2D texture: textures) {
			texture.dispose();
		}
	}
}
