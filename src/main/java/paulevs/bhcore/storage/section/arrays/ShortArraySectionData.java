package paulevs.bhcore.storage.section.arrays;

import net.minecraft.util.io.CompoundTag;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ShortArraySectionData extends AbstractIntArraySectionData {
	private static final int MAX_VALUE = 65535;
	private final short[] array;
	
	public ShortArraySectionData(int capacity) {
		array = new short[capacity];
	}
	
	public short getData(int index) {
		return array[index];
	}
	
	public void setData(int index, short data) {
		array[index] = data;
	}
	
	@Override
	public int getIntData(int index) {
		return getData(index);
	}
	
	@Override
	public void setIntData(int index, int data) {
		setData(index, (short) data);
	}
	
	@Override
	public boolean isEmpty() {
		for (short value: array) {
			if (value != 0) return false;
		}
		return true;
	}
	
	@Override
	public void saveData(String dataKey, CompoundTag tag) {
		ByteBuffer buffer = ByteBuffer.allocate(array.length << 2).order(ByteOrder.BIG_ENDIAN);
		for (int i = 0; i < array.length; i++) buffer.putShort(array[i]);
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
		for (int i = 0; i < array.length; i++) array[i] = buffer.getShort();
	}
}
