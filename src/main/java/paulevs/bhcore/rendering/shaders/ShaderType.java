package paulevs.bhcore.rendering.shaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

@Environment(EnvType.CLIENT)
public enum ShaderType {
	VERTEX(GL20.GL_VERTEX_SHADER, "vert"),
	GEOMETRY(GL32.GL_GEOMETRY_SHADER, "geom"),
	FRAGMENT(GL20.GL_FRAGMENT_SHADER, "frag");
	
	private final int id;
	private final String extension;
	
	ShaderType(int id, String extension) {
		this.id = id;
		this.extension = extension;
	}
	
	public int getID() {
		return id;
	}
	
	public String getExtension() {
		return extension;
	}
}
