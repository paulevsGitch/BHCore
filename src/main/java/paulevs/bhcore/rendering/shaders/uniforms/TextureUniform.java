package paulevs.bhcore.rendering.shaders.uniforms;

import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import paulevs.bhcore.rendering.textures.Texture2D;

public class TextureUniform extends Uniform {
	private Texture2D texture;
	
	public TextureUniform(int id) {
		super(id);
		GL20.glUniform1i(id, id);
	}
	
	public void setTexture(Texture2D texture) {
		this.texture = texture;
	}
	
	public Texture2D getTexture() {
		return texture;
	}
	
	public void bind() {
		if (getID() >= 0) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + getID());
			texture.bind();
		}
	}
}
