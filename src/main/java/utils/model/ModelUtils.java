package main.java.utils.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.lwjgl.assimp.AIMatrix4x4;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.data.Material;
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

	public static float[] createNormals(float[] vertices, float[] normals) {
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

	public static void createUniform(ShaderProgram program, Map<String, Integer> uniforms, String name) {
		if (program == null) {
			return;
		}
		int id = program.getUniformLocation(name);
		uniforms.put(name, id);
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

	public static float[] calculateMinmax(float[] startMinmax, Vec4 translation) {
		float[] minmax = new float[6];
		minmax[0] = startMinmax[0] + translation.x;
		minmax[1] = startMinmax[1] + translation.x;
		minmax[2] = startMinmax[2] + translation.y;
		minmax[3] = startMinmax[3] + translation.y;
		minmax[4] = startMinmax[4] + translation.z;
		minmax[5] = startMinmax[5] + translation.z;
		return minmax;
	}

	public static Vec3 getObjectIdAsColor(int id) {
		int red = (id >> 16) & 0xFF;
		int green = (id >> 8) & 0xFF;
		int blue = (id >> 0) & 0xFF;
		
		return new Vec3(red, green, blue);
	}
	
	public static Mat4 assimpMatrixToMat4(AIMatrix4x4 aiMatrix) {
		return new Mat4(
				aiMatrix.a1(), aiMatrix.a2(), aiMatrix.a3(), aiMatrix.a4(), 
				aiMatrix.b1(), aiMatrix.b2(), aiMatrix.b3(), aiMatrix.b4(), 
				aiMatrix.c1(), aiMatrix.c2(), aiMatrix.c3(), aiMatrix.c4(), 
				aiMatrix.d1(), aiMatrix.d2(), aiMatrix.d3(), aiMatrix.d4()
		);
	}
}
