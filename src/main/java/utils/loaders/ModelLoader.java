package main.java.utils.loaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import main.java.gui.Engine_Main;
import main.java.render.model.Material;
import main.java.render.model.Model;
import main.java.utils.FileUtils;

public class ModelLoader {

	private static ArrayList<Double[]> vertices = new ArrayList<>();
	private static ArrayList<Double[]> textures = new ArrayList<>();
	private static ArrayList<Double[]> normals = new ArrayList<>();

	private static ArrayList<Float> verticesFinal = new ArrayList<Float>();
	private static ArrayList<Float> texturesFinal = new ArrayList<Float>();
	private static ArrayList<Float> normalsFinal = new ArrayList<Float>();
	
	private static String material = "";

	private static int triangleCount = 0;
	
	private static File file;
	
	private static ArrayList<Model> models = new ArrayList<Model>();
	private static HashMap<String, Integer> materials = new HashMap<String, Integer>();
	
	public float[][] loadObjFromRescource(String fileName){
		fileName = Engine_Main.PATHS.MODEL_PATH + fileName;
		return loadObj(fileName);
	}
	
	private Model processContent(String[] lines) {
		triangleCount = 0;
		int modelMaterial = 0;
		for (String line : lines) {
			String[] splitted = line.split(" ");
			switch (splitted[0]) {
			case "v": 
				Double[] verticesVec = new Double[] {
					Double.parseDouble(splitted[1]), 
					Double.parseDouble(splitted[2]),
					Double.parseDouble(splitted[3]), 
					1.0 
				};
				vertices.add(verticesVec);
				break;
			case "vt":
				Double[] texVec = new Double[] {
					Double.parseDouble(splitted[1]), 
					1 - Double.parseDouble(splitted[2]),
					0.0, 
					1.0 
				};
				textures.add(texVec);
				break;
			case "vn":
				Double[] normalVec = new Double[] {
					Double.parseDouble(splitted[1]), 
					Double.parseDouble(splitted[2]),
					Double.parseDouble(splitted[3]), 
					1.0 
				};
				normals.add(normalVec);
				 break;
			case "f":
				processFace(splitted[1]);
				processFace(splitted[2]);
				processFace(splitted[3]);
				break;
			case "usemtl":
				modelMaterial = materials.get(splitted[1]).intValue();
			default:
				break;
			}
		}
		Model model = new Model(getVertices(), getNormals(), getNormals(), triangleCount, new Material());
		if(modelMaterial != 0) {
			model.getMaterial().setTexture(modelMaterial);
		}
		return model;
	}
	
	
	public static float[][] loadObj(String path) {
		clear();
		file = new File(path);
		try {
			ArrayList<String> lines = FileUtils.getFileContent(file);
			lines.parallelStream().filter(s -> s.startsWith("#"));
			
			for (String line : lines) {
				String[] splitted = line.split(" ");
				switch (splitted[0]) {
				case "v": 
					Double[] verticesVec = new Double[] {
						Double.parseDouble(splitted[1]), 
						Double.parseDouble(splitted[2]),
						Double.parseDouble(splitted[3]), 
						1.0 
					};
					vertices.add(verticesVec);
					break;
				case "vt":
					Double[] texVec = new Double[] {
						Double.parseDouble(splitted[1]), 
						1 - Double.parseDouble(splitted[2]),
						0.0, 
						1.0 
					};
					textures.add(texVec);
					break;
				case "vn":
					Double[] normalVec = new Double[] {
						Double.parseDouble(splitted[1]), 
						Double.parseDouble(splitted[2]),
						Double.parseDouble(splitted[3]),
						1.0 
					};
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
			
			return new float[][] {
				getVertices(),
				getTextures(),
				getNormals(),
			};
			
		}
		
		catch(IOException e){
			e.printStackTrace();
		}return null;
	}
	
	
	public static Model loadModel(String path) {
		
		float[][] data = loadObj(path);
		Model model = new Model(data[0], data[1], data[2], triangleCount, new Material());
		
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
	
	public ArrayList<Model> getModels() {
		return models;
	}
	
	private static void processFace(String triple) {
		String[] token = triple.split("/");
		int vector = Integer.parseInt(token[0])-1;
		int texture = Integer.parseInt(token[1])-1;
		int normal = Integer.parseInt(token[2])-1;
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
			if(subFile.getName().equals(material)) {
				materialFile = subFile;
				break;
			}
		}
		try {
			String content = FileUtils.getFileContentAsString(materialFile);
			String[] splitted = content.split("\n\n");
			String name = "";
			int textureID = 0;
			for (int i = 1; i < splitted.length; i++) {
				String[] lines = splitted[i].split("\n");
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	private static String processMaterialFile(String[] splitted) {
		String file = "";
		if(splitted.length > 2) {
			for (int i = 1; i < splitted.length; i++) {
				file += splitted[i] + " ";
			}
		}else {
			file = splitted[1];
		}
		File f = new File(file.trim());
		if(f.exists()) {
			return f.getAbsolutePath();
		}
		file = ModelLoader.file.getParent() + "/" + file;
		f = new File(file);
		if(f.exists()) {
			return f.getAbsolutePath();
		}
		return "";
	}

	public static float[] getVertices() {
		float[] vertices = new float[verticesFinal.size()];
		for (int i = 0; i < verticesFinal.size(); i++) {
			vertices[i] = verticesFinal.get(i);
		}
		return vertices;
	}

	public static float[] getTextures() {
		float[] textures = new float[texturesFinal.size()];
		for (int i = 0; i < texturesFinal.size(); i++) {
			textures[i] = texturesFinal.get(i);
		}
		return textures;
	}

	public static float[] getNormals() {
		float[] normals = new float[normalsFinal.size()];
		for (int i = 0; i < normalsFinal.size(); i++) {
			normals[i] = normalsFinal.get(i);
		}
		return normals;
	}
	
	
	
}
