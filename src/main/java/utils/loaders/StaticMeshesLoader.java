package main.java.utils.loaders;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.assimp.AIImporterDesc;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

import resources.ResourceLoader;

import static org.lwjgl.assimp.Assimp.*;

public class StaticMeshesLoader {

	public static final Set<Integer> ASSIMP_POST = new HashSet<>() {
		{
			add(aiProcess_Triangulate);
			add(aiProcess_GenSmoothNormals);
			add(aiProcess_GenUVCoords);
			add(aiProcess_FlipUVs);

			add(aiProcess_CalcTangentSpace);
			add(aiProcess_JoinIdenticalVertices);

			add(aiProcess_OptimizeMeshes);
		}
	};

	public static AIScene loadFromResource(String folder, String fileName) {
		return load(ResourceLoader.getModelFile(folder, fileName));
	}

	public static AIScene load(File file) {
		int combinedFlags = ASSIMP_POST.stream().reduce(0, (a, b) -> a | b);
		AIScene scene = AssimpWrapper.aiImportFile(file.getAbsolutePath(), combinedFlags);
		return scene;
	}
//	
//	private static void processMaterial(AIMaterial aiMaterial, List<Material> materials, String texturesDir) throws Exception {
//	    AIColor4D colour = AIColor4D.create();
//
//	    AIString path = AIString.calloc();
//	    aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
//	    String textPath = path.dataString();
//	    Texture texture = null;
//	    if (textPath != null && textPath.length() > 0) {
//	        TextureCache textCache = TextureCache.getInstance();
//	        texture = textCache.getTexture(texturesDir + "/" + textPath);
//	    }
//
//	    Vec4 ambient = Material.DEFAULT_COLOUR;
//	    int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, colour);
//	    if (result == 0) {
//	        ambient = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
//	    }
//
//	    Vec4 diffuse = Material.DEFAULT_COLOUR;
//	    result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, colour);
//	    if (result == 0) {
//	        diffuse = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
//	    }
//
//	    Vec4 specular = Material.DEFAULT_COLOUR;
//	    result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, colour);
//	    if (result == 0) {
//	        specular = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
//	    }
//
//	    Material material = new Material(ambient, diffuse, specular, 1.0f);
//	    material.setTexture(texture);
//	    materials.add(material);
//	}
//	private static Mesh processMesh(AIMesh aiMesh, List<Material> materials) {
//	    List<Float> vertices = new ArrayList<>();
//	    List<Float> textures = new ArrayList<>();
//	    List<Float> normals = new ArrayList<>();
//	    List<Integer> indices = new ArrayList();
//
//	    processVertices(aiMesh, vertices);
//	    processNormals(aiMesh, normals);
//	    processTextCoords(aiMesh, textures);
//	    processIndices(aiMesh, indices);
//
//	    Mesh mesh = new Mesh(Utils.listToArray(vertices),
//	        Utils.listToArray(textures),
//	        Utils.listToArray(normals),
//	        Utils.listIntToArray(indices)
//	    );
//	    Material material;
//	    int materialIdx = aiMesh.mMaterialIndex();
//	    if (materialIdx >= 0 && materialIdx < materials.size()) {
//	        material = materials.get(materialIdx);
//	    } else {
//	        material = new Material();
//	    }
//	    mesh.setMaterial(material);
//
//	    return mesh;
//	}
//	private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
//	    AIVector3D.Buffer aiVertices = aiMesh.mVertices();
//	    while (aiVertices.remaining() > 0) {
//	        AIVector3D aiVertex = aiVertices.get();
//	        vertices.add(aiVertex.x());
//	        vertices.add(aiVertex.y());
//	        vertices.add(aiVertex.z());
//	    }
//	}
}
