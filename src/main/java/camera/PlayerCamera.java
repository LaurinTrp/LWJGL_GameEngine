package main.java.camera;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AICamera;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

import glm.vec._3.Vec3;
import main.java.render.assimpModels.Player;

public class PlayerCamera extends Camera {

	private final AIScene scene;
	
	public PlayerCamera(Player player, AIScene scene) {
		super(player);
		cameraMode = CameraMode.POV_CAMERA;
		
		this.scene = scene;
		
		loadCameras();
	}
	
	
	private void loadCameras() {
		
		AINode root = scene.mRootNode();
		
		PointerBuffer buffer = scene.mCameras();
		
		while(buffer.hasRemaining()) {
			AICamera aiCamera = AICamera.create(buffer.get());
			AINode parent = getCameraNode(root, aiCamera);

			AIMatrix4x4 parentTransformation = parent.mTransformation();
			
			initialCameraPosition = new Vec3(parentTransformation.a4(), parentTransformation.b4(), parentTransformation.c4());
			cameraPosition = new Vec3(initialCameraPosition);
			
			AIVector3D aiCameraUp = aiCamera.mUp();
			AIVector3D aiCameraFront = aiCamera.mLookAt();
			
			cameraUp = new Vec3(aiCameraUp.x(), aiCameraUp.y(), aiCameraUp.z());
			cameraFront = new Vec3(aiCameraFront.x(), aiCameraFront.y(), aiCameraFront.z());
		}
	}
	
	private AINode getCameraNode(AINode node, AICamera camera) {
		if(node.mName().dataString().strip().startsWith(camera.mName().dataString().strip())) {
			return node;
		} else {
			PointerBuffer children = node.mChildren();
			while(children.hasRemaining()) {
				return getCameraNode(AINode.create(children.get()), camera);
			}
		}
		return null;
	}
	
	public void scale(float scale) {
		initialCameraPosition = initialCameraPosition.mul(scale);
	}
	
}
