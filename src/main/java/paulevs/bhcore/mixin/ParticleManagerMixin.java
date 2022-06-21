package paulevs.bhcore.mixin;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.BaseEntity;
import net.minecraft.entity.BaseParticle;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bhcore.interfaces.CustomParticle;
import paulevs.bhcore.util.MathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
	@Shadow private TextureManager textureManager;
	@Unique	private final List<BaseParticle> bhc_customParticles = new ArrayList<>(512);
	@Unique	private Map<Identifier, Integer> bhc_textureCache = new HashMap<>();
	@Unique	private Identifier bhc_activeTexture;
	@Unique	private int bhc_activeTarget = 0;
	
	@Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
	private void bhc_addParticle(BaseParticle particle, CallbackInfo info) {
		if (particle instanceof CustomParticle) {
			if (bhc_customParticles.size() == 512) bhc_customParticles.remove(0);
			bhc_customParticles.add(particle);
			info.cancel();
		}
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void bhc_tick(CallbackInfo info) {
		for (short i = 0; i < bhc_customParticles.size(); i++) {
			BaseParticle particle = bhc_customParticles.get(i);
			particle.tick();
			if (particle.removed) {
				bhc_customParticles.remove(i--);
			}
		}
	}
	
	@Inject(method = "renderAll(Lnet/minecraft/entity/BaseEntity;F)V", at = @At(value = "HEAD"))
	private void bhc_renderAll(BaseEntity arg, float delta, CallbackInfo info) {
		if (bhc_customParticles.isEmpty()) return;
		
		float angleYaw = arg.yaw * MathUtil.PI / 180.0F;
		float anglePitch = arg.pitch * MathUtil.PI / 180.0F;
		float x = MathHelper.cos(angleYaw);
		float z = MathHelper.sin(angleYaw);
		float y = MathHelper.cos(anglePitch);
		anglePitch = MathHelper.sin(anglePitch);
		float width = -z * anglePitch;
		float height = x * anglePitch;
		
		BaseParticle.posX = arg.prevRenderX + (arg.x - arg.prevRenderX) * delta;
		BaseParticle.posY = arg.prevRenderY + (arg.y - arg.prevRenderY) * delta;
		BaseParticle.posZ = arg.prevRenderZ + (arg.z - arg.prevRenderZ) * delta;
		
		bhc_activeTexture = null;
		Tessellator tessellator = Tessellator.INSTANCE;
		
		bhc_customParticles.forEach(particle -> {
			Identifier atlas = CustomParticle.cast(particle).getTextureAtlas();
			if (atlas != bhc_activeTexture) {
				boolean startNew = bhc_activeTexture == null;
				if (startNew) tessellator.start();
				else tessellator.draw();
				
				bhc_activeTexture = atlas;
				bhc_activeTarget = bhc_textureCache.computeIfAbsent(atlas, key -> textureManager.getTextureId(getTextureID(key)));
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, bhc_activeTarget);
				
				if (!startNew) tessellator.start();
			}
			particle.render(tessellator, delta, x, y, z, width, height);
		});
		tessellator.draw();
	}
	
	private String getTextureID(Identifier id) {
		return String.format("/assets/%s/stationapi/textures/particle/%s", id.modID, id.id);
	}
}
