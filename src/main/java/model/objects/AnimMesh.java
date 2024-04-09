package main.java.model.objects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIMesh;

public class AnimMesh extends Mesh {

	public static final int MAX_BONES = 150;
	public static final int MAX_WEIGHTS = 4;
	
	private List<Bone> boneList;
	
	private int[] boneIndices;
	private float[] weights;
	
	private int vboBoneIndices;
	private int vboWeights;
	
	public AnimMesh(AIMesh mesh) {
		super(mesh);
	}
	
	@Override
	protected void loadData() {
		super.loadData();
		loadBones();
	}
	
	@Override
	protected void bindData() {
		super.bindData();

		glBindVertexArray(vao);

		int[] vbos = new int[2];
		glGenBuffers(vbos);
		vboWeights = vbos[0];
		vboBoneIndices = vbos[1];
		
	    glBindBuffer(GL_ARRAY_BUFFER, vboWeights);
	    glBufferData(GL_ARRAY_BUFFER, weights, GL_STATIC_DRAW);
	    glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 4, GL_FLOAT, false, 4 * 4, 0);

	    glBindBuffer(GL_ARRAY_BUFFER, vboBoneIndices);
	    glBufferData(GL_ARRAY_BUFFER, boneIndices, GL_STATIC_DRAW);
	    glEnableVertexAttribArray(4);
		glVertexAttribPointer(4, 4, GL_FLOAT, false, 4 * 4, 0);

		glBindVertexArray(0);
	}
	
	private void loadBones() {
		boneList = new ArrayList<>();
		
		// Get all the bones and weights
		PointerBuffer aiBones = mesh.mBones();
		for (int i = 0; i < mesh.mNumBones(); i++) {
			AIBone aiBone = AIBone.create(aiBones.get(i));
			Bone bone = new Bone(aiBone, boneList.size());
			boneList.add(bone);
		}
		
		// Filter the weights so that every vertex has a max of MAX_WEIGHTS weights
		Map<Integer, List<VertexWeight>> reducedWeights = new HashMap<>();
		for (Bone bone : boneList) {
			for (VertexWeight vertexWeight : bone.getWeights()) {
				List<VertexWeight> vertexWeights = reducedWeights.get(vertexWeight.vertexID());
				if(vertexWeights == null) {
					vertexWeights = new ArrayList<>();
					reducedWeights.put(vertexWeight.vertexID(), vertexWeights);
				}
				if(vertexWeights.size() < MAX_WEIGHTS) {
					vertexWeights.add(vertexWeight);
				}
			}
		}
		
		List<Float> weightsList = new ArrayList<>();
		List<Integer> boneIds = new ArrayList<>();
		
		// Fill the empty spaces for the weights
		for (int i = 0; i < mesh.mNumVertices(); i++) {
			List<VertexWeight> vertexWeights = reducedWeights.get(i);
			int size = vertexWeights != null ? vertexWeights.size() : 0;
			for (int j = 0; j < MAX_WEIGHTS; j++) {
				if(j < size) {
					VertexWeight vw = vertexWeights.get(j);
					weightsList.add(vw.weight());
					boneIds.add(vw.boneID());
				}else {
					weightsList.add(0f);
					boneIds.add(0);
				}
			}
		}
		
		// Collect the bones and weights in arrays
		boneIndices = boneIds.stream().mapToInt(Integer::intValue).toArray();
		weights = new float[weightsList.size()];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = weightsList.get(i);
		}
		System.out.println(Arrays.toString(weights));
	}
	
	public List<Bone> getBoneList() {
		return boneList;
	}
}
