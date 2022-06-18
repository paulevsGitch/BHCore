package paulevs.bhcore.util;

import net.minecraft.block.BaseBlock;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.BlockToolLogic;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagRegistry;

public class ToolsUtil {
	public static final Identifier AXE = Identifier.of("tools/axes");
	public static final Identifier PICKAXE = Identifier.of("tools/pickaxes");
	public static final Identifier SHOVEL = Identifier.of("tools/shovels");
	public static final Identifier SWORD = Identifier.of("tools/swords");
	public static final Identifier SHEARS = Identifier.of("tools/shears");
	
	/**
	 * Set tool for mining block
	 * @param block {@link BaseBlock} block to add tool tag
	 * @param toolTag {@link Identifier} tool tag to assign to this block
	 * @param level required minimum level to mine block
	 */
	public static void setTool(BaseBlock block, Identifier toolTag, int level) {
		((BlockToolLogic) block).mineableBy(toolTag, level);
	}
	
	/**
	 * Set axe for mining block
	 * @param block {@link BaseBlock} block to add tool tag
	 * @param level required minimum level to mine block
	 */
	public static void setAxe(BaseBlock block, int level) {
		setTool(block, AXE, level);
	}
	
	/**
	 * Set pickaxe for mining block
	 * @param block {@link BaseBlock} block to add tool tag
	 * @param level required minimum level to mine block
	 */
	public static void setPickaxe(BaseBlock block, int level) {
		setTool(block, PICKAXE, level);
	}
	
	/**
	 * Set shovel for mining block
	 * @param block {@link BaseBlock} block to add tool tag
	 * @param level required minimum level to mine block
	 */
	public static void setShovel(BaseBlock block, int level) {
		setTool(block, SHOVEL, level);
	}
	
	/**
	 * Set sword for mining block
	 * @param block {@link BaseBlock} block to add tool tag
	 * @param level required minimum level to mine block
	 */
	public static void setSword(BaseBlock block, int level) {
		setTool(block, SWORD, level);
	}
	
	/**
	 * Set shears for mining block
	 * @param block {@link BaseBlock} block to add tool tag
	 * @param level required minimum level to mine block
	 */
	public static void setShears(BaseBlock block, int level) {
		setTool(block, SHEARS, level);
	}
	
	/**
	 * Check if item has tag.
	 * @param tag {@link Identifier} tool tag.
	 * @param item {@link ItemStack} item.
	 * @return true if item has tag
	 */
	public static boolean hasTag(Identifier tag, ItemStack item) {
		return TagRegistry.INSTANCE.tagMatches(tag, item);
	}
	
	/**
	 * Check if item has axe tag.
	 * @param item {@link ItemStack} item.
	 * @return true if item is axe
	 */
	public static boolean isAxe(ItemStack item) {
		return TagRegistry.INSTANCE.tagMatches(AXE, item);
	}
	
	/**
	 * Check if item has pickaxe tag.
	 * @param item {@link ItemStack} item.
	 * @return true if item is pickaxe
	 */
	public static boolean isPickaxe(ItemStack item) {
		return TagRegistry.INSTANCE.tagMatches(PICKAXE, item);
	}
	
	/**
	 * Check if item has shovel tag.
	 * @param item {@link ItemStack} item.
	 * @return true if item is shovel
	 */
	public static boolean isShovel(ItemStack item) {
		return TagRegistry.INSTANCE.tagMatches(SHOVEL, item);
	}
	
	/**
	 * Check if item has sword tag.
	 * @param item {@link ItemStack} item.
	 * @return true if item is sword
	 */
	public static boolean isSword(ItemStack item) {
		return TagRegistry.INSTANCE.tagMatches(SWORD, item);
	}
	
	/**
	 * Check if item has shears tag.
	 * @param item {@link ItemStack} item.
	 * @return true if item is shears
	 */
	public static boolean isShears(ItemStack item) {
		return TagRegistry.INSTANCE.tagMatches(SHEARS, item);
	}
}
