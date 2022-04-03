package paulevs.bhcore.rendering.shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import paulevs.bhcore.rendering.shaders.uniforms.Uniform;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ShaderProgram implements AutoCloseable {
	private Map<String, Uniform> uniforms = new HashMap<>();
	private final Shader[] shaders;
	private final int id;
	
	public ShaderProgram(Shader... shaders) {
		this.shaders = shaders;
		id = GL20.glCreateProgram();
		for (Shader shader: shaders) {
			GL20.glAttachShader(id, shader.getID());
		}
		GL20.glLinkProgram(id);
		if (GL20.glGetShaderi(id, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Can't link shader program, reason: " + GL20.glGetShaderInfoLog(id, 512));
		}
		GL20.glValidateProgram(id);
		if (GL20.glGetShaderi(id, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Can't validate shader program, reason: " + GL20.glGetShaderInfoLog(id, 512));
		}
		GL20.glLinkProgram(0);
	}
	
	public <T extends Uniform> T getUniform(String name, Function<Integer, T> constructor) {
		Uniform uniform = uniforms.get(name);
		if (uniform != null) {
			return (T) uniform;
		}
		this.bind();
		int id = GL20.glGetUniformLocation(this.id, name);
		T result = constructor.apply(id);
		uniforms.put(name, result);
		return result;
	}
	
	public void bind() {
		GL20.glUseProgram(id);
	}
	
	public void bindWithUniforms() {
		bind();
		uniforms.values().forEach(uniform -> uniform.bind());
	}
	
	public static void unbind() {
		GL20.glUseProgram(0);
	}
	
	@Override
	public void close() throws Exception {
		for (Shader shader: shaders) {
			shader.close();
		}
		GL20.glDeleteProgram(id);
	}
}
