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
		type = getType(constants.length);
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
		else if (type == ArrayType.SHORT) {
			ShortArraySectionData data = ShortArraySectionData.class.cast(array);
			for (int i = 0; i < capacity; i++) {
				data.setData(i, (short) type.maxValue);
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
			case SHORT -> value = ShortArraySectionData.class.cast(array).getData(index) & type.maxValue;
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
			case SHORT -> ShortArraySectionData.class.cast(array).setData(index, (byte) value);
			case INT -> IntArraySectionData.class.cast(array).setData(index, value);
		}
	}
	
	private static ArrayType getType(int valuesCount) {
		for (ArrayType type: ArrayType.VALUES) {
			if (valuesCount < type.maxValue) {
				return type;
			}
		}
		return ArrayType.INT;
	}
	
	private enum ArrayType {
		NIBBLE(0, NibbleArraySectionData::new, 15),
		BYTE(1, ByteArraySectionData::new, 255),
		SHORT(2, ShortArraySectionData::new, Short.MAX_VALUE),
		INT(3, IntArraySectionData::new, Integer.MAX_VALUE);
		
		static final ArrayType[] VALUES = values();
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
