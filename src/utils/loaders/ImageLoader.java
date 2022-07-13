package utils.loaders;

import java.io.BufferedReader;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import gui.Engine_Main;

public class ImageLoader {
	
	
	public static int loadTextureFromResource(String fileName) throws Exception {
		fileName = Engine_Main.PATHS.TEXTURE_PATH + fileName;
		return loadTexture(fileName);
	}
	
	public static int loadTexture(String path) throws Exception {
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
		return id;
	}

	public static void byteBuffer2BufferedImage(ByteBuffer bb, BufferedImage bi) {
        final int bytesPerPixel = 3;
        byte[] imageArray = ((DataBufferByte) bi.getRaster()
                .getDataBuffer()).getData();
        bb.rewind();
        bb.get(imageArray);
        int numPixels = bb.capacity() / bytesPerPixel;
        for (int i = 0; i < numPixels; i++) {
            byte tmp = imageArray[i * bytesPerPixel];
            imageArray[i * bytesPerPixel] = imageArray[i * bytesPerPixel
                    + 2];
            imageArray[i * bytesPerPixel + 2] = tmp;
        }
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
