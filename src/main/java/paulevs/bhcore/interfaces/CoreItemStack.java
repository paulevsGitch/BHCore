package paulevs.bhcore.interfaces;

import net.minecraft.util.io.CompoundTag;
import org.jetbrains.annotations.Nullable;

public interface CoreItemStack {
	@Nullable
	CompoundTag bhc_getItemNBT();
	void bhc_setItemNBT(CompoundTag tag);
}
