package paulevs.bhcore.rendering.shaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import paulevs.bhcore.interfaces.Disposable;
import paulevs.bhcore.util.DisposeUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Environment(EnvType.CLIENT)
public class Shader implements Disposable {
	private final ShaderType type;
	private final int id;
	
	public Shader(String source, ShaderType type) {
		this.type = type;
		id = GL20.glCreateShader(type.getID());
		GL20.glShaderSource(id, readFileAsString(source));
		GL20.glCompileShader(id);
		if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Can't create shader " + source + " with type " + type + ", reason: " + GL20.glGetShaderInfoLog(id, 512));
		}
		DisposeUtil.addObject(this);
	}
	
	public ShaderType getType() {
		return type;
	}
	
	private String readFileAsString(String path) {
		StringBuilder builder = new StringBuilder();
		String line;
		try {
			InputStream input = getClass().getResourceAsStream(path);
			InputStreamReader streamReader = new InputStreamReader(input);
			BufferedReader reader = new BufferedReader(streamReader);
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					builder.append(line + "\n");
				}
			}
			reader.close();
			streamReader.close();
			input.close();
			return builder.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public int getID() {
		return id;
	}
	
	@Override
	public void dispose() {
		GL20.glDeleteShader(id);
	}
}
