package paulevs.bhcore.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import org.jetbrains.annotations.Nullable;

public interface ItemModelProvider {
	@Nullable
	@Environment(EnvType.CLIENT)
	BakedModel getModel(ItemInstance stack);
}
