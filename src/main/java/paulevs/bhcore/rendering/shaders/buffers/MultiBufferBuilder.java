package paulevs.bhcore.rendering.shaders.buffers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import paulevs.bhcore.rendering.shaders.ShaderProgram;
import paulevs.bhcore.rendering.textures.Texture2D;
import paulevs.bhcore.rendering.textures.TextureType;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class MultiBufferBuilder {
	private static MultiBufferBuilder instance;
	
	private final Map<String, Texture2D> textures = new HashMap<>();
	private Texture2D depth;
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
		if (instance == null) instance = new MultiBufferBuilder();
		instance.textures.clear();
		instance.depth = null;
		instance.height = height;
		instance.width = width;
		return instance;
	}
	
	/**
	 * Add depth map to buffer.
	 * @return same {@link MultiBufferBuilder} instance.
	 */
	public MultiBufferBuilder addDepthMap() {
		depth = new Texture2D(width, height, TextureType.DEPTH);
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
		if (textures.containsKey(name)) throw new RuntimeException("Texture with name " + name + " already exists!");
		textures.put(name, texture);
		return this;
	}
	
	/**
	 * Construct {@link MultiBuffer} and finish building process.
	 * @param program {@link ShaderProgram} to link with buffer.
	 * @return new {@link MultiBuffer} instance.
	 */
	public MultiBuffer build(ShaderProgram program) {
		return new MultiBuffer(textures, depth, program);
	}
	
	/**
	 * Construct {@link MultiBuffer} and finish building process.
	 * @return new {@link MultiBuffer} instance.
	 */
	public MultiBuffer build() {
		return build(null);
	}
}
