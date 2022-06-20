package paulevs.bhcore.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ModelUtil {
	private static final Map<Identifier, Identifier> ID_REPLACEMENTS = new HashMap<>();
	
	/**
	 * Register replacement for model.
	 * @param idToReplace {@link Identifier} to replace
	 * @param replacementID {@link Identifier} replacement
	 */
	public static void registerReplacement(Identifier idToReplace, Identifier replacementID) {
		ID_REPLACEMENTS.put(idToReplace, replacementID);
	}
	
	/**
	 * Get ID replacement.
	 * @param id key {@link Identifier}
	 * @return {@link Identifier} or null
	 */
	@Nullable
	public static Identifier getReplacement(Identifier id) {
		return ID_REPLACEMENTS.get(id);
	}
}
