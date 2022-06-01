package paulevs.bhcore.rendering.shaders;

import paulevs.bhcore.rendering.shaders.complex.ComplexShaderProgram;

// Will be moved to graphical mod
@Deprecated(forRemoval = true)
public class DefaultShaderPrograms {
	public static final ComplexShaderProgram TERRAIN = ComplexShaderProgram.create("terrain");
	
	public static void init() {}
}
