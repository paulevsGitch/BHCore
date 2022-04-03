package paulevs.bhcore.rendering.shaders.buffers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import paulevs.bhcore.rendering.shaders.ShaderProgram;
import paulevs.bhcore.rendering.textures.Texture2D;
import paulevs.bhcore.rendering.textures.TextureType;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class MultiBufferBuilder {
	private static final MultiBufferBuilder INSTANCE = new MultiBufferBuilder();
	
	private final List<String> textureNames = new ArrayList<>();
	private final List<Texture2D> textures = new ArrayList<>();
	private boolean hasDepth;
	private int height;
	private int width;
	
	private MultiBufferBuilder() {}
	
	/**
	 * Start process of {@link MultiBuffer} building.
	 * @param width buffer width (X axis).
	 * @param height buffer height (X axis).
	 * @return same {@link MultiBufferBuilder} instance.
	 */
	public static MultiBufferBuilder start(int width, int height) {
		INSTANCE.textureNames.clear();
		INSTANCE.textures.clear();
		INSTANCE.hasDepth = false;
		INSTANCE.height = height;
		INSTANCE.width = width;
		return INSTANCE;
	}
	
	/**
	 * Add depth map to buffer.
	 * @return same {@link MultiBufferBuilder} instance.
	 */
	public MultiBufferBuilder addDepthMap() {
		hasDepth = true;
		return this;
	}
	
	/**
	 * Add standard RGBA {@link Texture2D} to buffer.
	 * @param name texture name (same as uniform name in shader program).
	 * @return same {@link MultiBufferBuilder} instance.
	 */
	public MultiBufferBuilder addRGBTexture(String name) {
		return addTexture(name, new Texture2D(width, height, TextureType.RGBA));
	}
	
	/**
	 * Add {@link Texture2D} with specified {@link TextureType} to buffer.
	 * @param name texture name (same as uniform name in shader program).
	 * @param type desired {@link TextureType}.
	 * @return same {@link MultiBufferBuilder} instance.
	 */
	public MultiBufferBuilder addTexture(String name, TextureType type) {
		return addTexture(name, new Texture2D(width, height, type));
	}
	
	/**
	 * Add {@link Texture2D} to buffer.
	 * @param name texture name (same as uniform name in shader program).
	 * @param texture {@link Texture2D} to add.
	 * @return same {@link MultiBufferBuilder} instance.
	 */
	public MultiBufferBuilder addTexture(String name, Texture2D texture) {
		textures.add(texture);
		textureNames.add(name);
		return this;
	}
	
	/**
	 * Construct {@link MultiBuffer} and finish building process.
	 * @param program {@link ShaderProgram} to link with buffer.
	 * @return new {@link MultiBuffer} instance.
	 */
	public MultiBuffer build(ShaderProgram program) {
		String[] names = textureNames.toArray(new String[textureNames.size()]);
		Texture2D[] textures = this.textures.toArray(new Texture2D[this.textures.size()]);
		return new MultiBuffer(program, textures, names, hasDepth);
	}
}
