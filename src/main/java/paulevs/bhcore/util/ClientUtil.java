package paulevs.bhcore.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.level.Level;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ClientUtil {
	/**
	 * Get current Minecraft instance (using Fabric API).
	 * @return {@link Minecraft} instance.
	 */
	@SuppressWarnings("deprecation")
	public static Minecraft getMinecraft() {
		return Minecraft.class.cast(FabricLoader.getInstance().getGameInstance());
	}
	
	/**
	 * Get current client level (or null if player is in game menu or teleporting).
	 * @return client {@link Level}.
	 */
	@Nullable
	public static Level getClientLevel() {
		return getMinecraft().level;
	}
	
	/**
	 * Check if client has enabled "Fancy Graphics" option.
	 * @return {@code true} if client has enabled Fancy Graphics.
	 */
	public static boolean isFancyGraphics() {
		return getMinecraft().options.fancyGraphics;
	}
}
