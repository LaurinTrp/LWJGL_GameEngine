package svaev;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.java.utils.math.PerlinNoise;

public class rstva {
	public static void main(String[] args) {
		final int WIDTH = 2 * 256, HEIGHT = 2*256;

		double[] data = new double[WIDTH * HEIGHT];
		int count = 0;

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				data[count++] = PerlinNoise.noise(20.0 * x / WIDTH, 10.0 * y / HEIGHT, 0);
			}
		}

		double minValue = data[0], maxValue = data[0];
		for (int i = 0; i < data.length; i++) {
			minValue = Math.min(data[i], minValue);
			maxValue = Math.max(data[i], maxValue);
		}

		int[] pixelData = new int[WIDTH * HEIGHT];
		for (int i = 0; i < data.length; i++) {
			pixelData[i] = (int) (255 * (data[i] - minValue) / (maxValue - minValue));
		}

		BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
		img.getRaster().setPixels(0, 0, WIDTH, HEIGHT, pixelData);
		
		try {
			ImageIO.write(convertTo4ByteABGR(img), "PNG", new File("imageOutputs/test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static BufferedImage convertTo4ByteABGR(BufferedImage grayImage) {
        int width = grayImage.getWidth();
        int height = grayImage.getHeight();

        // Create a new BufferedImage with the desired type
        BufferedImage abgrImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        // Get the graphics object for the new image
        Graphics2D g2d = abgrImage.createGraphics();

        // Draw the gray image onto the new image using a color conversion
        g2d.drawImage(grayImage, 0, 0, null);

        // Dispose the graphics object to free resources
        g2d.dispose();

        return abgrImage;
    }
}
