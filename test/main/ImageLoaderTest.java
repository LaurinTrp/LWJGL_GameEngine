package main;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import main.java.utils.ImageUtils;

public class ImageLoaderTest {
	
	public static long loadTexture(BufferedImage image) {
		ByteBuffer buffer = ImageUtils.imageToByteBuffer(image);
		
		long id = ImageUtils.createTextureFromByteGray(buffer, image.getWidth(), image.getHeight());

	    return id; 
	}

}
