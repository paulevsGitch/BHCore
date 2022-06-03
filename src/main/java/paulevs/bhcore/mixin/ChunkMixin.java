package paulevs.bhcore.mixin;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.TilePos;
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
	@Shadow public Map field_964;
	
	@Inject(
		method = "placeTileEntity(IIILnet/minecraft/tileentity/TileEntityBase;)V",
		at = @At("HEAD"), cancellable = true
	)
	public void placeTileEntity(int x, int y, int z, TileEntityBase entity, CallbackInfo info) {
		if (entity instanceof IndependentTileEntity) {
			TilePos tilePos = new TilePos(x, y, z);
			entity.level = this.level;
			entity.x = this.x * 16 + x;
			entity.y = y;
			entity.z = this.z * 16 + z;
			entity.validate();
			this.field_964.put(tilePos, entity);
			info.cancel();
		}
	}
}
