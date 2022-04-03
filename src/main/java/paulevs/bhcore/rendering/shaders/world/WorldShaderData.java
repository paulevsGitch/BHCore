package paulevs.bhcore.rendering.shaders.world;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.maths.MathHelper;
import paulevs.bhcore.rendering.textures.Texture2D;
import paulevs.bhcore.rendering.textures.TextureFilter;
import paulevs.bhcore.rendering.textures.TextureType;
import paulevs.bhcore.rendering.textures.TextureWrapMode;
import paulevs.bhcore.storage.vector.Vec3I;
import paulevs.bhcore.util.MathUtil;

@Environment(EnvType.CLIENT)
public class WorldShaderData {
	private final ShaderSectionData[] data;
	private final Texture2D texture;
	private final int dataHeight;
	private final int dataWidth;
	private final int dataSide;
	
	public WorldShaderData(int dataWidth, int dataHeight) {
		this.data = new ShaderSectionData[dataWidth * dataWidth * dataHeight];
		this.dataHeight = dataHeight;
		this.dataWidth = dataWidth;
		this.dataSide = (int) Math.ceil(MathHelper.sqrt(data.length));
		int textureSide = MathUtil.getClosestPowerOfTwo(dataSide << 6);
		texture = new Texture2D(textureSide, textureSide, TextureType.RED);
		texture.setFilter(TextureFilter.NEAREST);
		texture.setWrapMode(TextureWrapMode.CLAMP);
	}
	
	public void setData(int chunkX, int chunkZ, int section, int x, int y, int z, int value) {
		int index = getIndex(chunkX, section, chunkZ);
		ShaderSectionData shaderSection = data[index];
		
	}
	
	public void updateSection(int chunkX, int chunkZ, int section) {
	
	}
	
	private int getIndex(int x, int y, int z) {
		return ((x * dataWidth) + y * dataWidth) + z;
	}
	
	private class ShaderSectionData {
		Vec3I position = new Vec3I();
	}
}
