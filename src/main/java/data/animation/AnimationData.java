package main.java.data.animation;

import java.util.Arrays;

import glm.mat._4.Mat4;
import main.java.model.objects.AnimMesh;

public class AnimationData {
	
	public static final Mat4[] DEFAULT_BONE_MATRICES = new Mat4[AnimMesh.MAX_BONES];
	
	static {
		Mat4 emptyMat = new Mat4();
		Arrays.fill(DEFAULT_BONE_MATRICES, emptyMat);
	}
	
	private Animation currentAnimation;
	private int currentFrameIdx;
	
	public AnimationData(Animation currentAnimation) {
		currentFrameIdx = 0;
		this.currentAnimation = currentAnimation;
	}
	
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}
	
	public AnimatedFrame getCurrentFrame() {
		return currentAnimation.getFrames().get(currentFrameIdx);
	}
	
	public int getCurrentFrameIdx() {
		return currentFrameIdx;
	}
	
	public void nextFrame() {
		currentFrameIdx = (currentFrameIdx + 1) % currentAnimation.getFrames().size();
	}
	
	public void setCurrentAnimation(Animation currentAnimation) {
		this.currentAnimation = currentAnimation;
	}
	
}
