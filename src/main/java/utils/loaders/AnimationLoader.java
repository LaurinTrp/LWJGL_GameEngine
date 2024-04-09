package main.java.utils.loaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIQuatKey;
import org.lwjgl.assimp.AIQuaternion;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVectorKey;

import glm.mat._4.Mat4;
import glm.quat.Quat;
import main.java.data.animation.AnimatedFrame;
import main.java.data.animation.Animation;
import main.java.model.objects.AnimMesh;
import main.java.model.objects.Bone;
import main.java.model.objects.Node;

public class AnimationLoader {

	private List<Animation> animations;

	private AIScene scene;

	public AnimationLoader(AIScene scene) {
		this.scene = scene;
	}

	public void loadAnimations(List<Bone> boneList, Node rootNode, Mat4 globalInverseTransformation) {
		animations = new ArrayList<>();

		int numAnimations = scene.mNumAnimations();
		PointerBuffer aiAnimations = scene.mAnimations();
		for (int i = 0; i < numAnimations; i++) {
			AIAnimation aiAnimation = AIAnimation.create(aiAnimations.get());
			int maxFrames = calculateMaxFrames(aiAnimation);

			List<AnimatedFrame> frames = new ArrayList<>();
			Animation animation = new Animation(aiAnimation.mName().dataString(), frames, aiAnimation.mDuration());
			animations.add(animation);

			for (int j = 0; j < maxFrames; j++) {
				Mat4[] boneMatrices = new Mat4[AnimMesh.MAX_BONES];
				Arrays.fill(boneMatrices, new Mat4(1.0f));
				AnimatedFrame animatedFrame = new AnimatedFrame(boneMatrices);
				buildFrameMatrices(aiAnimation, boneList, animatedFrame, j, rootNode, rootNode.getTransformation(),
						globalInverseTransformation);
				frames.add(animatedFrame);
			}
		}
	}

	private int calculateMaxFrames(AIAnimation aiAnimation) {
		int maxFrames = 0;
		int numNodeAnims = aiAnimation.mNumChannels();
		PointerBuffer aiChannels = aiAnimation.mChannels();
		for (int i = 0; i < numNodeAnims; i++) {
			AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get());
			int numFrames = Math.max(Math.max(aiNodeAnim.mNumPositionKeys(), aiNodeAnim.mNumRotationKeys()),
					aiNodeAnim.mNumScalingKeys());
			maxFrames = Math.max(numFrames, maxFrames);
		}

		return maxFrames;
	}

	private static Mat4 buildNodeTransformationMatrix(AINodeAnim aiNodeAnim, int frame) {
        AIVectorKey.Buffer positionKeys = aiNodeAnim.mPositionKeys();
        AIVectorKey.Buffer scalingKeys = aiNodeAnim.mScalingKeys();
        AIQuatKey.Buffer rotationKeys = aiNodeAnim.mRotationKeys();

        AIVectorKey aiVecKey;
        AIVector3D vec;

        Mat4 nodeTransform = new Mat4();
        int numPositions = aiNodeAnim.mNumPositionKeys();
        if (numPositions > 0) {
            aiVecKey = positionKeys.get(Math.min(numPositions - 1, frame));
            vec = aiVecKey.mValue();
            nodeTransform.translate(vec.x(), vec.y(), vec.z());
        }
        int numRotations = aiNodeAnim.mNumRotationKeys();
        if (numRotations > 0) {
            AIQuatKey quatKey = rotationKeys.get(Math.min(numRotations - 1, frame));
            AIQuaternion aiQuat = quatKey.mValue();
            Quat quat = new Quat(aiQuat.x(), aiQuat.y(), aiQuat.z(), aiQuat.w());
            nodeTransform.rotate(quat);
        }
        int numScalingKeys = aiNodeAnim.mNumScalingKeys();
        if (numScalingKeys > 0) {
            aiVecKey = scalingKeys.get(Math.min(numScalingKeys - 1, frame));
            vec = aiVecKey.mValue();
            nodeTransform.scale(vec.x(), vec.y(), vec.z());
        }

        return nodeTransform;
    }

	private static void buildFrameMatrices(AIAnimation aiAnimation, List<Bone> boneList,
			AnimatedFrame animatedFrame, int frame, Node node, Mat4 parentTransformation,
			Mat4 globalInverseTransform) {
		String nodeName = node.getNodeName();
		AINodeAnim aiNodeAnim = findAIAnimNode(aiAnimation, nodeName);
		Mat4 nodeTransform = node.getTransformation();
		if (aiNodeAnim != null) {
			nodeTransform = buildNodeTransformationMatrix(aiNodeAnim, frame);
		}
		Mat4 nodeGlobalTransform = new Mat4(parentTransformation).mul(nodeTransform);

		List<Bone> affectedBones = boneList.stream().filter(b -> b.boneName().equals(nodeName)).toList();
		for (Bone bone : affectedBones) {
			Mat4 boneTransform = new Mat4(globalInverseTransform).mul(nodeGlobalTransform)
					.mul(bone.offsetMatrix());
			animatedFrame.boneMatrices()[bone.boneId()] = new Mat4(boneTransform);
		}

		for (Node childNode : node.getChildren()) {
			buildFrameMatrices(aiAnimation, boneList, animatedFrame, frame, childNode, nodeGlobalTransform,
					globalInverseTransform);
		}
	}
	
	private static AINodeAnim findAIAnimNode(AIAnimation aiAnimation, String nodeName) {
        AINodeAnim result = null;
        int numAnimNodes = aiAnimation.mNumChannels();
        PointerBuffer aiChannels = aiAnimation.mChannels();
        for (int i = 0; i < numAnimNodes; i++) {
            AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(i));
            if (nodeName.equals(aiNodeAnim.mNodeName().dataString())) {
                result = aiNodeAnim;
                break;
            }
        }
        return result;
    }
	
	public List<Animation> getAnimations() {
		return animations;
	}
}
