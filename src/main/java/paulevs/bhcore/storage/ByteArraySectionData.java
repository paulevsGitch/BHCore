package paulevs.bhcore.storage;

import net.minecraft.util.io.CompoundTag;
import paulevs.bhcore.interfaces.CustomSectionData;

public class ByteArraySectionData implements CustomSectionData {
	private final byte[] array;
	
	public ByteArraySectionData(int capacity) {
		array = new byte[capacity];
	}
	
	public byte getData(int index) {
		return array[index];
	}
	
	public void setData(int index, byte data) {
		array[index] = data;
	}
	
	@Override
	public void saveToBNT(String dataKey, CompoundTag tag) {
		tag.put(dataKey, array);
	}
	
	@Override
	public void loadFromBNT(String dataKey, CompoundTag tag) {
		byte[] tagArray = tag.getByteArray(dataKey);
		if (tagArray.length == array.length) {
			System.arraycopy(tagArray, 0, array, 0, array.length);
		}
	}
}
