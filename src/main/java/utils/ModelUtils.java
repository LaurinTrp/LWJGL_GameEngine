package main.java.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.render.model.Material;
import main.java.render.model.Model;
import main.java.shader.ShaderProgram;

public class ModelUtils {
	public static float[] flattenArrays(Float[] vertices, Float[] colors, Float[] uvs, Float[] normals) {
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
		ArrayList<Float> list = new ArrayList<>();
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

	public static float[] createNormals(Float[] vertices, Float[] normals) {
		float[] output = new float[vertices.length + normals.length];
		ArrayList<Float> list = new ArrayList<>();
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

	public static Float[] flattenListOfListsStream(ArrayList<ArrayList<Vec4>> arrayList) {
		List<Vec4> list = arrayList.stream().flatMap(Collection::stream).collect(Collectors.toList());
		Float[] array = new Float[list.size() * 4];
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

	public static void compareModels(Model model0, Model model1) {
		System.out.println("#########################");
		System.out.println("Model Compare:");
		System.out.println("Vertices: " + Arrays.equals(model0.getVertices(), model1.getVertices()));
		System.out.println("Normals: " + Arrays.equals(model0.getNormals(), model1.getNormals()));
		System.out.println("Texture: " + Arrays.equals(model0.getUvs(), model1.getUvs()));
		compareMaterials(model0.getMaterial(), model1.getMaterial());
		System.out.println("#########################");
	}

	public static void compareMaterials(Material material0, Material material1) {
		System.out.println("Material Compare:");
		System.out.println("Ambient: " + (material0.getAmbient() == material1.getAmbient()));
		System.out.println("Diffuse: " + (material0.getDiffuse() == material1.getDiffuse()));
		System.out.println("Specular: " + (material0.getSpecular() == material1.getSpecular()));
		System.out.println("Texture: " + (material0.getTexture() == material1.getTexture()));
		System.out.println("Shininess: " + (material0.getReflectance() == material1.getReflectance()));
	}

	public static boolean isIntersecting(Float[] minmax0, Float[] minmax1) {
		return (minmax0[1] >= minmax1[0] && minmax0[0] <= minmax1[1] && minmax0[3] >= minmax1[2]
				&& minmax0[2] <= minmax1[3] && minmax0[5] >= minmax1[4] && minmax0[4] <= minmax1[5]);
	}

	public static Float[] calculateMinmax(Float[] startMinmax, Vec3 translation) {
		return calculateMinmax(startMinmax, new Vec4(translation, 1.0f));
	}

	public static Float[] calculateMinmax(Float[] startMinmax, Vec4 translation) {
		Float[] minmax = new Float[6];
		minmax[0] = startMinmax[0] + translation.x;
		minmax[1] = startMinmax[1] + translation.x;
		minmax[2] = startMinmax[2] + translation.y;
		minmax[3] = startMinmax[3] + translation.y;
		minmax[4] = startMinmax[4] + translation.z;
		minmax[5] = startMinmax[5] + translation.z;
		return minmax;
	}
}
