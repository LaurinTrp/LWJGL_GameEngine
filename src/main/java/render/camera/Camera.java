package main.java.render.camera;

import glm.Glm;
import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.entities.Player;
import main.java.render.model.MultiTextureTerrain;
import main.java.render.renderobject.IRenderObject;

public class Camera {

	private Player player;

	private Vec3 cameraPosition = new Vec3(0.0f, 0.0f, 0.0f);
	private Vec3 cameraFront = new Vec3(0.0f, 0.0f, -1.0f);
	private Vec3 cameraUp = new Vec3(0.0f, 1.0f, 0.0f);
//	private Vec3 cameraRight = new Vec3(1.0f, 0.0f, 0.0f);

	private float angleHorizontal = 0.0f;
	private float angleVertical = 0.0f;

	private Mat4 view = new Mat4();
	private Mat4 projectionMatrix = new Mat4();

	private final float cameraSpeed = 0.5f;

	private float distanceFromPlayer = 10f;

	private Vec3 focusPoint;

	public CameraMode cameraMode = CameraMode.PLAYER_CAMERA;

	private final float NEAR_CLIPPING_PLANE = 0.1f, FAR_CLIPPING_PLANE = 100f;

	public Camera() {
		projectionMatrix = Glm.perspective_(45.0f, (float) Engine_Main.windowWidth / (float) Engine_Main.windowHeight,
				NEAR_CLIPPING_PLANE, FAR_CLIPPING_PLANE);
		focusPoint = new Vec3();
	}

	public Camera(Player player) {
		this();
		this.player = player;
	}

	private void rotation() {

		switch (cameraMode) {
		case PLAYER_CAMERA: {
			angleHorizontal = player.getRotationAngle() - (float) Math.toRadians(180);

			angleVertical += Engine_Main.mouseHandler.getYoffset();

			if (angleVertical <= -89f) {
				angleVertical = -89f;
			}
			if (angleVertical >= 89f) {
				angleVertical = 89f;
			}

			calculateCameraPos(angleVertical, angleHorizontal);
			break;
		}
		case POV_CAMERA: {
			angleVertical += Engine_Main.mouseHandler.getYoffset();

			if (angleVertical <= -89f) {
				angleVertical = -89f;
			}
			if (angleVertical >= 89f) {
				angleVertical = 89f;
			}
			
			
			cameraFront = new Vec3(player.getPlayerFront());
			cameraFront.y = (float) Math.sin(Math.toRadians(angleVertical));

			cameraPosition = new Vec3(player.getPosition());

			focusPoint = new Vec3(cameraPosition).add(cameraFront);
			break;
		}
		default:
			System.err.println("CameraMode not supported");
			break;
		}

	}

	private void calculateCameraPos(float angleVertical, float angleHorizontal) {
		float verticalDistance = (float) (distanceFromPlayer * Math.sin(Math.toRadians(angleVertical * cameraSpeed)));
		float horizontalDistance = (float) (distanceFromPlayer * Math.cos(Math.toRadians(angleVertical * cameraSpeed)));

		float offsetX = (float) (horizontalDistance * Math.sin(angleHorizontal));
		float offsetZ = (float) (horizontalDistance * Math.cos(angleHorizontal));

		cameraPosition.x = focusPoint.x - offsetX;
		cameraPosition.y = focusPoint.y + verticalDistance;
		cameraPosition.z = focusPoint.z - offsetZ;

//		if (((Player) player).getCurrentTerrain() != null) {
//			IRenderObject currTerrain = ((Player) player).getCurrentTerrain();
//			if (((MultiTextureTerrain) currTerrain).isOnTerrain(new Vec2(cameraPosition.x, cameraPosition.z))) {
//				float terrainHeight = ((MultiTextureTerrain) currTerrain)
//						.heightAtPosition(new Vec2(cameraPosition.x, cameraPosition.z)
//								.min(((MultiTextureTerrain) currTerrain).getGlobalPosition()));
//				if (cameraPosition.y < terrainHeight + 0.2f) {
//					cameraPosition.y = terrainHeight + 0.2f;
//				}
//			}
//		}
	}

	public void moveCamera() {

		distanceFromPlayer = Math.min(100,
				Math.max(2, distanceFromPlayer - Engine_Main.mouseHandler.getScrollY() * 0.4f));

		rotation();

		view = Glm.lookAt_(cameraPosition, focusPoint, cameraUp);
	}

	private void updateProjectionMatrix() {
		projectionMatrix = Glm.perspective_((float) Math.toRadians(45),
				(float) Engine_Main.windowWidth / (float) Engine_Main.windowHeight, NEAR_CLIPPING_PLANE,
				FAR_CLIPPING_PLANE);
	}

	public void setFocusPoint(Vec3 focusPoint) {
		this.focusPoint = focusPoint;
	}

	public Mat4 getView() {
		return view;
	}

	public Mat4 getProjectionMatrix() {
		return projectionMatrix;
	}

	public Vec3 getCameraPosition() {
		return cameraPosition;
	}

	public float getCameraSpeed() {
		return cameraSpeed;
	}

}
