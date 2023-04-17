package main.java.utils.math;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.java.utils.math.PerlinNoise.GPT_Generated;

public class Noise {
//	public static BufferedImage createNoiseImage(float width, float height, float offsetX, float offsetY) {
//		BufferedImage img = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_BYTE_GRAY);
//		for (int i = 0; i < width; i++) {
//			for (int j = 0; j < height; j++) {
//				img.setRGB(i, j,  Color.BLACK.getRGB());
//			}
//		}
//		return img;
//	}
	public static BufferedImage createNoiseImage(float width, float height, float offsetX, float offsetY) {
		final int WIDTH = (int)width, HEIGHT = (int)height;

		double[] data = new double[WIDTH * HEIGHT];
		int count = 0;

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				data[count++] = GPT_Generated.perlinNoise2D(8.0 * (x+offsetX) / WIDTH, 8.0 * (y+offsetY) / HEIGHT);
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
		
		return img;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {

			BufferedImage img = Noise.createNoiseImage(200, 200, i, 0);
			try {
				ImageIO.write(img, "PNG", new File("imageOutputs/testImage" + i + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
