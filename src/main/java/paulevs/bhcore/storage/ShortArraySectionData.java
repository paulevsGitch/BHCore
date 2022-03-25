package paulevs.bhcore.storage;

import net.minecraft.util.io.CompoundTag;
import paulevs.bhcore.interfaces.CustomSectionData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ShortArraySectionData implements CustomSectionData {
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
	public void saveToBNT(String dataKey, CompoundTag tag) {
		ByteBuffer buffer = ByteBuffer.allocate(array.length << 2).order(ByteOrder.BIG_ENDIAN);
		for (int i = 0; i < array.length; i++) buffer.putShort(array[i]);
		tag.put(dataKey, buffer.array());
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
