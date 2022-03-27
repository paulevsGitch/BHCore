package paulevs.bhcore.storage.section.arrays;

import net.minecraft.util.io.CompoundTag;
import paulevs.bhcore.interfaces.CustomSectionData;

public abstract class AbstractIntArraySectionData implements CustomSectionData {
	@Override
	public void saveToBNT(String dataKey, CompoundTag tag) {
		if (isEmpty()) return;
		saveData(dataKey, tag);
	}
	
	public abstract int getIntData(int index);
	public abstract void setIntData(int index, int data);
	public abstract boolean isEmpty();
	protected abstract void saveData(String dataKey, CompoundTag tag);
	public abstract int maxValue();
	public abstract int length();
}
