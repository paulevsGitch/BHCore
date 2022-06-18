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
	
	/*@Inject(method = "<init>(SSSS)V", at = @At("TAIL"))
	private void bhc_onChunkSectionInit(short yOffset, short nonEmptyBlockCount, short randomTickableBlockCount, short nonEmptyFluidCount, CallbackInfo info) {
		bhc_sectionData = new CustomSectionData[SectionDataHandler.getCount()];
		for (int i = 0; i < bhc_sectionData.length; i++) {
			bhc_sectionData[i] = SectionDataHandler.getConstructor(i).get();
		}
	}
	
	@Inject(method = "toNBT()Lnet/minecraft/util/io/CompoundTag;", at = @At(
		value = "INVOKE",
		target = "Lnet/modificationstation/stationapi/impl/level/chunk/PalettedContainer;write(Lnet/minecraft/util/io/CompoundTag;Ljava/lang/String;Ljava/lang/String;)V"
	), locals = LocalCapture.CAPTURE_FAILHARD)
	public void bhc_onTagSave(CallbackInfoReturnable<CompoundTag> info, CompoundTag tag) {
		for (int index = 0; index < bhc_sectionData.length; index++) {
			String key = SectionDataHandler.getKey(index);
			bhc_sectionData[index].saveToBNT(key, tag);
		}
	}
	
	@Inject(method = "fromNBT(Lnet/minecraft/util/io/CompoundTag;Lnet/minecraft/util/io/CompoundTag;)V", at = @At("HEAD"))
	public void bhc_onTagLoad(CompoundTag chunkTag, CompoundTag sectionTag, CallbackInfo info) {
		for (int index = 0; index < bhc_sectionData.length; index++) {
			String key = SectionDataHandler.getKey(index);
			bhc_sectionData[index].loadFromBNT(key, sectionTag);
		}
	}*/
	
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
