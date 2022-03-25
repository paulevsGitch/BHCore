package paulevs.bhcore.storage;

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
	
	public static int register(Identifier dataKey, Supplier<CustomSectionData> constructor) {
		int index = CONSTRUCTORS.size();
		CONSTRUCTORS.add(constructor);
		KEYS.add(dataKey.toString());
		return index;
	}
	
	public static String getKey(int index) {
		return KEYS.get(index);
	}
	
	public static Supplier<CustomSectionData> getConstructor(int index) {
		return CONSTRUCTORS.get(index);
	}
	
	public static int getCount() {
		return CONSTRUCTORS.size();
	}
	
	public static <T extends CustomSectionData> T getData(ChunkSection section, int index) {
		return (T) CoreChunkSection.class.cast(section).bhc_getData(index);
	}
	
	public static <T extends CustomSectionData> T getData(Chunk chunk, int sectionIndex, int dataIndex) {
		ChunkSectionsAccessor accessor = ChunkSectionsAccessor.class.cast(chunk);
		ChunkSection section = accessor.getSections()[sectionIndex];
		if (section == null) return null;
		return getData(section, dataIndex);
	}
}
