package paulevs.bhcore.util;

import net.minecraft.block.BaseBlock;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;

import java.util.HashMap;
import java.util.Map;

public class BurnableUtil {
	private static final Map<BlockState, BurnableInfo> INFO = new HashMap<>();
	
	/**
	 * Register block state that can be burned by fire.
	 * @param state {@link BlockState} that can burn.
	 * @param burnSpeed burn speed. Wood have 5, leaves have 30
	 * @param spreadDelay spread delay (chance of fire to be created). Wood have 20, leaves have 60
	 */
	public static void registerBurnable(BlockState state, int burnSpeed, int spreadDelay) {
		INFO.put(state, new BurnableInfo(burnSpeed, spreadDelay));
	}
	
	/**
	 * Register block that can be burned by fire. Will be applied to all block states.
	 * @param block {@link BaseBlock} that can burn.
	 * @param burnSpeed burn speed. Wood have 5, leaves have 30
	 * @param spreadDelay spread delay (chance of fire to be created). Wood have 20, leaves have 60
	 */
	public static void registerBurnable(BaseBlock block, int burnSpeed, int spreadDelay) {
		BlockStateHolder holder = (BlockStateHolder) block;
		holder.getStateManager().getStates().forEach(state -> registerBurnable(state, burnSpeed, spreadDelay));
	}
	
	/**
	 * Register block that can be burned by fire. All values are identical to wood.
	 * @param state {@link BlockState} that can burn.
	 */
	public static void registerBurnable(BlockState state) {
		registerBurnable(state, 5, 20);
	}
	
	/**
	 * Register block that can be burned by fire. Will be applied to all block states.
	 * All values are identical to wood.
	 * @param block {@link BaseBlock} that can burn.
	 */
	public static void registerBurnable(BaseBlock block) {
		BlockStateHolder holder = (BlockStateHolder) block;
		holder.getStateManager().getStates().forEach(state -> registerBurnable(state));
	}
	
	public static int getBurnSpeed(BlockState state) {
		BurnableInfo info = INFO.get(state);
		return info == null ? 0 : info.burnSpeed;
	}
	
	public static int getSpreadDelay(BlockState state) {
		BurnableInfo info = INFO.get(state);
		return info == null ? 0 : info.spreadDelay;
	}
	
	private record BurnableInfo(int burnSpeed, int spreadDelay) {}
}
