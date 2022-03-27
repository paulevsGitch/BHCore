package paulevs.bhcore.storage.section.arrays;

import net.minecraft.util.io.CompoundTag;

public class ByteArraySectionData extends AbstractIntArraySectionData {
	private static final int MAX_VALUE = 255;
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
	public void loadFromBNT(String dataKey, CompoundTag tag) {
		byte[] tagArray = tag.getByteArray(dataKey);
		if (tagArray.length == array.length) {
			System.arraycopy(tagArray, 0, array, 0, array.length);
		}
	}
	
	@Override
	public int getIntData(int index) {
		return getData(index);
	}
	
	@Override
	public void setIntData(int index, int data) {
		setData(index, (byte) data);
	}
	
	@Override
	public boolean isEmpty() {
		for (byte value: array) {
			if (value != 0) return false;
		}
		return true;
	}
	
	@Override
	protected void saveData(String dataKey, CompoundTag tag) {
		tag.put(dataKey, array);
	}
	
	@Override
	public int maxValue() {
		return MAX_VALUE;
	}
	
	@Override
	public int length() {
		return array.length;
	}
}
