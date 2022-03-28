package paulevs.bhcore.storage.section;

import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import paulevs.bhcore.interfaces.CoreChunkSection;
import paulevs.bhcore.interfaces.CustomSectionData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SectionDataHandler {
	private static final List<Supplier<CustomSectionData>> CONSTRUCTORS = new ArrayList<>();
	private static final List<String> KEYS = new ArrayList<>();
	
	/**
	 * Register additional section data and return its index in section.
	 * Index should be stored and used to get data from section.
	 * @param dataKey Unique {@link Identifier} for data.
	 * @param constructor {@link Supplier} for {@link CustomSectionData}, used to create new empty data values in section.
	 * @return Data index.
	 */
	public static int register(Identifier dataKey, Supplier<CustomSectionData> constructor) {
		int index = CONSTRUCTORS.size();
		CONSTRUCTORS.add(constructor);
		KEYS.add(dataKey.toString());
		return index;
	}
	
	/**
	 * Get BNT tag name (key) for stored data by index.
	 * @param index Data index.
	 * @return {@link String} data key.
	 */
	public static String getKey(int index) {
		return KEYS.get(index);
	}
	
	/**
	 * Get data constructor by index.
	 * @param index Data index.
	 * @return {@link Supplier} for {@link CustomSectionData}.
	 */
	public static Supplier<CustomSectionData> getConstructor(int index) {
		return CONSTRUCTORS.get(index);
	}
	
	/**
	 * Get count of registered custom data types.
	 */
	public static int getCount() {
		return CONSTRUCTORS.size();
	}
	
	/**
	 * Helper function to get data from section by index.
	 * @param section {@link ChunkSection} to get data from.
	 * @param index Data index.
	 * @return {@link CustomSectionData} of any type.
	 */
	public static <T extends CustomSectionData> T getData(ChunkSection section, int index) {
		return (T) CoreChunkSection.class.cast(section).bhc_getData(index);
	}
	
	/**
	 * Helper function to get data from chunk by section index and data index.
	 * @param chunk {@link Chunk} to get data from.
	 * @param sectionIndex Section index (y / 16 or y >> 4).
	 * @param dataIndex Data index.
	 * @return {@link CustomSectionData} of any type.
	 */
	public static <T extends CustomSectionData> T getData(Chunk chunk, int sectionIndex, int dataIndex) {
		ChunkSectionsAccessor accessor = ChunkSectionsAccessor.class.cast(chunk);
		ChunkSection section = accessor.getSections()[sectionIndex];
		if (section == null) return null;
		return getData(section, dataIndex);
	}
}
