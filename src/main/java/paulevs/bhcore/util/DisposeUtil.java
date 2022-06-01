package paulevs.bhcore.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import paulevs.bhcore.interfaces.Disposable;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class DisposeUtil {
	private static final List<Disposable> OBJECTS_TO_DISPOSE = new ArrayList<>();
	
	/**
	 * Add object into internal list. All objects from this list will be disposed when Minecraft will stop running.
	 * @param object {@link Disposable} object.
	 */
	public static void addObject(Disposable object) {
		OBJECTS_TO_DISPOSE.add(object);
	}
	
	/**
	 * Dispose all objects from internal list. Should be called only on program exit.
	 */
	public static void dispose() {
		OBJECTS_TO_DISPOSE.forEach(object -> object.dispose());
	}
}
