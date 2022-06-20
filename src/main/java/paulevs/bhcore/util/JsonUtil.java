package paulevs.bhcore.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.modificationstation.stationapi.api.client.resource.Resource;
import paulevs.bhcore.storage.vector.Vec3F;

import java.io.InputStreamReader;

public class JsonUtil {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static JsonObject read(Resource resource) {
		return GSON.fromJson(new InputStreamReader(resource.getInputStream()), JsonObject.class);
	}
	
	/**
	 * Converts {@link JsonArray} to a vector. Array should have only 3 entries.
	 * @param array {@link JsonArray} to read a vector
	 * @return new {@link Vec3F} instance
	 */
	public static Vec3F vectorFromArray(JsonArray array) {
		return new Vec3F(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat());
	}
}
