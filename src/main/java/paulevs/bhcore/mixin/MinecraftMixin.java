package paulevs.bhcore.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bhcore.util.DisposeUtil;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(method = "stop()V", at = @At("HEAD"))
	private void bhc_onGameStop(CallbackInfo info) {
		DisposeUtil.dispose();
	}
}
