package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.java.utils.math.PerlinNoise.GPT_Generated;

public class TestClass extends Thread implements Runnable {

	@Override
	public void run() {
		createNoiseImage();
	}
	public static BufferedImage createNoiseImage() {
		final int WIDTH = 2 * 256, HEIGHT = 2*256;

		double[] data = new double[WIDTH * HEIGHT];
		int count = 0;

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				data[count++] = GPT_Generated.perlinNoise2D(20.0 * x / WIDTH, 10.0 * y / HEIGHT);
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
			ImageIO.write(img, "PNG", new File("imageOutputs/testImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	public static void main(String[] args) {
		TestClass testClass = new TestClass();
		testClass.start();
	}
}