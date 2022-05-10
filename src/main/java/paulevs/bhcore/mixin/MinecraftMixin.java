package paulevs.bhcore.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bhcore.rendering.shaders.DefaultShaderPrograms;
import paulevs.bhcore.util.DisposeUtil;
import paulevs.bhcore.util.ModUtil;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(method = "init()V", at = @At("TAIL"))
	public void bhc_init(CallbackInfo info) {
		ModUtil.init();
		DefaultShaderPrograms.init();
	}
	
	@Inject(method = "stop()V", at = @At("HEAD"))
	private void bhc_onGameStop(CallbackInfo info) {
		DisposeUtil.dispose();
	}
}
