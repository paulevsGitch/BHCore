package paulevs.bhcore.rendering.shaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Vec3i;
import paulevs.bhcore.rendering.textures.Texture2D;
import paulevs.bhcore.rendering.textures.TextureFilter;
import paulevs.bhcore.rendering.textures.TextureType;
import paulevs.bhcore.rendering.textures.TextureWrapMode;
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
		this.dataSide = MathUtil.getClosestPowerOfTwo(data.length);
		texture = new Texture2D(dataSide, dataSide, TextureType.RED);
		texture.setFilter(TextureFilter.NEAREST);
		texture.setWrapMode(TextureWrapMode.CLAMP);
	}
	
	private class ShaderSectionData {
		Vec3i position = new Vec3i();
		
	}
}
