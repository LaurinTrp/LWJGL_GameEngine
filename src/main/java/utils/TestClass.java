package main.java.utils;

import glm.vec._3.Vec3;
import main.java.render.utilities.terrain.ProceduralTerrain;
import main.java.utils.math.MathFunctions;

public class TestClass extends Thread implements Runnable {

	@Override
	public void run() {
		ProceduralTerrain pt = new ProceduralTerrain(0, 0, 100, 100, 0.5f);
		pt.generateHeightMap();
	}


	public static void main(String[] args) {
		TestClass testClass = new TestClass();
		testClass.start();
	}
}
