package paulevs.bhcore.storage.section.arrays;

import net.minecraft.util.io.CompoundTag;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IntArraySectionData extends AbstractIntArraySectionData {
	private static final int MAX_VALUE = Integer.MAX_VALUE;
	private final int[] array;
	
	public IntArraySectionData(int capacity) {
		array = new int[capacity];
	}
	
	public int getData(int index) {
		return array[index];
	}
	
	public void setData(int index, int data) {
		array[index] = data;
	}
	
	@Override
	public void saveData(String dataKey, CompoundTag tag) {
		ByteBuffer buffer = ByteBuffer.allocate(array.length << 2).order(ByteOrder.BIG_ENDIAN);
		for (int i = 0; i < array.length; i++) buffer.putInt(array[i]);
		tag.put(dataKey, buffer.array());
	}
	
	@Override
	public int maxValue() {
		return MAX_VALUE;
	}
	
	@Override
	public int length() {
		return array.length;
	}
	
	@Override
	public void loadFromBNT(String dataKey, CompoundTag tag) {
		byte[] tagArray = tag.getByteArray(dataKey);
		if (tagArray.length >> 2 != array.length) return;
		ByteBuffer buffer = ByteBuffer.wrap(tagArray).order(ByteOrder.BIG_ENDIAN);
		buffer.rewind();
		for (int i = 0; i < array.length; i++) array[i] = buffer.getInt();
	}
	
	@Override
	public int getIntData(int index) {
		return getData(index);
	}
	
	@Override
	public void setIntData(int index, int data) {
		setData(index, data);
	}
	
	@Override
	public boolean isEmpty() {
		for (int value: array) {
			if (value != 0) return false;
		}
		return true;
	}
}
