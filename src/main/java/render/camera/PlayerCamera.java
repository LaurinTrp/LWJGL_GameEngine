package main.java.render.camera;

import java.util.List;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import jassimp.AiBuiltInWrapperProvider;
import jassimp.AiCamera;
import jassimp.AiNode;
import jassimp.AiScene;
import main.java.render.entities.Player;
import main.java.utils.loaders.WrapperProvider;

public class PlayerCamera extends Camera {

	private final AiScene scene;
	
	public PlayerCamera(Player player, AiScene scene) {
		super(player);
		cameraMode = CameraMode.POV_CAMERA;
		
		this.scene = scene;
		
		loadCameras();
	}
	
	
	private void loadCameras() {
		
		WrapperProvider wrapper = new WrapperProvider();
		
		AiNode root = scene.getSceneRoot(new AiBuiltInWrapperProvider());
		
		List<AiCamera> cameras = scene.getCameras();
		for (AiCamera aiCamera : cameras) {
			AiNode parent = getCameraNode(root, aiCamera);
			Mat4 matrix = new Mat4(parent.getTransform(wrapper));
			
			initialCameraPosition = new Vec3(matrix.m30, matrix.m31, matrix.m32);
			cameraPosition = new Vec3(matrix.m30, matrix.m31, matrix.m32);
			
			cameraFront = aiCamera.getLookAt(wrapper);
			
			cameraUp = aiCamera.getUp(wrapper);
		}
	}
	
	private AiNode getCameraNode(AiNode node, AiCamera camera) {
		if(node.getName().strip().startsWith(camera.getName().strip())) {
			return node;
		} else {
			for (AiNode child : node.getChildren()) {
				return getCameraNode(child, camera);
			}
		}
		return null;
	}
	
	public void scale(float scale) {
		initialCameraPosition = initialCameraPosition.mul(scale);
	}
	
}
