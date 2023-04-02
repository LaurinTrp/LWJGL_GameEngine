package main.java.render.utilities.terrain;

import java.awt.image.BufferedImage;

import main.java.utils.ImageUtils;
import main.java.utils.math.Noise;
import main.java.utils.math.PerlinNoise;

public class ProceduralTerrain {

	private BufferedImage heightMap;
	private int size;
	private float density;
	private int heightMapID = -1;
	
	public ProceduralTerrain(int size, float density) {
		heightMap = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
		this.size = size;
		this.density = density;
	}
	
	public void generateHeightMap(float offsetX, float offsetY) {
		heightMap = Noise.createNoiseImage(size/density, size/density, offsetX, offsetY);
		if(heightMapID == -1) {
			heightMapID = ImageUtils.createTextureFromImage(heightMap);
		}
	}
	
	public int getHeightMapID() {
		ImageUtils.updateTextureWithGrayImage(heightMapID, heightMap);
		return heightMapID;
	}

}
