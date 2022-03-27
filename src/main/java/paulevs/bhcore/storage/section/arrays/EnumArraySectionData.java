package paulevs.bhcore.storage.section.arrays;

import net.minecraft.util.io.CompoundTag;
import org.jetbrains.annotations.Nullable;
import paulevs.bhcore.interfaces.CustomSectionData;

import java.util.function.Function;

public class EnumArraySectionData <E extends Enum<E>> implements CustomSectionData {
	private final AbstractIntArraySectionData array;
	private final boolean hasNullState;
	private final ArrayType type;
	private final E[] constants;
	
	public EnumArraySectionData(int capacity, Class<E> enumClass, boolean hasNullState) {
		this.hasNullState = hasNullState;
		this.constants = enumClass.getEnumConstants();
		this.type = getType(hasNullState, constants.length);
		this.array = type.constructor.apply(capacity);
	}
	
	@Override
	public void saveToBNT(String dataKey, CompoundTag tag) {
		array.saveToBNT(dataKey, tag);
	}
	
	@Override
	public void loadFromBNT(String dataKey, CompoundTag tag) {
		array.loadFromBNT(dataKey, tag);
	}
	
	@Nullable
	public E getData(int index) {
		int value = array.getIntData(index) & array.maxValue();
		if (hasNullState) {
			if (value == 0 || value >= constants.length) return null;
			return constants[value - 1];
		}
		else {
			return value >= constants.length ? constants[0] : constants[value];
		}
	}
	
	public void setData(int index, E data) {
		int value = data == null ? 0 : data.ordinal() + 1;
		array.setIntData(index, value);
	}
	
	private static ArrayType getType(boolean haveNullState, int valuesCount) {
		int maxCount = haveNullState ? valuesCount + 1 : valuesCount;
		for (ArrayType type: ArrayType.VALUES) {
			if (maxCount < type.maxValue) {
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
		final Function<Integer, AbstractIntArraySectionData> constructor;
		final int maxValue;
		final byte type;
		
		ArrayType(int type, Function<Integer, AbstractIntArraySectionData> constructor, int maxValue) {
			this.constructor = constructor;
			this.maxValue = maxValue;
			this.type = (byte) type;
		}
	}
}
