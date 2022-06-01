package paulevs.bhcore.util;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.BlockStateView;

public class BlocksUtil {
	public static void setBlockState(Level level, int x, int y, int z, BlockState state) {
		((BlockStateView) level).setBlockState(x, y, z, state);
	}
	
	public static BlockState getBlockState(Level level, int x, int y, int z) {
		return ((BlockStateView) level).getBlockState(x, y, z);
	}
}
