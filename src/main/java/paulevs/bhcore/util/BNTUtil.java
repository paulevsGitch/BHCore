package paulevs.bhcore.util;

import net.minecraft.util.io.AbstractTag;
import net.minecraft.util.io.ByteArrayTag;
import net.minecraft.util.io.ByteTag;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.DoubleTag;
import net.minecraft.util.io.FloatTag;
import net.minecraft.util.io.IntTag;
import net.minecraft.util.io.ListTag;
import net.minecraft.util.io.LongArrayTag;
import net.minecraft.util.io.LongTag;
import net.minecraft.util.io.ShortTag;
import net.minecraft.util.io.StringTag;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class BNTUtil {
	/**
	 * Will copy NBT tag.
	 * @param tag Any NBT {@link AbstractTag} to copy.
	 */
	@Nullable
	public static <T extends AbstractTag> T copyTag(T tag) {
		AbstractTag t = null;
		
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
			AbstractTag.writeTag(tag, dataOutputStream);
			dataOutputStream.close();
			byte[] array = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.close();
			
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
			DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
			t = AbstractTag.readTag(dataInputStream);
			dataInputStream.close();
			byteArrayInputStream.close();
		}
		catch (IOException exception) {
			exception.printStackTrace();
		}
		
		return (T) t;
	}
	
	/**
	 * Compare tags and all their child tags.
	 * @param a Any NBT {@link AbstractTag}.
	 * @param b Any NBT {@link AbstractTag}.
	 * @return {@code true} if tags are equal.
	 */
	public static boolean equal(AbstractTag a, AbstractTag b) {
		if (a.getId() != b.getId()) return false;
		if (a.getId() == 0) return true;
		if (!a.getType().equals(b.getType())) return false;
		switch(a.getId()) {
			case 1 -> { return ByteTag.class.cast(a).data == ByteTag.class.cast(b).data; }
			case 2 -> { return ShortTag.class.cast(a).data == ShortTag.class.cast(b).data; }
			case 3 -> { return IntTag.class.cast(a).data == IntTag.class.cast(b).data; }
			case 4 -> { return LongTag.class.cast(a).data == LongTag.class.cast(b).data; }
			case 5 -> { return FloatTag.class.cast(a).data == FloatTag.class.cast(b).data; }
			case 6 -> { return DoubleTag.class.cast(a).data == DoubleTag.class.cast(b).data; }
			case 7 -> { return Arrays.equals(ByteArrayTag.class.cast(a).data, ByteArrayTag.class.cast(b).data); }
			case 8 -> { return StringTag.class.cast(a).data.equals(StringTag.class.cast(b).data); }
			case 9 -> {
				ListTag listA = ListTag.class.cast(a);
				ListTag listB = ListTag.class.cast(b);
				final int size = listA.size();
				if (size != listB.size()) return false;
				for (int i = 0; i < size; i++) {
					if (!equal(listA.get(i), listB.get(i))) return false;
				}
				return true;
			}
			case 10 -> {
				Collection<AbstractTag> valuesA = CompoundTag.class.cast(a).values();
				Collection<AbstractTag> valuesB = CompoundTag.class.cast(b).values();
				final int size = valuesA.size();
				if (size != valuesB.size()) return false;
				Iterator<AbstractTag> iteratorA = valuesA.iterator();
				Iterator<AbstractTag> iteratorB = valuesB.iterator();
				for (int i = 0; i < size; i++) {
					AbstractTag tagA = iteratorA.next();
					AbstractTag tagB = iteratorB.next();
					if (!equal(tagA, tagB)) return false;
				}
				return true;
			}
			case 12 -> { return Arrays.equals(LongArrayTag.class.cast(a).data, LongArrayTag.class.cast(b).data); }
		}
		return true;
	}
}
