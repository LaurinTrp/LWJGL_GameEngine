package main.java.utils.loaders;

import static org.lwjgl.assimp.Assimp.aiImportFile;
import static org.lwjgl.assimp.Assimp.aiProcess_FixInfacingNormals;
import static org.lwjgl.assimp.Assimp.aiProcess_JoinIdenticalVertices;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;

import glm.vec._4.Vec4;
import main.java.render.model.Material;
import main.java.render.model.Mesh;

public class StaticMeshesLoader {
	
	
	
//	public static Mesh[] load(String resourcePath, String texturesDir) {
//		return load(resourcePath, texturesDir, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals);
//	}
//	
//	public static Mesh[] load(String resourcePath, String texturesDir, int flags) {
//		AIScene aiScene = aiImportFile(resourcePath, flags);
//		if(aiScene == null) {
//			throw new AssimpLoadException("Could not load model: " + resourcePath);
//		}
//		
//		int numMaterials = aiScene.mNumMaterials();
//		PointerBuffer aiMaterials = aiScene.mMaterials();
//		List<Material> materials = new ArrayList<>();
//		for (int i = 0; i < numMaterials; i++) {
//		    AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
//		    processMaterial(aiMaterial, materials, texturesDir);
//		}
//
//		int numMeshes = aiScene.mNumMeshes();
//		PointerBuffer aiMeshes = aiScene.mMeshes();
//		Mesh[] meshes = new Mesh[numMeshes];
//		for (int i = 0; i < numMeshes; i++) {
//		    AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
//		    Mesh mesh = processMesh(aiMesh, materials);
//		    meshes[i] = mesh;
//		}
//
//		return meshes;
//	}
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
