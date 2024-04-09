package main.java.model.objects;

import java.util.ArrayList;
import java.util.List;

import glm.mat._4.Mat4;

public class Joint {
	
	public final int index;
	public final String name;
	public final List<Joint> children = new ArrayList<>();
	
	private Mat4 animatedTransform = new Mat4();
	
	private final Mat4 localBindTransform;
	private Mat4 inverseBindTransform = new Mat4();
	
	public Joint(int index, String name, Mat4 localBindTransform) {
		this.index = index;
		this.name = name;
		this.localBindTransform = localBindTransform;
	}
	
	public void addChild(Joint child) {
		this.children.add(child);
	}
	
	public Mat4 getAnimatedTransform() {
		return animatedTransform;
	}
	
	public void setAnimationTransform(Mat4 animatedTransform) {
		this.animatedTransform = animatedTransform;
	}
	
	public Mat4 getInverseBindTransform() {
		return inverseBindTransform;
	}
	
	protected void calcInverseBindTransform(Mat4 parentBindTransform) {
		Mat4 bindTransform = parentBindTransform.mul(localBindTransform);
		inverseBindTransform = bindTransform.inverse();
		for (Joint child : children) {
			child.calcInverseBindTransform(bindTransform);
		}
	}
	
}
