package main.java.data.animation;

import glm.mat._4.Mat4;

public class AnimatedFrame {
	
	private Mat4[] boneMatrices;

	public AnimatedFrame(Mat4[] boneMatrices) {
		this.boneMatrices = boneMatrices;
	}

	public Mat4[] boneMatrices() {
		return boneMatrices;
	}
	
	public void print() {
		for (int i = 0; i < 10; i++) {
			boneMatrices[i].print();
			System.out.println();
		}
//		for (Mat4 mat4 : boneMatrices) {
//			mat4.print();
//			System.out.println();
//		}
	}
}
