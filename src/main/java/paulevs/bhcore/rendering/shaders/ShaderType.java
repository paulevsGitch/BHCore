package paulevs.bhcore.rendering.shaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

@Environment(EnvType.CLIENT)
public enum ShaderType {
	VERTEX(GL20.GL_VERTEX_SHADER),
	GEOMETRY(GL32.GL_GEOMETRY_SHADER),
	FRAGMENT(GL20.GL_FRAGMENT_SHADER);
	
	private final int id;
	
	ShaderType(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
}
