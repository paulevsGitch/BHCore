package paulevs.bhcore.mixin;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import paulevs.bhcore.interfaces.CoreChunkSection;
import paulevs.bhcore.interfaces.CustomSectionData;
import paulevs.bhcore.storage.section.SectionDataHandler;

@Mixin(value = ChunkSection.class)
public class ChunkSectionMixin implements CoreChunkSection {
	@Unique private CustomSectionData[] bhc_sectionData;
	
	@Unique
	@Override
	public CustomSectionData bhc_getData(int index) {
		bhc_initData();
		CustomSectionData data = bhc_sectionData[index];
		if (data == null) {
			data = SectionDataHandler.getConstructor(index).get();
			bhc_sectionData[index] = data;
		}
		return data;
	}
	
	@Unique
	@Override
	public void bhc_saveData(CompoundTag tag) {
		if (bhc_sectionData == null) return;
		for (int index = 0; index < bhc_sectionData.length; index++) {
			String key = SectionDataHandler.getKey(index);
			bhc_sectionData[index].saveToBNT(key, tag);
		}
	}
	
	@Override
	public void bhc_loadData(CompoundTag tag) {
		bhc_initData();
		for (int index = 0; index < bhc_sectionData.length; index++) {
			String key = SectionDataHandler.getKey(index);
			bhc_sectionData[index].loadFromBNT(key, tag);
		}
	}
	
	@Unique
	private void bhc_initData() {
		if (bhc_sectionData == null) bhc_sectionData = new CustomSectionData[SectionDataHandler.getCount()];
	}
}
