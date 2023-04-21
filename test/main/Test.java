package main;

import java.util.Random;

import glm.vec._2.Vec2;

public class Test {
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			test(new Vec2(0f, 10f));
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void test(Vec2 xMinMax) {
		Random random = new Random(System.currentTimeMillis());
		
	}
}
