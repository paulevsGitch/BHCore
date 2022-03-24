package paulevs.bhcore.util;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.impl.level.StationDimension;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import paulevs.bhcore.interfaces.CoreChunkSection;

public class WorldUtil {
	public static void setDestruction(Level level, int x, int y, int z, int destruction) {
		if (y < 0 || y >= getLevelHeight(level)) {
			return;
		}
		ChunkSectionsAccessor accessor = ChunkSectionsAccessor.class.cast(level.getChunk(x >> 4, z >> 4));
		ChunkSection[] sections = accessor.getSections();
		int index = y >> 4;
		ChunkSection section = sections[index];
		if (section == null) {
			return;
		}
		CoreChunkSection coreChunkSection = CoreChunkSection.class.cast(section);
		coreChunkSection.setDestruction(x & 15, y & 15, z & 15, destruction);
	}
	
	public static int getDestruction(Level level, int x, int y, int z) {
		if (y < 0 || y >= getLevelHeight(level)) {
			return 0;
		}
		ChunkSectionsAccessor accessor = ChunkSectionsAccessor.class.cast(level.getChunk(x >> 4, z >> 4));
		ChunkSection[] sections = accessor.getSections();
		int index = y >> 4;
		ChunkSection section = sections[index];
		if (section == null) {
			return 0;
		}
		CoreChunkSection coreChunkSection = CoreChunkSection.class.cast(section);
		return coreChunkSection.getDestruction(x & 15, y & 15, z & 15);
	}
	
	public static int getLevelHeight(Level level) {
		StationDimension dimension = StationDimension.class.cast(level.dimension);
		return dimension.getActualLevelHeight();
	}
}
