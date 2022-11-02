package main.java.shader;

import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import resources.ResourceLoader;


public class ShaderProgram {

	private int programID = 0;
	private int vertexID = 0;
	private int fragmentID = 0;
	private int textureID = 0;
	
	private String path;
	
	public ShaderProgram(String path) {
		this.path = path;
		
		vertexID = loadShader(ResourceLoader.loadShader(path, "vertex.glsl"), GL20.GL_VERTEX_SHADER);
		fragmentID = loadShader(ResourceLoader.loadShader(path, "fragment.glsl"), GL20.GL_FRAGMENT_SHADER);
		
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexID);
		GL20.glAttachShader(programID, fragmentID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
	}

	private int loadShader(InputStream stream, int type) {
		String shaderSource = "";
		try {
			shaderSource = new String(stream.readAllBytes());
			shaderSource = shaderSource.replaceAll("\\/\\/.*", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		shaderSource = replacedIncludes(shaderSource);
		
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader.");
            System.exit(-1);
        }
        
        return shaderID;			
	}
	
	private String replacedIncludes(String shaderSource) {
		String[] lines = shaderSource.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if(lines[i].contains("#include")) {
				String line = lines[i];
				line = line.replaceAll("[<|>]", "").replace("#include", "");
				line = line.strip();
				String folder = path;
				if(line.contains("/")) {
					int last = line.lastIndexOf("/");
					folder = line.substring(0, last);
					line = line.replace(folder+"/", "");
				}
				String shader = line;
				
				String loadedShader = ResourceLoader.loadShaderAsString(folder, shader);
				loadedShader = loadedShader.replaceAll("#version.*", "");
				lines[i] = loadedShader;
			}
		}
		String newShaderCode = "";
		for (String string : lines) {
			newShaderCode+=string + "\n";
		}
		
		return newShaderCode;
	}
	
	public int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	public void dispose() {
		GL20.glDeleteShader(vertexID);
		GL20.glDeleteShader(fragmentID);
		GL20.glDeleteProgram(programID);
		GL20.glDeleteProgram(textureID);
	}
	
	public int getProgramID() {
		return programID;
	}
	
	public int getTextureID() {
		return textureID;
	}
	
}
