package main.java.utils.loaders;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.assimp.Assimp.aiProcess_FixInfacingNormals;
import static org.lwjgl.assimp.Assimp.aiProcess_FlipUVs;
import static org.lwjgl.assimp.Assimp.aiProcess_GenSmoothNormals;
import static org.lwjgl.assimp.Assimp.aiProcess_GenUVCoords;
import static org.lwjgl.assimp.Assimp.aiProcess_JoinIdenticalVertices;
import static org.lwjgl.assimp.Assimp.aiProcess_LimitBoneWeights;
import static org.lwjgl.assimp.Assimp.aiProcess_OptimizeMeshes;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.assimp.AIScene;

import resources.ResourceLoader;

public class AssimpModelLoader {

	private static final Set<Integer> ASSIMP_POST_STATIC = new HashSet<>() {
		private static final long serialVersionUID = 1L;

		{
			add(aiProcess_Triangulate);
			add(aiProcess_GenSmoothNormals);
			add(aiProcess_GenUVCoords);
			add(aiProcess_FlipUVs);

			add(aiProcess_CalcTangentSpace);
			add(aiProcess_JoinIdenticalVertices);

			add(aiProcess_OptimizeMeshes);
			
			add(aiProcess_GenBoundingBoxes);
		}
	};
	
	private static final Set<Integer> ASSIMP_POST_ANIMATED = new HashSet<>() {
		private static final long serialVersionUID = 1L;

		{
			add(aiProcess_GenSmoothNormals);
			add(aiProcess_JoinIdenticalVertices);
			add(aiProcess_Triangulate);
			add(aiProcess_FixInfacingNormals);
			add(aiProcess_LimitBoneWeights);
		}
	};

	public static AIScene loadStaticFromResource(String folder, String fileName) {
		return loadStatic(ResourceLoader.getModelFile(folder, fileName));
	}

	public static AIScene loadStatic(File file) {
		int combinedFlags = ASSIMP_POST_STATIC.stream().reduce(0, (a, b) -> a | b);
		AIScene scene = AssimpWrapper.aiImportFile(file.getAbsolutePath(), combinedFlags);
		return scene;
	}

	public static AIScene loadAnimatedFromResource(String folder, String fileName) {
		return loadStatic(ResourceLoader.getModelFile(folder, fileName));
	}

	public static AIScene loadAnimated(File file) {
		int combinedFlags = ASSIMP_POST_ANIMATED.stream().reduce(0, (a, b) -> a | b);
		AIScene scene = AssimpWrapper.aiImportFile(file.getAbsolutePath(), combinedFlags);
		return scene;
	}
	
}
