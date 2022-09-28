package main.java.utils.math;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

public class Glm {
	public static Vec3 normalize(Vec3 vector) {
		float length = (float) Math.sqrt((vector.x * vector.x) + (vector.y * vector.y) + (vector.z * vector.z));
		return new Vec3(vector.x / length, vector.y / length, vector.z / length);
	}

	public static Vec3 subtract(Vec3 vector1, Vec3 vector2) {
		return new Vec3(vector1.x - vector2.x, vector1.y - vector2.y, vector1.z - vector2.z);
	}

	public static Vec3 add(Vec3 vector1, Vec3 vector2) {
		return new Vec3(vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z);
	}

	public static Vec3 times(Vec3 vector1, Vec3 vector2) {
		return new Vec3(vector1.x * vector2.x, vector1.y * vector2.y, vector1.z * vector2.z);
	}

	public static Vec3 times(Vec3 vector1, float multiplier) {
		return new Vec3(vector1.x * multiplier, vector1.y * multiplier, vector1.z * multiplier);
	}

	public static Vec3 cross(Vec3 vector1, Vec3 vector2) {
		float x = vector1.y * vector2.z - vector1.z * vector2.y;
		float y = vector1.z * vector2.x - vector1.x * vector2.z;
		float z = vector1.x * vector2.y - vector1.y * vector2.x;

		return new Vec3(x, y, z);
	}
	
	public static Vec3 matTranslation(Mat4 matrix) {
		float x = matrix.m30;
		float y = matrix.m31;
		float z = matrix.m32;
		return new Vec3(x, y, z);
	}

	

}
