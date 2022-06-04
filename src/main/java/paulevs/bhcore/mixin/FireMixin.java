package paulevs.bhcore.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Fire;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bhcore.util.BlocksUtil;
import paulevs.bhcore.util.BurnableUtil;

import java.util.Random;

@Mixin(Fire.class)
public class FireMixin {
	@Inject(method = "method_1824(Lnet/minecraft/level/BlockView;III)Z", at = @At("HEAD"), cancellable = true)
	private void bhc_method_1824(BlockView world, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
		int speed = BurnableUtil.getBurnSpeed(BlocksUtil.getBlockState(world, x, y, z));
		if (speed > 0) info.setReturnValue(true);
	}
	
	@Inject(method = "method_1825(Lnet/minecraft/level/Level;IIII)I", at = @At("HEAD"), cancellable = true)
	private void bhc_method_1825(Level level, int x, int y, int z, int val, CallbackInfoReturnable<Integer> info) {
		int speed = BurnableUtil.getBurnSpeed(BlocksUtil.getBlockState(level, x, y, z));
		if (speed > 0) info.setReturnValue(speed > val ? speed : val);
	}
	
	@Inject(method = "fireTick(Lnet/minecraft/level/Level;IIIILjava/util/Random;I)V", at = @At("HEAD"), cancellable = true)
	private void bhc_fireTick(Level level, int x, int y, int z, int maxVal, Random random, int metaOffset, CallbackInfo info) {
		int delay = BurnableUtil.getSpreadDelay(BlocksUtil.getBlockState(level, x, y, z));
		if (delay > 0) {
			info.cancel();
			if (random.nextInt(maxVal) < delay) {
				if (random.nextInt(metaOffset + 10) < 5 && !level.canRainAt(x, y, z)) {
					int meta = metaOffset + random.nextInt(5) / 4;
					if (meta > 15) {
						meta = 15;
					}
					level.placeBlockWithMetaData(x, y, z, BlockBase.FIRE.id, meta);
				}
				else {
					level.setTile(x, y, z, 0);
				}
				if (level.getTileId(x, y, z) == BlockBase.TNT.id) {
					BlockBase.TNT.activate(level, x, y, z, 1);
				}
			}
		}
	}
}
