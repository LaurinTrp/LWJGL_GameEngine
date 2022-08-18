package main.java.shader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import main.java.gui.Engine_Main;
import resources.ResourceLoader;


public class ShaderProgram {

	private int programID = 0;
	private int vertexID = 0;
	private int fragmentID = 0;
	private int textureID = 0;
	
	public ShaderProgram(String path) {
		vertexID = loadShader(ResourceLoader.loadShader(path, "vertex.glsl"), GL20.GL_VERTEX_SHADER);
		fragmentID = loadShader(ResourceLoader.loadShader(path, "fragment.glsl"), GL20.GL_FRAGMENT_SHADER);
		
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexID);
		GL20.glAttachShader(programID, fragmentID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
	}

	private int loadShader(InputStream stream, int type) {
//		StringBuilder shaderSource = new StringBuilder();
//		System.out.println(file.exists());
//        try{
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//            String line;
//            while((line = reader.readLine())!=null){
//                shaderSource.append(line).append(System.getProperty("line.separator"));
//            }
//            System.out.println(line);
////        	String content
//           
//            reader.close();
//        }catch(IOException e){
//            e.printStackTrace();
//            System.exit(-1);
//        }
		String shaderSource = "";
		try {
			shaderSource = new String(stream.readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
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
