package paulevs.bhcore.interfaces;

import net.minecraft.util.io.CompoundTag;

public interface CustomSectionData {
	void saveToBNT(String dataKey, CompoundTag tag);
	void loadFromBNT(String dataKey, CompoundTag tag);
}
