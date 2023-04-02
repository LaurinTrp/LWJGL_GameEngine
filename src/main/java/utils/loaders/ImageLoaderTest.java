package main.java.utils.loaders;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import main.java.utils.ImageUtils;

public class ImageLoaderTest {
	
	public static long loadTexture(BufferedImage image) {
		ByteBuffer buffer = ImageUtils.imageToByteBuffer(image);
		
		long id = ImageUtils.createTextureFromByteGray(buffer, image.getWidth(), image.getHeight());

	    return id; 
	}

}
