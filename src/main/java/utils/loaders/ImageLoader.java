package main.java.utils.loaders;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_RGB;
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
import static org.lwjgl.opengl.GL11.glTexSubImage2D;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import main.java.utils.ImageUtils;
import resources.ResourceLoader;

public class ImageLoader {

	/**
	 * Load a image from the memory
	 *
	 * @param path The path to the image
	 * @return Returning the OpenGL texture id
	 * @throws Exception if the image could not be loaded
	 */
	public static int loadTextureFromMemory(String path) throws Exception {
		int width, height;
		ByteBuffer buffer;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);

			buffer = STBImage.stbi_load(path, w, h, c, 4);
			if (buffer == null) {
				throw new Exception("Image File " + path + " not loaded " + STBImage.stbi_failure_reason());
			}

			width = w.get();
			height = h.get();
		}
		return getImageID(width, height, buffer);
	}

	/**
	 * Loading a texture from the resources
	 *
	 * @param fileName Filename of the image in the res/Textures folder
	 * @return Returning the OpenGL texture id
	 */
	public static int loadTextureFromResource(String fileName) {
		try {
			return loadTexture(ResourceLoader.loadTexture(fileName));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static ByteBuffer bufferedImageToByteBuffer(BufferedImage image) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		InputStream imageFile = new ByteArrayInputStream(baos.toByteArray());

		byte[] imageData = IOUtils.toByteArray(imageFile);
		ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageData.length);
		imageBuffer.put(imageData);
		imageBuffer.flip();

		return imageBuffer;
	}

	/**
	 * Load a texture from a byte buffer
	 *
	 * @param data The data of the image as an byte buffer
	 * @return Returning the OpenGL texture id
	 * @throws Exception if the image loading failed
	 */
	public static int loadTexture(ByteBuffer data) throws Exception {
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

	public static int loadSkybox(String[] files) throws Exception {
		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);
		int width, height;
		for (int i = 0; i < files.length; i++) {
			ByteBuffer data = ResourceLoader.loadTexture(files[i]);
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
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE,
					buffer);
		}
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

		return textureID;
	}

	public static void updateTexture(int id, BufferedImage image) {
		glBindTexture(GL_TEXTURE_2D, id);
		try {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
					bufferedImageToByteBuffer(image));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void updateTextureHeightMap(int id, BufferedImage image) {
		// Bind the texture
	    glBindTexture(GL_TEXTURE_2D, id);

	    ByteBuffer buffer = ImageUtils.imageToByteBuffer(image);
	    // Update the texture data
	    glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, image.getWidth(), image.getHeight(), GL_RED, GL_UNSIGNED_BYTE, buffer);

	    // Unbind the texture
	    glBindTexture(GL_TEXTURE_2D, 0);
	}

	/**
	 * Setting the image parameter
	 *
	 * @param width  Image width
	 * @param height Image height
	 * @param buffer Image Data
	 * @return Returning the OpenGL texture id
	 */
	private static int getImageID(int width, int height, ByteBuffer buffer) {
		int id = glGenTextures();

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
		float[] borderColor = { 1.0f, 0.0f, 0.0f, 1.0f };
		glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glGenerateMipmap(GL_TEXTURE_2D);

		return id;
	}

}
