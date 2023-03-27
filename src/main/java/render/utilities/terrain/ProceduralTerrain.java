package main.java.render.utilities.terrain;

import java.awt.Color;
import java.awt.image.BufferedImage;

import main.java.utils.loaders.ImageLoader;
import main.java.utils.math.PerlinNoise;

public class ProceduralTerrain {

	private BufferedImage heightMap;
	private int startX, startZ, size, density;
	private int heightMapId = -1;

	public ProceduralTerrain(int size, int density, int startX, int startZ) {
		heightMap = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		this.startX = startX;
		this.startZ = startZ;
		this.size = size;
		this.density = density;
	}

	public void generateHeightMap() {
		
//		for (int i = 0; i < heightMap.getWidth(); i++) {
//			for (int j = 0; j < heightMap.getHeight(); j++) {
//				heightMap.setRGB(i, j, Color.RED.getRGB());
//			}
//		}
//		heightMapId = ImageLoader.loadTextureFromBufferedImage(heightMap);
		
		final int WIDTH = size, HEIGHT = size;
		
		double[] data = new double[WIDTH * HEIGHT * 4];
		int count = 0;

		for (int z = startZ; z < HEIGHT + startZ; z++) {
			for (int x = startX; x < WIDTH + startX; x++) {
				data[count++] = PerlinNoise.noise(20.0 * x / WIDTH, 0, 10.0 * z / HEIGHT);
				data[count++] = PerlinNoise.noise(20.0 * x / WIDTH, 0, 10.0 * z / HEIGHT);
				data[count++] = PerlinNoise.noise(20.0 * x / WIDTH, 0, 10.0 * z / HEIGHT);
			}
		}

		double minValue = data[0], maxValue = data[0];
		for (int i = 0; i < data.length; i++) {
			minValue = Math.min(data[i], minValue);
			maxValue = Math.max(data[i], maxValue);
		}

		int[] pixelData = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			pixelData[i] = (int) (255 * (data[i] - minValue) / (maxValue - minValue));
		}
		heightMap.getRaster().setPixels(0, 0, WIDTH, HEIGHT, pixelData);

		
		if (heightMapId == -1) {
			heightMapId = ImageLoader.loadTextureFromBufferedImage(heightMap);
		} else {
			ImageLoader.updateTexture(heightMapId, heightMap);
		}
	}

//	public void setStartX(float startX) {
//		this.startX = startX / size;
//	}
//
//	public void setStartY(float startY) {
//		this.startZ = startY / size;
//	}

	public BufferedImage getHeightMap() {
		return heightMap;
	}

	public int getHeightMapId() {
		return heightMapId;
	}

}
