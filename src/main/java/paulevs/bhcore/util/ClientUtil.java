package paulevs.bhcore.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.level.Level;
import org.jetbrains.annotations.Nullable;

public class ClientUtil {
	/**
	 * Get current Minecraft instance (using Fabric API).
	 * @return {@link Minecraft} instance.
	 */
	@Environment(EnvType.CLIENT)
	@SuppressWarnings("deprecation")
	public static Minecraft getMinecraft() {
		return Minecraft.class.cast(FabricLoader.getInstance().getGameInstance());
	}
	
	/**
	 * Get current client level (or null if player is in game menu or teleporting).
	 * @return client {@link Level}.
	 */
	@Nullable
	@Environment(EnvType.CLIENT)
	public static Level getClientLevel() {
		return getMinecraft().level;
	}
	
	/**
	 * Check if client has enabled "Fancy Graphics" option.
	 * @return {@code true} if client has enabled Fancy Graphics.
	 */
	@Environment(EnvType.CLIENT)
	public static boolean isFancyGraphics() {
		return getMinecraft().options.fancyGraphics;
	}
	
	/**
	 * Update area on client side.
	 * @param level client {@link Level}.
	 * @param x1 start X coordinate.
	 * @param y1 start Y coordinate.
	 * @param z1 start Z coordinate.
	 * @param x2 end X coordinate.
	 * @param y2 end Y coordinate.
	 * @param z2 end Z coordinate.
	 */
	public static void updateArea(Level level, int x1, int y1, int z1, int x2, int y2, int z2) {
		level.method_202(x1, y1, z1, x2, y2, z2);
	}
	
	/**
	 * Update block on client side.
	 * @param level client {@link Level}.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 */
	public static void updateBlock(Level level, int x, int y, int z) {
		updateArea(level, x, y, z, x, y, z);
	}
}
