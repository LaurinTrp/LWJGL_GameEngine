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
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImageResize.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class ResourceLoader {

	static ResourceLoader rl = new ResourceLoader();

	public static Image loadImage(String imageName) {
		System.out.println(rl.getClass().getResource("images/" + imageName));
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

	public static ByteBuffer loadTexture(String fileName) {
		InputStream imageFile = ResourceLoader.class.getResourceAsStream("Textures/" + fileName);
		byte[] imageData;
		try {
			imageData = IOUtils.toByteArray(imageFile);
			ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageData.length);
			imageBuffer.put(imageData);
			imageBuffer.flip();
			return imageBuffer;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
//		try {
//			URL url = rl.getClass().getResource("Textures/" + fileName);
//			URI uri = url.toURI();
//			File file = new File(uri);
//			return file;
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//			return null;
//		}
	}

	public static void main(String[] args) {
		int width, height;
		ByteBuffer buffer;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			
			ByteBuffer imageBuffer = ResourceLoader.loadTexture("Tex.png");

			buffer = STBImage.stbi_load_from_memory(imageBuffer, w, h, c, 0);
			if (buffer == null) {
				throw new Exception("Image File not loaded " + STBImage.stbi_failure_reason());
			}

			width = w.get();
			height = h.get();

		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}