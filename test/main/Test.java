package main;

import java.util.Arrays;

import glm.mat._4.Mat4;

public class Test {
	public static void main(String[] args) {
		Mat4 mat = new Mat4(1.0f);
		mat = mat.translate(10, 20, 30);
		float[] fa = mat.toFa_();
		System.out.println(Arrays.toString(fa));
	}
}
