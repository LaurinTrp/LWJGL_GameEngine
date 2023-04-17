package main.java.utils;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;

public class ImageUtils {

	public static int createTextureFromByteGray(ByteBuffer buffer, int width, int height) {
	    // Generate a new texture ID
	    int textureId = glGenTextures();

	    // Bind the texture
	    glBindTexture(GL_TEXTURE_2D, textureId);

	    // Set texture parameters
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

	    // Upload the texture data to the GPU
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, width, height, 0, GL_RED, GL_UNSIGNED_BYTE, buffer);

	    // Unbind the texture
	    glBindTexture(GL_TEXTURE_2D, 0);

	    return textureId;
	}
	
	public static int createTextureFromImage(BufferedImage image) {
	    int width = image.getWidth();
	    int height = image.getHeight();

	    ByteBuffer buffer = imageToByteBuffer(image);

	    int textureId = glGenTextures();
	    glBindTexture(GL_TEXTURE_2D, textureId);
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, width, height, 0, GL_RED, GL_UNSIGNED_BYTE, buffer);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

	    return textureId;
	}
	
	public static void updateTextureWithGrayImage(int id, BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		glBindTexture(GL_TEXTURE_2D, id);
		ByteBuffer buffer = imageToByteBuffer(image);
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, width, height, 0, GL_RED, GL_UNSIGNED_BYTE, buffer);
	    glBindTexture(GL_TEXTURE_2D, 0);
	}

	public static ByteBuffer imageToByteBuffer(BufferedImage image) {
	    int width = image.getWidth();
	    int height = image.getHeight();

	    byte[] imageData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	    ByteBuffer buffer = ByteBuffer.allocateDirect(width * height);
	    buffer.put(imageData);
	    buffer.rewind();

	    return buffer;
	}
	
	public static float getFloatValueFromByteGrayImage(BufferedImage image, int x, int y) {
	    // Get the raster of the image
	    WritableRaster raster = image.getRaster();
	    
	    // Get the byte value of the pixel at the specified position
	    byte[] pixelData = new byte[1];
	    raster.getDataElements(x, y, pixelData);
	    
	    // Convert the byte value to a float between 0 and 1
	    float floatValue = ((float)(pixelData[0] & 0xff)) / 255.0f;
	    
	    return floatValue;
	}
}
