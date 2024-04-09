package main.java.model.objects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.assimp.AINode;

import glm.mat._4.Mat4;
import main.java.utils.model.ModelUtils;

public class Node {
	
	private String nodeName;
	private Node parent;
	private List<Node> children = new ArrayList<>();
	private List<Mat4> transformations = new ArrayList<>();
	
	private Mat4 transformation;
	
	public Node(AINode aiNode, Node parent) {
		this.nodeName = aiNode.mName().dataString();
		this.parent = parent;
		this.transformation = ModelUtils.assimpMatrixToMat4(aiNode.mTransformation());
	}
	
	public void addChild(Node child) {
		children.add(child);
	}
	
	public List<Node> getChildren() {
		return children;
	}
	
	public void addTransformation(Mat4 transformation) {
		transformations.add(transformation);
	}

	public Node findByName(String nodeName) {
		if(this.nodeName.equals(nodeName)) {
			return this;
		}
		for (Node node : children) {
			return node.findByName(nodeName);
		}
		return null;
	}

	public String getNodeName() {
		return nodeName;
	}
	
	public Mat4 getTransformation() {
		return transformation;
	}

	public Mat4 getParentTransforms(int i) {
		if(parent != null) {
			return new Mat4(getParentTransforms(i)).mul(transformations.get(i));
		}
		return transformations.get(i);
	}
	
}
