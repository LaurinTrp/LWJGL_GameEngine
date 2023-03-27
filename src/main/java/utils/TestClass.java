package main.java.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import glm.vec._3.Vec3;
import main.java.render.utilities.terrain.ProceduralTerrain;
import main.java.utils.math.MathFunctions;
import main.java.utils.math.SimplexNoise;

public class TestClass extends Thread implements Runnable {

	@Override
	public void run() {
//		BufferedImage image0 = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
//		image0.setRGB(0, 0, Color.GREEN.getRGB());
//		System.out.println(Arrays.toString(image0.getRaster().getPixel(0, 0, new double[4])));
//		
//		BufferedImage image1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
//		image1.setRGB(0, 0, Color.BLUE.getRGB());
//		System.out.println(Arrays.toString(image1.getRaster().getPixel(0, 0, new double[4])));
		
		ProceduralTerrain pt = new ProceduralTerrain(100, 1, 0, 0);
		pt.generateHeightMap();
		
		try {
			ImageIO.write(pt.getHeightMap(), "PNG", new File("imageOutputs/Test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestClass testClass = new TestClass();
		testClass.start();
	}
}