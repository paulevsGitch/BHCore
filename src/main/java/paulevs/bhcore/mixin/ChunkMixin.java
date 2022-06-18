package paulevs.bhcore.mixin;

import net.minecraft.block.entity.BaseBlockEntity;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.maths.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bhcore.interfaces.IndependentTileEntity;

import java.util.Map;

@Mixin(Chunk.class)
public class ChunkMixin {
	@Shadow public Level level;
	@Shadow @Final public int x;
	@Shadow @Final public int z;
	@Shadow public Map blockEntities;
	
	@Inject(
		method = "setBlockEntity(IIILnet/minecraft/block/entity/BaseBlockEntity;)V",
		at = @At("HEAD"), cancellable = true
	)
	public void placeTileEntity(int x, int y, int z, BaseBlockEntity entity, CallbackInfo info) {
		if (entity instanceof IndependentTileEntity) {
			BlockPos tilePos = new BlockPos(x, y, z);
			entity.level = this.level;
			entity.x = this.x * 16 + x;
			entity.y = y;
			entity.z = this.z * 16 + z;
			entity.validate();
			this.blockEntities.put(tilePos, entity);
			info.cancel();
		}
	}
}
