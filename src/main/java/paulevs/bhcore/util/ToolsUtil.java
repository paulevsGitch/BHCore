package paulevs.bhcore.util;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.BlockToolLogic;
import net.modificationstation.stationapi.api.registry.Identifier;

public class ToolsUtil {
	public static final Identifier AXE = Identifier.of("tools/axes");
	public static final Identifier PICKAXE = Identifier.of("tools/pickaxes");
	public static final Identifier SHOVEL = Identifier.of("tools/shovels");
	public static final Identifier SWORD = Identifier.of("tools/swords");
	public static final Identifier SHEARS = Identifier.of("tools/shears");
	
	/**
	 * Set tool for mining block
	 * @param block {@link BlockBase} block to add tool tag
	 * @param toolTag {@link Identifier} tool tag to assign to this block
	 * @param level required minimum level to mine block
	 */
	public static void setTool(BlockBase block, Identifier toolTag, int level) {
		((BlockToolLogic) block).mineableBy(toolTag, level);
	}
	
	/**
	 * Set axe for mining block
	 * @param block {@link BlockBase} block to add tool tag
	 * @param level required minimum level to mine block
	 */
	public static void setAxe(BlockBase block, int level) {
		setTool(block, AXE, level);
	}
	
	/**
	 * Set pickaxe for mining block
	 * @param block {@link BlockBase} block to add tool tag
	 * @param level required minimum level to mine block
	 */
	public static void setPickaxe(BlockBase block, int level) {
		setTool(block, PICKAXE, level);
	}
	
	/**
	 * Set shovel for mining block
	 * @param block {@link BlockBase} block to add tool tag
	 * @param level required minimum level to mine block
	 */
	public static void setShovel(BlockBase block, int level) {
		setTool(block, SHOVEL, level);
	}
	
	/**
	 * Set sword for mining block
	 * @param block {@link BlockBase} block to add tool tag
	 * @param level required minimum level to mine block
	 */
	public static void setSword(BlockBase block, int level) {
		setTool(block, SWORD, level);
	}
	
	/**
	 * Set shears for mining block
	 * @param block {@link BlockBase} block to add tool tag
	 * @param level required minimum level to mine block
	 */
	public static void setShears(BlockBase block, int level) {
		setTool(block, SHEARS, level);
	}
}
