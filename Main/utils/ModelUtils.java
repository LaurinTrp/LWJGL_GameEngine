package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import shader.ShaderProgram;

public class ModelUtils {
	public static float[] flattenArrays(float[] vertices, float[] colors, float[] uvs, float[] normals) {
		float[] output = new float[vertices.length + colors.length + uvs.length + normals.length];
		ArrayList<Float> list = new ArrayList<Float>();
		for (int i = 0; i < vertices.length; i += 4) {
			if (vertices != null) {
				for (int j = 0; j < 4; j++) {
					list.add(vertices[i + j]);
				}
			}
			if (colors != null) {
				for (int j = 0; j < 4; j++) {
					list.add(colors[i + j]);
				}
			}
			if (uvs != null) {
				for (int j = 0; j < 4; j++) {
					list.add(uvs[i + j]);
				}
			}
			if (normals != null) {
				for (int j = 0; j < 4; j++) {
					list.add(normals[i + j]);
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			output[i] = list.get(i);
		}
		return output;
	}
	
	public static float[] createNormals(float[] vertices, float[] normals) {
		float[] output = new float[vertices.length + normals.length];
		ArrayList<Float> list = new ArrayList<Float>();
		for (int i = 0; i < vertices.length; i += 4) {
			for (int j = 0; j < 4; j++) {
				list.add(vertices[i + j]);
			}
			for (int j = 0; j < 3; j++) {
				list.add(normals[i + j] + vertices[i + j]);
			}
			list.add(1.0f);
		}
		for (int i = 0; i < list.size(); i++) {
			output[i] = list.get(i);
		}
		return output;
	}
	

	public static void createUniform(ShaderProgram program, HashMap<String, Integer> uniforms, String name) {
		if (program == null) {
			return;
		}
		int id = program.getUniformLocation(name);
		uniforms.put(name, id);
	}
	
}
