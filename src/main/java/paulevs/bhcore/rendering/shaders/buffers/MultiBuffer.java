package paulevs.bhcore.rendering.shaders.buffers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import paulevs.bhcore.rendering.shaders.ShaderProgram;
import paulevs.bhcore.rendering.shaders.uniforms.TextureUniform;
import paulevs.bhcore.rendering.textures.Texture2D;
import paulevs.bhcore.rendering.textures.TextureType;

import java.nio.IntBuffer;

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
	 * @param program {@link ShaderProgram} that will put its output into the buffer. Program uniforms will be linked to buffer textures using their names.
	 * @param textures array of {@link Texture2D} to output data into.
	 * @param textureNames array of {@link String} texture names to construct links between shader program output and textures.
	 * @param hasDepth if {@code true} buffer will write depth information (also allows GL_DEPTH_TEST).
	 */
	protected MultiBuffer(ShaderProgram program, Texture2D[] textures, String[] textureNames, boolean hasDepth) {
		drawBuffers = BufferUtils.createIntBuffer(textures.length);
		this.textures = textures;
		
		Texture2D depth = null;
		if (hasDepth) {
			Texture2D source = textures[0];
			depth = new Texture2D(source.getWidth(), source.getHeight(), TextureType.DEPTH);
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depth.getID(), 0);
		}
		this.depth = depth;
		
		for (byte i = 0; i < textures.length; i++) {
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
