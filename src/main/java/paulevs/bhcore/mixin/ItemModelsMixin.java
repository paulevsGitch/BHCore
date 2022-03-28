package paulevs.bhcore.mixin;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bhcore.interfaces.ItemModelProvider;

@Mixin(value = ItemModels.class, remap = false)
public class ItemModelsMixin {
	@Inject(
		method = "getModel(Lnet/minecraft/item/ItemInstance;)Lnet/modificationstation/stationapi/api/client/render/model/BakedModel;",
		at = @At("HEAD"),
		cancellable = true
	)
	private void getModel(ItemInstance stack, CallbackInfoReturnable<BakedModel> info) {
		if (ItemModelProvider.class.isInstance(stack)) {
			BakedModel model = ItemModelProvider.class.cast(stack).getModel(stack);
			if (model != null) {
				info.setReturnValue(model);
			}
		}
	}
}
