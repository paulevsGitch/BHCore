package paulevs.bhcore.mixin;

import net.minecraft.level.Level;
import net.minecraft.level.LevelManager;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.level.HeightLimitView;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import paulevs.bhcore.interfaces.CoreChunkSection;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Mixin(value = LevelManager.class, priority = 2000)
public class LevelManagerMixin {
	private static final String BHC_SECTIONS_TAG = Identifier.of(MODID, "sections").toString();
	@Unique private static final String BHC_HEIGHT_KEY = "y";
	
	@Inject(
		method = "method_1479(Lnet/minecraft/level/Level;Lnet/minecraft/util/io/CompoundTag;)Lnet/minecraft/level/chunk/Chunk;",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/level/chunk/Chunk;tiles:[B",
			opcode = Opcodes.PUTFIELD,
			shift = At.Shift.AFTER
		),
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	private static void bhc_loadStationData(Level level, CompoundTag chunkTag, CallbackInfoReturnable<Chunk> info, int var2, int var3, Chunk chunk) {
		ChunkSection[] sections = ((ChunkSectionsAccessor) chunk).getSections();
		if (chunkTag.containsKey(BHC_SECTIONS_TAG)) {
			ListTag listTag = chunkTag.getListTag(BHC_SECTIONS_TAG);
			for (int i = 0; i < listTag.size(); i++) {
				CompoundTag sectionTag = (CompoundTag) listTag.get(i);
				int sectionY = sectionTag.getByte(BHC_HEIGHT_KEY);
				int index = ((HeightLimitView) level).sectionCoordToIndex(sectionY);
				CoreChunkSection.cast(sections[index]).bhc_loadData(sectionTag);
			}
		}
	}
	
	@Inject(
		method = "method_1480(Lnet/minecraft/level/chunk/Chunk;Lnet/minecraft/level/Level;Lnet/minecraft/util/io/CompoundTag;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/io/CompoundTag;put(Ljava/lang/String;[B)V",
			ordinal = 0,
			shift = At.Shift.AFTER
		)
	)
	private static void bhc_saveStationData(Chunk chunk, Level level, CompoundTag tag, CallbackInfo ci) {
		ChunkSection[] sections = ((ChunkSectionsAccessor) chunk).getSections();
		ListTag listTag = tag.getListTag(BHC_SECTIONS_TAG);
		for (int i = 0; i < listTag.size(); i++) {
			CompoundTag sectionTag = (CompoundTag) listTag.get(i);
			int sectionY = sectionTag.getByte(BHC_HEIGHT_KEY);
			int index = ((HeightLimitView) level).sectionCoordToIndex(sectionY);
			CoreChunkSection.cast(sections[index]).bhc_saveData(sectionTag);
		}
	}
}
