package paulevs.bhcore.rendering.shaders.buffers;

import paulevs.bhcore.rendering.shaders.ShaderProgram;
import paulevs.bhcore.rendering.textures.Texture2D;
import paulevs.bhcore.rendering.textures.TextureType;

import java.util.ArrayList;
import java.util.List;

public class MultiBufferBuilder {
	private static final MultiBufferBuilder INSTANCE = new MultiBufferBuilder();
	
	private List<String> textureNames = new ArrayList<>();
	private List<Texture2D> textures = new ArrayList<>();
	private boolean hasDepth;
	private int height;
	private int width;
	
	private MultiBufferBuilder() {}
	
	public static MultiBufferBuilder start(int width, int height) {
		INSTANCE.textureNames.clear();
		INSTANCE.textures.clear();
		INSTANCE.hasDepth = false;
		INSTANCE.height = height;
		INSTANCE.width = width;
		return INSTANCE;
	}
	
	public MultiBufferBuilder addDepthMap() {
		hasDepth = true;
		return this;
	}
	
	public MultiBufferBuilder addRGBTexture(String name) {
		return addRGBTexture(name, new Texture2D(width, height, TextureType.RGBA));
	}
	
	public MultiBufferBuilder addRGBTexture(String name, TextureType type) {
		return addRGBTexture(name, new Texture2D(width, height, type));
	}
	
	public MultiBufferBuilder addRGBTexture(String name, Texture2D texture) {
		textures.add(texture);
		textureNames.add(name);
		return this;
	}
	
	public MultiBuffer build(ShaderProgram program) {
		String[] names = textureNames.toArray(new String[textureNames.size()]);
		Texture2D[] textures = this.textures.toArray(new Texture2D[this.textures.size()]);
		return new MultiBuffer(width, height, hasDepth, textures, names, program);
	}
}
