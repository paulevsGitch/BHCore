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
		type = constants.length < 16 ? ArrayType.NIBBLE : constants.length < 256 ? ArrayType.BYTE : ArrayType.INT;
		array = type.constructor.apply(capacity);
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
			case NIBBLE -> value = NibbleArraySectionData.class.cast(array).getData(index);
			case BYTE -> value = ByteArraySectionData.class.cast(array).getData(index);
			case INT -> value = IntArraySectionData.class.cast(array).getData(index);
		}
		if (value == -1) return null;
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
		NIBBLE(0, NibbleArraySectionData::new),
		BYTE(1, ByteArraySectionData::new),
		INT(2, IntArraySectionData::new);
		
		final Function<Integer, CustomSectionData> constructor;
		final byte type;
		
		ArrayType(int type, Function<Integer, CustomSectionData> constructor) {
			this.constructor = constructor;
			this.type = (byte) type;
		}
		
		int getIndex(int value) {
			return this == NIBBLE ? value & 15 : this == BYTE ? value & 255 : value;
		}
	}
}
