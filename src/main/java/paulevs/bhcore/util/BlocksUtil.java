package paulevs.bhcore.util;

import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.level.WorldPopulationRegion;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bhcore.storage.vector.Vec3I;

public class BlocksUtil {
	/**
	 * Set block state in world.
	 * @param level {@link BlockView}, {@link Level} or {@link WorldPopulationRegion}
	 * @param pos {@link Vec3I} block coordinate
	 * @param state {@link BlockState} to place
	 */
	public static void setBlockState(BlockView level, Vec3I pos, BlockState state) {
		setBlockState(level, pos.x, pos.y, pos.z, state);
	}
	
	/**
	 * Set block state in world.
	 * @param level {@link BlockView}, {@link Level} or {@link WorldPopulationRegion}
	 * @param x X block coordinate
	 * @param y Y block coordinate
	 * @param z Z block coordinate
	 * @param state {@link BlockState} to place
	 */
	public static void setBlockState(BlockView level, int x, int y, int z, BlockState state) {
		((BlockStateView) level).setBlockState(x, y, z, state);
	}
	
	/**
	 * Get block state from world.
	 * @param level {@link BlockView}, {@link Level} or {@link WorldPopulationRegion}
	 * @param pos {@link Vec3I} block coordinate
	 * @return {@link BlockState} at this position
	 */
	public static BlockState getBlockState(BlockView level, Vec3I pos) {
		return getBlockState(level, pos.x, pos.y, pos.z);
	}
	
	/**
	 * Get block state from world.
	 * @param level {@link BlockView}, {@link Level} or {@link WorldPopulationRegion}
	 * @param x X block coordinate
	 * @param y Y block coordinate
	 * @param z Z block coordinate
	 * @return {@link BlockState} at this position
	 */
	public static BlockState getBlockState(BlockView level, int x, int y, int z) {
		return ((BlockStateView) level).getBlockState(x, y, z);
	}
	
	/**
	 * Get direction from block face integer. Can be used to correctly update state of placed blocks
	 * @param facing block facing [0-5]
	 * @return {@link Direction} that matches facing
	 */
	public static Direction fromFacing(int facing) {
		return MathUtil.DIRECTIONS[facing];
	}
}
