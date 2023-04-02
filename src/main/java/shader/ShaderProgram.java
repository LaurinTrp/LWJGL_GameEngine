package main.java.shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.io.IOException;
import java.io.InputStream;

import resources.ResourceLoader;

public class ShaderProgram {

	private int programID = 0;
	private int vertexID = 0;
	private int geometryID = 0;
	private int fragmentID = 0;
	private int textureID = 0;

	private String path;

	public ShaderProgram(String path) {
		this.path = path;

		vertexID = loadShader(ResourceLoader.loadShader(path, "vertex.glsl"), GL_VERTEX_SHADER);
		geometryID = loadShader(ResourceLoader.loadShader(path, "geometry.glsl"), GL_GEOMETRY_SHADER);
		fragmentID = loadShader(ResourceLoader.loadShader(path, "fragment.glsl"), GL_FRAGMENT_SHADER);

		programID = glCreateProgram();
		glAttachShader(programID, vertexID);
		glAttachShader(programID, geometryID);
		glAttachShader(programID, fragmentID);
		glLinkProgram(programID);
		glValidateProgram(programID);

	}

	private int loadShader(InputStream stream, int type) {
		if (stream == null) {
			return 0;
		}
		String shaderSource = "";
		try (stream) {
			shaderSource = new String(stream.readAllBytes());
			shaderSource = shaderSource.replaceAll("\\/\\/.*", "");
		} catch (IOException e) {
			e.printStackTrace();
		}

		shaderSource = replacedIncludes(shaderSource);

		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.out.println(glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader.");
			new Exception().printStackTrace();
			System.exit(-1);
		}

		return shaderID;
	}

	private String replacedIncludes(String shaderSource) {
		String[] lines = shaderSource.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("#include")) {
				String line = lines[i];
				line = line.replaceAll("[<|>]", "").replace("#include", "");
				line = line.strip();
				String folder = path;
				if (line.contains("/")) {
					int last = line.lastIndexOf("/");
					folder = line.substring(0, last);
					line = line.replace(folder + "/", "");
				}
				String shader = line;

				String loadedShader = ResourceLoader.loadShaderAsString(folder, shader);
				loadedShader = loadedShader.replaceAll("#version.*", "");
				lines[i] = loadedShader;
			}
		}
		String newShaderCode = "";
		for (String string : lines) {
			newShaderCode += string + "\n";
		}

		return newShaderCode;
	}

	public int getUniformLocation(String uniformName) {
		return glGetUniformLocation(programID, uniformName);
	}

	public void dispose() {
		glDeleteShader(vertexID);
		glDeleteShader(geometryID);
		glDeleteShader(fragmentID);
		glDeleteProgram(programID);
		glDeleteProgram(textureID);
	}

	public int getProgramID() {
		return programID;
	}

	public int getTextureID() {
		return textureID;
	}

}
