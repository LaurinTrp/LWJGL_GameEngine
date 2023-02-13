package main.java.utils.loaders;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.*;
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
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import resources.ResourceLoader;

public class ImageLoader {
	
	/**
	 * Load a image from the memory
	 * @param path The path to the image
	 * @return Returning the OpenGL texture id
	 * @throws Exception if the image could not be loaded
	 */
	public static int loadTextureFromMemory(String path) throws Exception {
		int width, height;
		ByteBuffer buffer;
		try(MemoryStack stack = MemoryStack.stackPush()){
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			
			buffer = STBImage.stbi_load(path, w, h, c, 4);
			if(buffer == null) {
				throw new Exception("Image File " + path + " not loaded " + STBImage.stbi_failure_reason());
			}
			
			width = w.get();
			height = h.get();
		}
		return getImageID(width, height, buffer);
	}
	
	/**
	 * Loading a texture from the resources
	 * @param fileName Filename of the image in the res/Textures folder
	 * @return Returning the OpenGL texture id
	 */
	public static int loadTextureFromResource(String fileName) {
		try {
			System.out.println("FILE: " + fileName);
			return loadTexture(ResourceLoader.loadTexture(fileName));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Load a texture from a byte buffer
	 * @param data The data of the image as an byte buffer
	 * @return Returning the OpenGL texture id
	 * @throws Exception if the image loading failed
	 */
	private static int loadTexture(ByteBuffer data) throws Exception {
		int width, height;
		ByteBuffer buffer;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			
			buffer = STBImage.stbi_load_from_memory(data, w, h, c, 0);
			if (buffer == null) {
				throw new Exception("Image File not loaded " + STBImage.stbi_failure_reason());
			}

			width = w.get();
			height = h.get();

		}
		return getImageID(width, height, buffer);
	}

	/**
	 * Setting the image parameter
	 * @param width Image width
	 * @param height Image height
	 * @param buffer Image Data
	 * @return Returning the OpenGL texture id
	 */
	private static int getImageID(int width, int height, ByteBuffer buffer) {
		int id = glGenTextures();

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
		float[] borderColor = {1.0f, 0.0f, 0.0f, 1.0f};
		glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		System.out.println(width + "\t" + height);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		return id;
	}
	
	public static class ImageParams {
		public int width, height;
		public ByteBuffer buffer;

		public ImageParams(int width, int height, ByteBuffer buffer) {
			this.width = width;
			this.height = height;
			this.buffer = buffer;
		}
	}

}
