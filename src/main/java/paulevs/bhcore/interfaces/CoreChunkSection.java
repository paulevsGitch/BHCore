package paulevs.bhcore.interfaces;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;

public interface CoreChunkSection {
	CustomSectionData bhc_getData(int index);
	void bhc_saveData(CompoundTag tag);
	void bhc_loadData(CompoundTag tag);
	
	static CoreChunkSection cast(ChunkSection section) {
		return (CoreChunkSection) section;
	}
}
