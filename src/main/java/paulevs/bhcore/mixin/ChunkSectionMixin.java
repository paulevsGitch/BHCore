package paulevs.bhcore.mixin;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import paulevs.bhcore.interfaces.CoreChunkSection;
import paulevs.bhcore.interfaces.CustomSectionData;
import paulevs.bhcore.storage.SectionDataHandler;

@Mixin(value = ChunkSection.class, remap = false)
public class ChunkSectionMixin implements CoreChunkSection {
	@Unique private CustomSectionData[] bhc_sectionData;
	
	@Inject(method = "<init>(SSSS)V", at = @At("TAIL"))
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
	}
	
	@Unique
	@Override
	public CustomSectionData bhc_getData(int index) {
		return bhc_sectionData[index];
	}
}
