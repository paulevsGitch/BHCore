package paulevs.bhcore.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
	public static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * Merge all objects string representations and log it.
	 * @param objects array of {@link Object} to log
	 */
	public static void log(Object... objects) {
		if (objects.length == 0) return;
		StringBuilder builder = new StringBuilder(objects[0].toString());
		for (int i = 1; i < objects.length; i++) {
			builder.append(' ');
			builder.append(objects[i]);
		}
		log(builder);
	}
	
	/**
	 * Log string representation of an object.
	 * @param obj {@link Object} to log
	 */
	public static void log(Object obj) {
		LOGGER.log(Level.INFO, obj.toString());
	}
	
	/**
	 * Log a warning message
	 * @param message {@link String} warning
	 */
	public static void warn(String message) {
		LOGGER.warn(message);
	}
}
