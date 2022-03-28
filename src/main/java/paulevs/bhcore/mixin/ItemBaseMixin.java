package paulevs.bhcore.mixin;

import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import paulevs.bhcore.interfaces.ItemModelProvider;

@Mixin(ItemBase.class)
public class ItemBaseMixin implements ItemModelProvider {
	@Unique
	@Override
	@Nullable
	public BakedModel bhc_getModel(ItemInstance stack) {
		return null;
	}
}
