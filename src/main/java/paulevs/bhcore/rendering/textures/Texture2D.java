package paulevs.bhcore.rendering.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Texture2D implements AutoCloseable {
	private final TextureType type;
	private final int textureID;
	
	private ByteBuffer buffer;
	private int height;
	private int width;

	public Texture2D(BufferedImage image) {
		textureID = GL11.glGenTextures();
		type = TextureType.RGBA;
		setTextureID(image);
	}
	
	public Texture2D(int width, int height, TextureType type) {
		textureID = GL11.glGenTextures();
		this.height = height;
		this.width = width;
		this.type = type;
		this.bind();
		this.setFilter(TextureFilter.NEAREST);
		this.setWrapMode(TextureWrapMode.CLAMP);
		this.update();
	}
	
	public void resize(int width, int height) {
		if (this.width != width || this.height != height) {
			this.height = height;
			this.width = width;
			this.bind();
			this.update();
		}
	}

	public void setTextureID(BufferedImage image) {
		int[] data = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), data, 0, image.getWidth());
		setTexture(data, image.getWidth(), image.getHeight());
		this.bind();
		this.setFilter(TextureFilter.NEAREST);
		this.setWrapMode(TextureWrapMode.CLAMP);
		this.update();
	}
	
	public void setTexture(int[] data, int width, int height) {
		initBuffer(data.length);
		for (int i = 0; i < data.length; i++) {
			buffer.putInt(data[i]);
		}
		buffer.flip();
		
		this.width = width;
		this.height = height;
		this.bind();
		this.setFilter(TextureFilter.NEAREST);
		this.setWrapMode(TextureWrapMode.CLAMP);
		this.update();
	}

	public Texture2D setWrapMode(TextureWrapMode mode) {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, mode.getID());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, mode.getID());
		return this;
	}
	
	public Texture2D setFilter(TextureFilter filter) {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter.getID());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter.getID());
		return this;
	}
	
	public Texture2D addMipMaps() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		return this;
	}
	
	public Texture2D update() {
		switch (type) {
			case RGBA:
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buffer);
				break;
			case DEPTH:
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, buffer);
				break;
			case RGBA_16:
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB16F, width, height, 0, GL12.GL_BGRA, GL11.GL_FLOAT, buffer);
				break;
			case RED:
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RED, width, height, 0, GL12.GL_BGRA, GL11.GL_FLOAT, buffer);
				break;
		}
		return this;
	}

	public Texture2D bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		return this;
	}
	
	public static void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	private void initBuffer(int size) {
		int capacity = type.getCapacity(size);
		if (buffer == null || buffer.capacity() != capacity) {
			buffer = ByteBuffer.allocateDirect(capacity);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		}
	}
	
	public int getID() {
		return textureID;
	}
	
	@Override
	public void close() throws Exception {
		GL11.glDeleteTextures(textureID);
	}
}
