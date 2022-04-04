package paulevs.bhcore.rendering.shaders.world;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.impl.level.StationDimension;
import paulevs.bhcore.rendering.textures.Texture2D;
import paulevs.bhcore.rendering.textures.TextureFilter;
import paulevs.bhcore.rendering.textures.TextureType;
import paulevs.bhcore.rendering.textures.TextureWrapMode;
import paulevs.bhcore.storage.vector.Vec2F;
import paulevs.bhcore.storage.vector.Vec3I;
import paulevs.bhcore.util.MathUtil;
import uk.co.benjiweber.expressions.function.TriConsumer;

@Environment(EnvType.CLIENT)
public class WorldShaderData {
	private static final Vec3I POS = new Vec3I();
	
	private final TriConsumer<Level, Vec3I, byte[]> updateFunction;
	private final ShaderSectionData[][][] data;
	private final Vec3I lastCenter = new Vec3I();
	private final Texture2D texture;
	private final int dataHeight;
	private final int halfHeight;
	private final int dataWidth;
	private final int halfWidth;
	private final int dataSide;
	
	public WorldShaderData(int dataWidth, int dataHeight, TriConsumer<Level, Vec3I, byte[]> updateFunction) {
		this.data = new ShaderSectionData[dataWidth][dataHeight][dataWidth];
		this.updateFunction = updateFunction;
		this.halfHeight = dataHeight >> 1;
		this.halfWidth = dataWidth >> 1;
		
		for (int x = 0; x < dataWidth; x++) {
			for (int y = 0; y < dataHeight; y++) {
				for (int z = 0; z < dataWidth; z++) {
					data[x][y][z] = new ShaderSectionData();
				}
			}
		}
		
		this.dataSide = (int) Math.ceil(MathHelper.sqrt(dataWidth * dataWidth * dataHeight));
		int textureSide = MathUtil.getClosestPowerOfTwo(dataSide << 6);
		texture = new Texture2D(textureSide, textureSide, TextureType.RED);
		texture.setFilter(TextureFilter.NEAREST);
		texture.setWrapMode(TextureWrapMode.CLAMP);
		this.dataHeight = dataHeight;
		this.dataWidth = dataWidth;
	}
	
	public Texture2D getTexture() {
		return texture;
	}
	
	public int getDataWidth() {
		return dataWidth;
	}
	
	public int getDataHalfWidth() {
		return halfWidth;
	}
	
	public int getDataHeight() {
		return dataWidth;
	}
	
	public int getDataHalfHeight() {
		return halfHeight;
	}
	
	public int getDataSide() {
		return dataSide;
	}
	
	public void setTexturePosition(int x, int y, int z, Vec2F position) {
		int indexX = MathUtil.wrap(x, dataWidth);
		int indexY = MathUtil.wrap(y, dataHeight);
		int indexZ = MathUtil.wrap(z, dataWidth);
		int index = ((indexX * dataWidth) + indexY) * dataWidth + indexZ;
		position.x = (float) ((index % dataSide) << 6) / texture.getWidth();
		position.y = (float) ((index / dataSide) << 6) / texture.getWidth();
	}
	
	public void updateSection(Level level, int x, int y, int z) {
		int indexX = MathUtil.wrap(x, dataWidth);
		int indexY = MathUtil.wrap(y, dataHeight);
		int indexZ = MathUtil.wrap(z, dataWidth);
		ShaderSectionData section = data[indexX][indexY][indexZ];
		if (!section.hasCorrectPosition(x, y, z)) {
			section.setPosition(x, y, z);
			byte[] sectionData = section.getData();
			updateFunction.accept(level, POS.set(x, y, z), sectionData);
			int index = ((indexX * dataWidth) + indexY) * dataWidth + indexZ;
			int textureX = (index % dataSide) << 6;
			int textureY = (index / dataSide) << 6;
			texture.setArea(sectionData, textureX, textureY, 64, 64);
		}
	}
	
	public void update(PlayerBase player) {
		Level level = player.level;
		short height = StationDimension.class.cast(level.dimension).getSectionCount();
		int cy = ((int) player.y) >> 4;
		int cx = player.chunkX;
		int cz = player.chunkZ;
		if (lastCenter.x == cx && lastCenter.y == cy && lastCenter.z == cz) return;
		lastCenter.set(cx, cy, cz);
		for (int i = 0; i < dataWidth; i++) {
			int px = cx - halfWidth + i;
			for (int j = 0; j < dataWidth; j++) {
				int pz = cz - halfWidth + j;
				for (int k = 0; k < dataHeight; k++) {
					int py = cy - halfHeight + k;
					if (py < 0 || py >= height) continue;
					updateSection(level, px, py, pz);
				}
			}
		}
	}
}
