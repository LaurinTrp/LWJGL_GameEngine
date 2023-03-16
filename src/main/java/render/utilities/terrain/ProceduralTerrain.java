package main.java.render.utilities.terrain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.java.utils.loaders.ImageLoader;
import main.java.utils.math.SimplexNoise;

public class ProceduralTerrain {

	private BufferedImage heightMap;
	private float startX, startZ, size, density;
	private int heightMapId = -1;

	public ProceduralTerrain(float startX, float startZ, float size, float density) {
		float imageWidth = size / density;
		float imageHeight = size / density;
		heightMap = new BufferedImage((int) imageWidth, (int) imageHeight, BufferedImage.TYPE_INT_ARGB);
		this.startX = startX;
		this.startZ = startZ;
		this.size = size;
		this.density = density;
	}
	
	public void generateHeightMap() {
		for (int x = 0; x < heightMap.getWidth(); x++) {
			for (int y = 0; y < heightMap.getHeight(); y++) {
				double noise = (SimplexNoise.noise(x + startX / density, y + startZ / density) + 1) / 2d;
				heightMap.setRGB(x, y,
						new Color((int) (noise * 255), (int) (noise * 255), (int) (noise * 255)).getRGB());
			}
		}
		
		heightMapId = ImageLoader.loadTextureFromBufferedImage(heightMap);
	}
	
	public void setStartX(float startX) {
		this.startX = startX/size;
	}

	public void setStartY(float startY) {
		this.startZ = startY/size;
	}

	public BufferedImage getHeightMap() {
		return heightMap;
	}
	
	public int getHeightMapId() {
		return heightMapId;
	}
	
}
