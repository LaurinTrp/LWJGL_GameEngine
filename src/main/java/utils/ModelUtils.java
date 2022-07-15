package main.java.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import glm.glm.vec._4.Vec4;
import main.java.shader.ShaderProgram;

public class ModelUtils {
	public static float[] flattenArrays(float[] vertices, float[] colors, float[] uvs, float[] normals) {
		int count = 0;
		if (vertices != null)
			count += vertices.length;
		if (colors != null)
			count += colors.length;
		if (uvs != null)
			count += uvs.length;
		if (normals != null)
			count += normals.length;
		float[] output = new float[count];
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

	public static float[] flattenListOfListsStream(ArrayList<ArrayList<Vec4>> arrayList) {
		List<Vec4> list = arrayList.stream().flatMap(Collection::stream).collect(Collectors.toList());
		float[] array = new float[list.size() * 4];
		for (int i = 0; i < list.size(); i++) {
			array[i * 4 + 0] = list.get(i).x;
			array[i * 4 + 1] = list.get(i).y;
			array[i * 4 + 2] = list.get(i).z;
			array[i * 4 + 3] = list.get(i).w;
		}
		return array;
	}

	public static void createUniform(ShaderProgram program, HashMap<String, Integer> uniforms, String name) {
		if (program == null) {
			return;
		}
		int id = program.getUniformLocation(name);
		uniforms.put(name, id);
	}

}
