package click.rmx.engine.gl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


import click.rmx.Bugger;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexProgram;

import static org.lwjgl.opengl.ARBVertexShader.GL_VERTEX_SHADER_ARB;
import static org.lwjgl.opengl.ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
import org.lwjgl.opengl.GL11;



import static click.rmx.RMX.rmxGetPackageUrl;
public class Shader {
	
	
	private final int defaultVertexShader() throws Exception {
		String url = "src/main/resources/shaders/Shader.vsh";
		Bugger.log(url);
		return createShader(url, GL_VERTEX_SHADER_ARB);
	}
	
	private final int defaultFragmentShader() throws Exception {
		String url = "src/main/resources/shaders/Shader.fsh";
		Bugger.log(url);
		return createShader(url, GL_FRAGMENT_SHADER_ARB);
	}
	
	public static final int DEFAULT = 0;
	
	public final int vertexShader;
	public final int fragmentShader;
	public final int program;

	private boolean useShader;
	
	public Shader(int type) throws Exception {
		switch (type) {
		case DEFAULT:
		default:
			this.vertexShader = defaultVertexShader();
			this.fragmentShader = defaultFragmentShader();
		}
		this.program = bindAndCreateProgram();
	}
	
	public Shader(String vertexShader, String fragmentShader) throws Exception {
		ARBShaderObjects.glCreateProgramObjectARB();
		this.vertexShader = createShader(vertexShader, GL_VERTEX_SHADER_ARB);
		this.fragmentShader = createShader(fragmentShader, GL_FRAGMENT_SHADER_ARB);
		this.program = bindAndCreateProgram();
	}
	
	private int bindAndCreateProgram() {
		int program = ARBShaderObjects.glCreateProgramObjectARB();
		ARBVertexProgram.glBindProgramARB(vertexShader, GL_VERTEX_SHADER_ARB);
		ARBVertexProgram.glBindProgramARB(fragmentShader, GL_FRAGMENT_SHADER_ARB);
		/*
		 * if the vertex and fragment shaders setup sucessfully,
		 * attach them to the shader program, link the sahder program
		 * (into the GL context I suppose), and validate
		 */
		ARBShaderObjects.glAttachObjectARB(program, this.vertexShader);
		ARBShaderObjects.glAttachObjectARB(program, this.fragmentShader);

		ARBShaderObjects.glLinkProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
			System.err.println(getLogInfo(program));
			return 0;
		}

		ARBShaderObjects.glValidateProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
			System.err.println(getLogInfo(program));
			return 0;
		}
		useShader = true;
		return program;
	}

	public static ARBVertexProgram program() {
		return ARBVertexProgram.getInstance();
	}
	
	/*
	 * With the exception of syntax, setting up vertex and fragment shaders
	 * is the same.
	 * @param the name and path to the vertex shader
	 */
	private int createShader(String filename, int shaderType) throws Exception {
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if(shader == 0)
				return 0;

			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

			return shader;
		}
		catch(Exception exc) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw exc;
		}
	}

	private static String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	private String readFileAsString(String filename) throws Exception {
		StringBuilder source = new StringBuilder();

		FileInputStream in = new FileInputStream(filename);

		Exception exception = null;

		BufferedReader reader;
		try{
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));

			Exception innerExc= null;
			try {
				String line;
				while((line = reader.readLine()) != null)
					source.append(line).append('\n');
			}
			catch(Exception exc) {
				exception = exc;
			}
			finally {
				try {
					reader.close();
				}
				catch(Exception exc) {
					if(innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if(innerExc != null)
				throw innerExc;
		}
		catch(Exception exc) {
			exception = exc;
		}
		finally {
			try {
				in.close();
			}
			catch(Exception exc) {
				if(exception == null)
					exception = exc;
				else
					exc.printStackTrace();
			}

			if(exception != null)
				throw exception;
		}

		return source.toString();
	}
}

