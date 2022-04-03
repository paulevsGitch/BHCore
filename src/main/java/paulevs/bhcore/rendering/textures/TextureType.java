package paulevs.bhcore.rendering.textures;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import paulevs.bhcore.interfaces.PentaConsumer;
import uk.co.benjiweber.expressions.function.TriConsumer;

import java.nio.ByteBuffer;

/**
 * Texture type enum, specifies texture data storage.
 * <p><b style="color:#579bf4;">{@code RED}</b> - single-channel (red) texture. Store data in floats, use single byte.</p>
 * <p><b style="color:#579bf4;">{@code RGBA}</b> - standard texture format. Store values as RGBA data in BGRA order.</p>
 * <p><b style="color:#579bf4;">{@code DEPTH}</b> - depth texture format. Store data in floats.</p>
 * <p><b style="color:#579bf4;">{@code RGBA_16}</b> - float RGBA format with double data. Store values as RGBA data in BGRA order.</p>
 */
@Environment(EnvType.CLIENT)
public enum TextureType {
	RED(
		0,
		(width, height, buffer) -> GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RED, width, height, 0, GL12.GL_BGRA, GL11.GL_FLOAT, buffer),
		(x, y, width, height, buffer) -> GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, width, height, GL12.GL_BGRA, GL11.GL_FLOAT, buffer)),
	RGBA(
		2,
		(width, height, buffer) -> GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buffer),
		(x, y, width, height, buffer) -> GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buffer)
	),
	DEPTH(
		2,
		(width, height, buffer) -> GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, buffer),
		(x, y, width, height, buffer) -> GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, width, height, GL12.GL_BGRA, GL11.GL_FLOAT, buffer)
	),
	RGBA_16(
		4,
		(width, height, buffer) -> GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB16F, width, height, 0, GL12.GL_BGRA, GL11.GL_FLOAT, buffer),
		(x, y, width, height, buffer) -> GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, width, height, GL12.GL_BGRA, GL11.GL_FLOAT, buffer)
	);
	
	private final PentaConsumer<Integer, Integer, Integer, Integer, ByteBuffer> genSubImage;
	private final TriConsumer<Integer, Integer, ByteBuffer> genFunction;
	private final int bitOffset;
	
	TextureType(int bitOffset, TriConsumer<Integer, Integer, ByteBuffer> genFunction, PentaConsumer<Integer, Integer, Integer, Integer, ByteBuffer> genSubImage) {
		this.genSubImage = genSubImage;
		this.genFunction = genFunction;
		this.bitOffset = bitOffset;
	}
	
	public int getCapacity(int elements) {
		return elements << bitOffset;
	}
	
	public void genTexture(int width, int height, ByteBuffer buffer) {
		genFunction.accept(width, height, buffer);
	}
	
	public void genSubImage(int x, int y, int width, int height, ByteBuffer buffer) {
		genSubImage.accept(x, y, width, height, buffer);
	}
}
