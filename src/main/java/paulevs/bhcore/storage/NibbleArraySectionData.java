package paulevs.bhcore.storage;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.impl.level.chunk.NibbleArray;
import paulevs.bhcore.interfaces.CustomSectionData;

public class NibbleArraySectionData implements CustomSectionData {
	private final NibbleArray array;
	
	public NibbleArraySectionData(int capacity) {
		array = new NibbleArray(capacity);
	}
	
	public int getData(int index) {
		return array.getValue(index);
	}
	
	public void setData(int index, int data) {
		array.setValue(index, data);
	}
	
	@Override
	public void saveToBNT(String dataKey, CompoundTag tag) {
		tag.put(dataKey, array.toTag());
	}
	
	@Override
	public void loadFromBNT(String dataKey, CompoundTag tag) {
		array.copyArray(tag.getByteArray(dataKey));
	}
}
