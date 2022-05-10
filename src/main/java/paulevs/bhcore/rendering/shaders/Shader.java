package paulevs.bhcore.rendering.shaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import paulevs.bhcore.interfaces.Disposable;
import paulevs.bhcore.util.DisposeUtil;
import paulevs.bhcore.util.ResourceUtil;

@Environment(EnvType.CLIENT)
public class Shader implements Disposable {
	private final ShaderType type;
	private final int id;
	
	public Shader(String shaderCode, ShaderType type) {
		this.type = type;
		id = GL20.glCreateShader(type.getID());
		GL20.glShaderSource(id, shaderCode);
		GL20.glCompileShader(id);
		if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Can't create shader with type " + type + ", reason: " + GL20.glGetShaderInfoLog(id, 512));
		}
		DisposeUtil.addObject(this);
	}
	
	public ShaderType getType() {
		return type;
	}
	
	public int getID() {
		return id;
	}
	
	@Override
	public void dispose() {
		GL20.glDeleteShader(id);
	}
	
	public static Shader create(Identifier id, ShaderType type) {
		String path = "/assets/" + id.modID + "/shaders/" + id.id + "." + type.getExtension();
		String source = ResourceUtil.readResourceAsString(path, true, true, true);
		return new Shader(source, type);
	}
}
