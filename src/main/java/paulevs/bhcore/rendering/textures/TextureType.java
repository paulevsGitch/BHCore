package paulevs.bhcore.rendering.textures;

public enum TextureType {
	RED(0),
	RGBA(2),
	DEPTH(2),
	RGBA_16(4);
	
	private final int bitOffset;
	
	TextureType(int bitOffset) {
		this.bitOffset = bitOffset;
	}
	
	public int getCapacity(int elements) {
		return elements << bitOffset;
	}
}
