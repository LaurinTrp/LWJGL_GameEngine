package main.java.render.utilities.terrain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.java.utils.math.SimplexNoise;

public class ProceduralTerrain {

	private BufferedImage heightMap;
	private float startX, startY, imageWidth, imageHeight, density;

	public ProceduralTerrain(float startX, float startY, int width, int height, float density) {
		imageWidth = width / density;
		imageHeight = height / density;
		heightMap = new BufferedImage((int) imageWidth, (int) imageHeight, BufferedImage.TYPE_BYTE_GRAY);
		this.startX = startX;
		this.startY = startY;
		this.density = density;
	}

	public void generateHeightMap() {
		for (int x = 0; x < heightMap.getWidth(); x++) {
			for (int y = 0; y < heightMap.getHeight(); y++) {
				double noise = (SimplexNoise.noise(x + startX / density, y + startY / density) + 1) / 2d;
				heightMap.setRGB(x, y,
						new Color((int) (noise * 255), (int) (noise * 255), (int) (noise * 255)).getRGB());
			}
		}
		try {
			ImageIO.write(heightMap, "PNG", new File("imageOutputs/heightMap2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
