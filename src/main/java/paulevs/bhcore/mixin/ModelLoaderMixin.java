package paulevs.bhcore.mixin;

import com.google.gson.JsonObject;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.client.resource.Resource;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bhcore.rendering.models.block.OBJBlockModel;
import paulevs.bhcore.util.JsonUtil;
import paulevs.bhcore.util.ModelUtil;

import java.io.IOException;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	@Shadow @Final private TexturePack resourceManager;
	@Unique private boolean skipMixin;
	
	@Inject(method = "loadModelFromResource", at = @At("HEAD"), cancellable = true, remap = false)
	private void bhc_loadModelFromResource(Identifier id, CallbackInfoReturnable<UnbakedModel> info) throws IOException {
		if (id.id.startsWith("builtin")) return;
		if (id.id.endsWith("#inventory")) return;
		
		Identifier id2 = ModelUtil.getReplacement(id);
		if (id2 != null) {
			id = id2;
		};
		
		Resource resource = Resource.of(resourceManager.getResourceAsStream(ResourceManager.ASSETS.toPath(
			id,
			MODID + "/models",
			"json"
		)));
		
		if (resource != null) {
			JsonObject obj = JsonUtil.read(resource);
			resource.close();
			
			if (obj.has("linkedOBJ")) {
				info.setReturnValue(OBJBlockModel.load(resourceManager, obj));
				return;
			}
		};
		
		if (id2 != null) {
			UnbakedModel model = loadModelFromResource(id2);
			info.setReturnValue(model);
		};
	}
	
	@Shadow
	private UnbakedModel loadModelFromResource(Identifier id) { return null; }
}
