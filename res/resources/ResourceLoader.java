package resources;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
import java.nio.*;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;

import static java.lang.Math.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_BORDER_COLOR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterfv;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImageResize.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class ResourceLoader {

	static ResourceLoader rl = new ResourceLoader();

	public static Image loadImage(String imageName) {
		return Toolkit.getDefaultToolkit().getImage(rl.getClass().getResource("images/" + imageName));
	}

	public static InputStream loadFile(String fileName) {
		InputStream is = rl.getClass().getResourceAsStream("Files/" + fileName);
		return is;
	}

	public static InputStream loadShader(String parent, String fileName) {
		InputStream is = rl.getClass().getResourceAsStream("Shader/" + parent + "/" + fileName);
		return is;
	}
	
	public static String loadShaderAsString(String parent, String fileName) {
		try {
			return new String(loadShader(parent, fileName).readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int loadTexture(String fileName) throws Exception{
		InputStream imageFile = ResourceLoader.class.getResourceAsStream("Textures/" + fileName);
		byte[] imageData;
		try {
			imageData = IOUtils.toByteArray(imageFile);
			ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageData.length);
			imageBuffer.put(imageData);
			imageBuffer.flip();
			
			int width, height;
			ByteBuffer buffer;
			try(MemoryStack stack = MemoryStack.stackPush()){
				IntBuffer w = stack.mallocInt(1);
				IntBuffer h = stack.mallocInt(1);
				IntBuffer c = stack.mallocInt(1);
				
				buffer = STBImage.stbi_load_from_memory(imageBuffer, w, h, c, 4);
				if(buffer == null) {
					throw new Exception("Image File not loaded " + STBImage.stbi_failure_reason());
				}
				
				width = w.get();
				height = h.get();
			}
			int id = glGenTextures();

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
			float[] borderColor = {1.0f, 1.0f, 0.0f, 1.0f};
			glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			
			glBindTexture(GL_TEXTURE_2D, id);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			glGenerateMipmap(GL_TEXTURE_2D);
			
//			System.out.println(id);
			
			return id;
			
//			return imageBuffer;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static ArrayList<String> loadObjFile(String parentFolder, String fileName){
		InputStream modelFile = ResourceLoader.class.getResourceAsStream("Models/" + parentFolder + "/" + fileName);
		try {
			String content = new String(modelFile.readAllBytes());
			ArrayList<String> list = new ArrayList<>();
			for (String line : content.split("\n")) {
				list.add(line);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<String> loadMaterialFile(String parentFolder, String fileName){
		InputStream modelFile = ResourceLoader.class.getResourceAsStream("Models/" + parentFolder + "/" + fileName);
		try {
			String content = new String(modelFile.readAllBytes());
			ArrayList<String> list = new ArrayList<>();
			for (String line : content.split("\n")) {
				list.add(line);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
//		int width, height;
//		ByteBuffer buffer;
//		try (MemoryStack stack = MemoryStack.stackPush()) {
//			IntBuffer w = stack.mallocInt(1);
//			IntBuffer h = stack.mallocInt(1);
//			IntBuffer c = stack.mallocInt(1);
//			
//			ByteBuffer imageBuffer = ResourceLoader.loadTexture("Player/Player.png");
//
//			buffer = STBImage.stbi_load_from_memory(imageBuffer, w, h, c, 0);
//			if (buffer == null) {
//				throw new Exception("Image File not loaded " + STBImage.stbi_failure_reason());
//			}
//
//			width = w.get();
//			height = h.get();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}


	}
}