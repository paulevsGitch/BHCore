package paulevs.bhcore.rendering.shaders.complex;

import paulevs.bhcore.rendering.shaders.Shader;
import paulevs.bhcore.rendering.shaders.ShaderProgram;
import paulevs.bhcore.rendering.shaders.ShaderType;
import paulevs.bhcore.util.ModUtil;
import paulevs.bhcore.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

public class ComplexShaderProgram extends ShaderProgram {
	private ComplexShaderProgram(Shader vertShader, Shader geomShader, Shader fragShader) {
		super(vertShader, geomShader, fragShader);
	}
	
	/*public <T extends Uniform> T getUniform(String name, ShaderType type, Function<Integer, T> constructor) {
	
	}*/
	
	/**
	 * Create new {@link ComplexShaderProgram} using {@link String} name to search in game resources for shaders.
	 * @param name {@link String} name of shader program, will be used in shader search.
	 * @return constructed {@link ComplexShaderProgram}.
	 */
	public static ComplexShaderProgram create(String name) {
		List<String> resources = getResources(name, "global_uniforms.txt");
		List<String> globalUniforms = getUniforms(resources);
		System.out.println(makeVertexShader(globalUniforms));
		return null;
	}
	
	private static List<String> getUniforms(List<String> resources) {
		List<String> uniforms = new ArrayList<>();
		resources.stream().map(res -> ResourceUtil.readResourceAsList(res, true, true)).forEach(uniforms::addAll);
		return uniforms;
	}
	
	private static List<String> getResources(String name, ShaderType type) {
		return getResources(name, "shader_" + type.getExtension() + ".txt");
	}
	
	private static List<String> getResources(String name, String type) {
		String shaderName = "/shaders/" + name + "/" + type;
		return ModUtil.getMods().stream().map(modID -> "/assets/" + modID.toString() + shaderName).filter(ResourceUtil::exists).toList();
	}
	
	private static String makeVertexShader(List<String> globalUniforms) {
		StringBuilder builder = new StringBuilder("#version 330 core\n");
		globalUniforms.forEach(uniform -> {
			builder.append("uniform ");
			builder.append(uniform);
			builder.append(";\n");
		});
		builder.append("void main() {\n");
		builder.append("gl_Position = projection * view * model * vert;\n");
		builder.append("}\n");
		return builder.toString();
	}
	
	private static void append(StringBuilder builder, String line) {
		builder.append(line);
		builder.append('\n');
	}
}
