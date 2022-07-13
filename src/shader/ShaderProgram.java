package shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import gui.Engine_Main;


public class ShaderProgram {

	private int programID = 0;
	private int vertexID = 0;
	private int fragmentID = 0;
	private int textureID = 0;
	
	public ShaderProgram(String path) {
		path = Engine_Main.PATHS.SHADER_PATH + path + "/";
		vertexID = loadShader(path + "vertex.glsl", GL20.GL_VERTEX_SHADER);
		fragmentID = loadShader(path + "fragment.glsl", GL20.GL_FRAGMENT_SHADER);
		
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexID);
		GL20.glAttachShader(programID, fragmentID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
	}

	private int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append(System.getProperty("line.separator"));
            }
           
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
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
