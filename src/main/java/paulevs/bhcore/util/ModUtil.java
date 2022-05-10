package paulevs.bhcore.util;

import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.registry.ModID;

import java.util.List;
import java.util.stream.Collectors;

public class ModUtil {
	private static List<ModID> modIDs;
	
	public static void init() {
		modIDs = FabricLoader.getInstance().getAllMods().stream().map(container -> ModID.of(container.getMetadata().getId())).collect(Collectors.toUnmodifiableList());
	}
	
	public static List<ModID> getMods() {
		return modIDs;
	}
}
