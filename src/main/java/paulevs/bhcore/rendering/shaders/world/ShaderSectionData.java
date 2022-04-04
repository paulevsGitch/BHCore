package paulevs.bhcore.rendering.shaders.world;

import paulevs.bhcore.storage.vector.Vec3I;

public class ShaderSectionData {
	private final Vec3I position = new Vec3I(0, -1000, 0);
	private final byte data[] = new byte[4096];
	
	public boolean hasCorrectPosition(int x, int y, int z) {
		return x == position.x && y == position.y && z == position.z;
	}
	
	public void setPosition(int x, int y, int z) {
		position.set(x, y, z);
	}
	
	public byte[] getData() {
		return data;
	}
}
