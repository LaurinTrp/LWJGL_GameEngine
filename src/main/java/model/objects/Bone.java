package main.java.model.objects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIVertexWeight;

import glm.mat._4.Mat4;
import main.java.utils.model.ModelUtils;

public class Bone {
	private String name;
	private Mat4 offsetMatrix;
	private List<VertexWeight> weights;
	
	private int id;

	public Bone(AIBone bone, int boneId) {
		this.name = bone.mName().dataString();
		this.id = boneId;
		AIMatrix4x4 boneMatrix = bone.mOffsetMatrix();

		this.offsetMatrix = ModelUtils.assimpMatrixToMat4(boneMatrix);

		this.weights = new ArrayList<>();
		for (int i = 0; i < bone.mNumWeights(); i++) {
			AIVertexWeight aiWeight = bone.mWeights().get(i);
			VertexWeight vertexWeight = new VertexWeight(boneId, aiWeight.mVertexId(), aiWeight.mWeight());
			weights.add(vertexWeight);
		}
		System.out.println(weights);
	}
	
	public Mat4 offsetMatrix() {
		return offsetMatrix;
	}
	
	public String boneName() {
		return name;
	}
	
	public int boneId() {
		return id;
	}
	
	public List<VertexWeight> getWeights() {
		return weights;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + name + "\n");
		sb.append("ID: " + id + "\n");
		sb.append("Matrix: " + offsetMatrix.toString() + "\n");
		sb.append(weights.toString());
		return sb.toString();
	}
}
