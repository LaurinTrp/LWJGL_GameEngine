package main.java.utils.loaders;

import static main.java.utils.FileUtils.ioResourceToByteBuffer;
import static org.lwjgl.assimp.Assimp.aiGetErrorString;
import static org.lwjgl.assimp.Assimp.aiOrigin_CUR;
import static org.lwjgl.assimp.Assimp.aiOrigin_END;
import static org.lwjgl.assimp.Assimp.aiOrigin_SET;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memCopy;
import static org.lwjgl.system.MemoryUtil.memUTF8;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.assimp.AIFile;
import org.lwjgl.assimp.AIFileIO;
import org.lwjgl.assimp.AIScene;

import jassimp.AiPostProcessSteps;
import jassimp.AiScene;
import jassimp.IHMCJassimp;
import main.java.render.model.assimp.AssimpModel;

public class StaticMeshesLoader {
		
	public static final Set<AiPostProcessSteps> ASSIMP_POST = new HashSet<AiPostProcessSteps>() {
	      {
	            add(AiPostProcessSteps.Triangulate);
	            add(AiPostProcessSteps.GenSmoothNormals);
	            add(AiPostProcessSteps.GenUVCoords);
	            add(AiPostProcessSteps.FlipUVs);

	            add(AiPostProcessSteps.CalcTangentSpace);
	            add(AiPostProcessSteps.JoinIdenticalVertices);

	            add(AiPostProcessSteps.OptimizeMeshes);
	      }
	  };
	
	
//	public static Mesh[] load(String resourcePath, String texturesDir) {
//		return load(resourcePath, texturesDir, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals);
//	}
//	
	public static AssimpModel load(String filePath) throws IOException { 
		AIFileIO fileIo = AIFileIO.create()
            .OpenProc((pFileIO, fileName, openMode) -> {
                ByteBuffer data;
                String fileNameUtf8 = memUTF8(fileName);
                try {
                    data = ioResourceToByteBuffer(fileNameUtf8, 8192);
                } catch (IOException e) {
                    throw new RuntimeException("Could not open file: " + fileNameUtf8);
                }

                return AIFile.create()
                    .ReadProc((pFile, pBuffer, size, count) -> {
                        long max = Math.min(data.remaining() / size, count);
                        memCopy(memAddress(data), pBuffer, max * size);
                        data.position(data.position() + (int) (max * size));
                        return max;
                    })
                    .SeekProc((pFile, offset, origin) -> {
                        if (origin == aiOrigin_CUR) {
                            data.position(data.position() + (int) offset);
                        } else if (origin == aiOrigin_SET) {
                            data.position((int) offset);
                        } else if (origin == aiOrigin_END) {
                            data.position(data.limit() + (int) offset);
                        }
                        return 0;
                    })
                    .FileSizeProc(pFile -> data.limit())
                    .address();
            })
            .CloseProc((pFileIO, pFile) -> {
                AIFile aiFile = AIFile.create(pFile);

                aiFile.ReadProc().free();
                aiFile.SeekProc().free();
                aiFile.FileSizeProc().free();
            });
		
//		aiImportF
		
//        AIScene scene = aiImportFile(filePath,
//                aiProcess_JoinIdenticalVertices | aiProcess_Triangulate);
		
		AiScene scene = IHMCJassimp.importFile(filePath, ASSIMP_POST);
		
        fileIo.OpenProc().free();
        fileIo.CloseProc().free();
        if (scene == null) {
            throw new IllegalStateException(aiGetErrorString());
        }
        return new AssimpModel(scene);	
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
