package main.java.utils;

import glm.vec._3.Vec3;
import main.java.utils.math.MathFunctions;

public class TestClass extends Thread implements Runnable {

	@Override
	public void run() {
		
		Vec3 v0 = new Vec3(1, 0, 0);
		Vec3 v1 = new Vec3(0, 0, 1);
		System.out.println(MathFunctions.VECTOR_MATH.distance(v0, v1));
	}
	
	
	public static void main(String[] args) {
		TestClass testClass = new TestClass();
		testClass.start();
	}
}
