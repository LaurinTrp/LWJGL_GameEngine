package main.java.utils.loaders;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import main.java.render.IRenderObject;
import main.java.render.model.Material;
import main.java.render.model.MultiModel;
import main.java.render.model.SingleModel;
import main.java.utils.FileUtils;
import resources.ResourceLoader;

public class ModelLoader {

	private static ArrayList<Double[]> vertices = new ArrayList<>();
	private static ArrayList<Double[]> textures = new ArrayList<>();
	private static ArrayList<Double[]> normals = new ArrayList<>();

	private static ArrayList<Float> verticesFinal = new ArrayList<>();
	private static ArrayList<Float> texturesFinal = new ArrayList<>();
	private static ArrayList<Float> normalsFinal = new ArrayList<>();

	private static String material = "";

	private static int triangleCount = 0;

	private static File file;


	private static ArrayList<IRenderObject> models = new ArrayList<>();
	private static HashMap<String, Integer> materials = new HashMap<>();

	public ModelLoader() {
	}

	public static ArrayList<IRenderObject> loadMultipleModelsFromObj(String path) {
		ArrayList<IRenderObject> models = new ArrayList<>();
		ArrayList<Float[][]> objects = loadMultipleFromObj(path);
		for (Float[][] floats : objects) {
			IRenderObject model = new SingleModel(floats[0], floats[1], floats[2], floats[4][0].intValue(), new Material(), floats[3]);
			models.add(model);
		}
		return models;
	}

	public static ArrayList<Float[][]> loadMultipleFromObj(String path) {
		clear();
		file = new File(path);
		try {
			String lines = FileUtils.getFileContentAsString(file);
			ArrayList<Float[][]> output = new ArrayList<>();
			// Regex to match a line that indicates the start of an object
			String[] content = lines.split("[o] .*");
			content = Arrays.copyOfRange(content, 1, content.length);
			Arrays.stream(content).filter(l -> l.isBlank());
			for (String string : content) {
				clearFinalLists();
				triangleCount = 0;
				ArrayList<String> object = new ArrayList<>(Arrays.asList(string.split("\n")));
				Float[][] data = loadObj(object, path);
				Float[][] newData = new Float[][] {
					data[0], data[1], data[2], data[3], new Float[] {(float) triangleCount}
				};
				output.add(newData);
			}

			return output;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Float[][] loadObj(String path) {
		clear();
		file = new File(path);
		try {
			ArrayList<String> lines = FileUtils.getFileContent(file);
			return loadObj(lines, path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Float[][] loadObj(ArrayList<String> lines, String path) {
		Float[] minmax = new Float[6];
		minmax[0] = Float.MAX_VALUE;
		minmax[1] = -Float.MAX_VALUE;
		minmax[2] = Float.MAX_VALUE;
		minmax[3] = -Float.MAX_VALUE;
		minmax[4] = Float.MAX_VALUE;
		minmax[5] = -Float.MAX_VALUE;
		file = new File(path);
		lines.parallelStream().filter(s -> s.startsWith("#")).filter(s -> s.isBlank());

		for (String line : lines) {
			String[] splitted = line.split(" ");
			switch (splitted[0]) {
			case "v":
				Double[] verticesVec = new Double[] { Double.parseDouble(splitted[1]), Double.parseDouble(splitted[2]),
						Double.parseDouble(splitted[3]), 1.0 };
				vertices.add(verticesVec);
				minmax[0] = (float)Math.min(minmax[0], verticesVec[0]);
				minmax[1] = (float)Math.max(minmax[1], verticesVec[0]);
				minmax[2] = (float)Math.min(minmax[2], verticesVec[1]);
				minmax[3] = (float)Math.max(minmax[3], verticesVec[1]);
				minmax[4] = (float)Math.min(minmax[4], verticesVec[2]);
				minmax[5] = (float)Math.max(minmax[5], verticesVec[2]);
				break;
			case "vt":
				Double[] texVec = new Double[] { Double.parseDouble(splitted[1]), 1 - Double.parseDouble(splitted[2]),
						0.0, 1.0 };
				textures.add(texVec);
				break;
			case "vn":
				Double[] normalVec = new Double[] { Double.parseDouble(splitted[1]), Double.parseDouble(splitted[2]),
						Double.parseDouble(splitted[3]), 1.0 };
				normals.add(normalVec);
				break;
			case "f":
				processFace(splitted[1]);
				processFace(splitted[2]);
				processFace(splitted[3]);
				break;
			case "mtllib":
				material = splitted[1];
				break;
			default:
				break;
			}
		}

		return new Float[][] { getVertices(), getTextures(), getNormals(), minmax};
	}

	public static IRenderObject loadModel(String path) {
		Float[][] data = loadObj(path);
		IRenderObject model = new SingleModel(data[0], data[1], data[2], triangleCount, new Material(), data[3]);
		return model;
	}

	public static IRenderObject loadModelFromResource(String parent, String file) {
		clear();
		Float[][] data = loadObj(ResourceLoader.loadObjFile(parent, file), parent + File.separator + file);
		IRenderObject model = new SingleModel(data[0], data[1], data[2], triangleCount, new Material(), data[3]);

		return model;
	}

	public static IRenderObject loadMultiModelFromResource(String parent, String file) {
		clear();
		Float[][] data = loadObj(ResourceLoader.loadObjFile(parent, file), parent + File.separator + file);
		IRenderObject model = new MultiModel(data[0], data[1], data[2], triangleCount, new Material(), data[3], null);

		return model;
	}

	private static void clear() {
		vertices.clear();
		normals.clear();
		textures.clear();
		verticesFinal.clear();
		normalsFinal.clear();
		texturesFinal.clear();
	}
	private static void clearFinalLists() {
		verticesFinal.clear();
		texturesFinal.clear();
		normalsFinal.clear();
	}

	public ArrayList<IRenderObject> getModels() {
		return models;
	}

	private static void processFace(String triple) {
		String[] token = triple.split("/");
		int vector = Integer.parseInt(token[0]) - 1;
		int texture = Integer.parseInt(token[1]) - 1;
		int normal = Integer.parseInt(token[2]) - 1;
		for (int i = 0; i < 4; i++) {
			triangleCount++;
			verticesFinal.add(vertices.get(vector)[i].floatValue());
			texturesFinal.add(textures.get(texture)[i].floatValue());
			normalsFinal.add(normals.get(normal)[i].floatValue());
		}
	}

	public int getTriangleCount() {
		return triangleCount;
	}

	public static int loadMaterial(String parentFile) {
		File folder = new File(parentFile).getParentFile();
		File materialFile = null;
		for (File subFile : folder.listFiles()) {
			if (subFile.getName().equals(material)) {
				materialFile = subFile;
				break;
			}
		}
		try {
			String content = FileUtils.getFileContentAsString(materialFile);
			ArrayList<String> lines = new ArrayList<>();
			for (String string : content.split("\n")) {
				lines.add(string);
			}
			return loadMaterial(lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int loadMaterialFileFromResource(String parentFile, String fileName) {
		return loadMaterial(ResourceLoader.loadMaterialFile(parentFile, fileName));
	}

	public static int loadMaterial(ArrayList<String> content) {
		String name = "";
		int textureID = 0;
		try {
			for (int i = 1; i < content.size(); i++) {
				String[] lines = content.get(i).split("\n");
				for (String string : lines) {
					String[] splittedLine = string.split(" ");
					switch (splittedLine[0]) {
					case "newmtl":
						name = splittedLine[1];
						break;
					case "map_Kd":
						String file = processMaterialFile(splittedLine);
						textureID = ImageLoader.loadTextureFromMemory(processMaterialFile(splittedLine));
						break;
					default:
						break;
					}
				}
			}
			return textureID;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private static String processMaterialFile(String[] splitted) {
		String file = "";
		if (splitted.length > 2) {
			for (int i = 1; i < splitted.length; i++) {
				file += splitted[i] + " ";
			}
		} else {
			file = splitted[1];
		}
		File f = new File(file.trim());
		if (f.exists()) {
			return f.getAbsolutePath();
		}
		file = ModelLoader.file.getParent() + "/" + file;
		f = new File(file);
		if (f.exists()) {
			return f.getAbsolutePath();
		}
		return "";
	}

	public static Float[] getVertices() {
		Float[] vertices = new Float[verticesFinal.size()];
		for (int i = 0; i < verticesFinal.size(); i++) {
			vertices[i] = verticesFinal.get(i);
		}
		return vertices;
	}

	public static Float[] getTextures() {
		Float[] textures = new Float[texturesFinal.size()];
		for (int i = 0; i < texturesFinal.size(); i++) {
			textures[i] = texturesFinal.get(i);
		}
		return textures;
	}

	public static Float[] getNormals() {
		Float[] normals = new Float[normalsFinal.size()];
		for (int i = 0; i < normalsFinal.size(); i++) {
			normals[i] = normalsFinal.get(i);
		}
		return normals;
	}

}