package main.java;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.java.render.utilities.terrain.ProceduralTerrain;

public class TestClass {
	public static void main(String[] args) {
		ProceduralTerrain pt = new ProceduralTerrain(100, 50, 0, 0);
		pt.generateHeightMap();
		BufferedImage image = pt.getHeightMap();
		try {
			ImageIO.write(image, "PNG", new File("imageOutputs/Test2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
