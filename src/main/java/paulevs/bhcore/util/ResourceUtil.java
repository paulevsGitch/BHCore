package paulevs.bhcore.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceUtil {
	public static InputStream getStream(String path) {
		return ResourceUtil.class.getResourceAsStream(path);
	}
	
	public static String readResourceAsString(String path, boolean trim, boolean skipEmpty, boolean newLines) {
		InputStream input = getStream(path);
		if (input == null) {
			throw new RuntimeException("Resource with location " + path + " is missing");
		}
		return readAsString(input, trim, skipEmpty, newLines);
	}
	
	public static String readAsString(InputStream input, boolean trim, boolean skipEmpty, boolean newLines) {
		InputStreamReader streamReader = new InputStreamReader(input);
		StringBuilder builder = new StringBuilder();
		String line;
		try {
			BufferedReader reader = new BufferedReader(streamReader);
			while ((line = reader.readLine()) != null) {
				if (skipEmpty && line.isEmpty()) continue;
				if (trim) line = line.trim();
				builder.append(line);
				if (newLines) builder.append("\n");
			}
			reader.close();
			streamReader.close();
			input.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
