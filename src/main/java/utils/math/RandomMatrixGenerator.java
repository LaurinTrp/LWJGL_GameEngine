package main.java.utils.math;

import java.util.Random;

import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.utils.math.PerlinNoise.GPT_Generated;

public class RandomMatrixGenerator {

	public static Mat4[] generateRandomWithHeight(int size, Vec2 xMinMax, Vec2 zMinMax, Vec2 scaleMinMax) {
		Mat4[] matrices = new Mat4[size];
		for (int i = 0; i < matrices.length; i++) {
			Random random = new Random(System.nanoTime());
			Mat4 mat = new Mat4();
			mat.rotate((float) Math.toRadians(random.nextInt(360)), new Vec3(0.0, 1.0, 0.0));
			
			float randX = (random.nextInt((int) (xMinMax.y * 10) - (int) (xMinMax.x * 10)) + xMinMax.x * 10) / 10f;
			float randZ = (random.nextInt((int) (zMinMax.y * 10) - (int) (zMinMax.x * 10)) + zMinMax.x * 10) / 10f;
			float y = (float) GPT_Generated.perlinNoise2D(randX, randZ);
			Vec3 translation = new Vec3(randX, y, randZ);
			mat.translate(translation);

			float scale = (random.nextInt((int) (scaleMinMax.y * 10) - (int) (scaleMinMax.x * 10)) + scaleMinMax.x * 10) / 10f;
			mat.scale(scale);
			
			matrices[i] = mat;
		}
		return matrices;
	}
}
