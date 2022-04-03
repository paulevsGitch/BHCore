package paulevs.bhcore.rendering.shaders;

import net.minecraft.util.Vec3i;

public class WorldShaderData {
	private final ShaderSectionData[] data;
	private final int dataHeight;
	private final int dataWidth;
	
	public WorldShaderData(int dataWidth, int dataHeight) {
		this.data = new ShaderSectionData[dataWidth * dataWidth * dataHeight];
		this.dataHeight = dataHeight;
		this.dataWidth = dataWidth;
	}
	
	private class ShaderSectionData {
		Vec3i position = new Vec3i();
		
	}
}
