package paulevs.bhcore.storage.section.arrays;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.impl.level.chunk.NibbleArray;
import paulevs.bhcore.interfaces.CoreNibbleArray;

public class NibbleArraySectionData extends AbstractIntArraySectionData {
	private static final int MAX_VALUE = 15;
	private final NibbleArray array;
	
	public NibbleArraySectionData(int capacity) {
		array = new NibbleArray(capacity);
	}
	
	public byte getData(int index) {
		return (byte) array.getValue(index);
	}
	
	public void setData(int index, byte data) {
		array.setValue(index, data);
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
		CoreNibbleArray array = CoreNibbleArray.class.cast(this.array);
		for (int i = 0; i < array.bhc_getLength(); i++) {
			if (array.bhc_getByte(0) != 0) return false;
		}
		return true;
	}
	
	@Override
	protected void saveData(String dataKey, CompoundTag tag) {
		tag.put(dataKey, array.toTag());
	}
	
	@Override
	public int maxValue() {
		return MAX_VALUE;
	}
	
	@Override
	public int length() {
		return 0;
	}
	
	@Override
	public void loadFromBNT(String dataKey, CompoundTag tag) {
		array.copyArray(tag.getByteArray(dataKey));
	}
}
