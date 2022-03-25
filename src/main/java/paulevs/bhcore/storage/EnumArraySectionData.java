package paulevs.bhcore.storage;

import net.minecraft.util.io.CompoundTag;
import paulevs.bhcore.interfaces.CustomSectionData;

import java.util.function.Function;

public class EnumArraySectionData <E extends Enum<E>> implements CustomSectionData {
	private final CustomSectionData array;
	private final ArrayType type;
	private final E[] constants;
	
	public EnumArraySectionData(int capacity, Class<E> enumClass) {
		constants = enumClass.getEnumConstants();
		type = constants.length < ArrayType.NIBBLE.maxValue ? ArrayType.NIBBLE : constants.length < ArrayType.BYTE.maxValue ? ArrayType.BYTE : ArrayType.INT;
		array = type.constructor.apply(capacity);
		
		if (type == ArrayType.NIBBLE) {
			NibbleArraySectionData data = NibbleArraySectionData.class.cast(array);
			for (int i = 0; i < capacity; i++) {
				data.setData(i, (byte) type.maxValue);
			}
		}
		else if (type == ArrayType.BYTE) {
			ByteArraySectionData data = ByteArraySectionData.class.cast(array);
			for (int i = 0; i < capacity; i++) {
				data.setData(i, (byte) type.maxValue);
			}
		}
		else {
			IntArraySectionData data = IntArraySectionData.class.cast(array);
			for (int i = 0; i < capacity; i++) {
				data.setData(i, type.maxValue);
			}
		}
	}
	
	@Override
	public void saveToBNT(String dataKey, CompoundTag tag) {
		array.saveToBNT(dataKey, tag);
	}
	
	@Override
	public void loadFromBNT(String dataKey, CompoundTag tag) {
		array.loadFromBNT(dataKey, tag);
	}
	
	public E getData(int index) {
		int value = -1;
		switch (type) {
			case NIBBLE -> value = NibbleArraySectionData.class.cast(array).getData(index) & type.maxValue;
			case BYTE -> value = ByteArraySectionData.class.cast(array).getData(index) & type.maxValue;
			case INT -> value = IntArraySectionData.class.cast(array).getData(index) & type.maxValue;
		}
		if (value == type.maxValue) return null;
		return constants[value];
	}
	
	public void setData(int index, E data) {
		int value = -1;
		if (data != null) value = data.ordinal();
		switch (type) {
			case NIBBLE -> NibbleArraySectionData.class.cast(array).setData(index, (byte) value);
			case BYTE -> ByteArraySectionData.class.cast(array).setData(index, (byte) value);
			case INT -> IntArraySectionData.class.cast(array).setData(index, value);
		}
	}
	
	private enum ArrayType {
		NIBBLE(0, NibbleArraySectionData::new, 15),
		BYTE(1, ByteArraySectionData::new, 255),
		INT(2, IntArraySectionData::new, Integer.MAX_VALUE);
		
		final Function<Integer, CustomSectionData> constructor;
		final int maxValue;
		final byte type;
		
		ArrayType(int type, Function<Integer, CustomSectionData> constructor, int maxValue) {
			this.constructor = constructor;
			this.maxValue = maxValue;
			this.type = (byte) type;
		}
	}
}
