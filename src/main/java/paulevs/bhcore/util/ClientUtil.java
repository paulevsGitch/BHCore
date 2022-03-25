package paulevs.bhcore.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.level.Level;

@Environment(EnvType.CLIENT)
public class ClientUtil {
	@SuppressWarnings("deprecation")
	public static Minecraft getMinecraft() {
		return Minecraft.class.cast(FabricLoader.getInstance().getGameInstance());
	}
	
	public static Level getClientLevel() {
		return getMinecraft().level;
	}
}
