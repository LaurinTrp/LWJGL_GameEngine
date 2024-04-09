package main.java.camera;

import static main.java.utils.constants.Constants.POV_CAM_VERTICAL_SPEED;

import org.lwjgl.glfw.GLFW;

import glm.Glm;
import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.assimpModels.Player;
import main.java.render.passes.MultiTextureTerrain;

public class Camera {

	private Player player;

	protected Vec3 initialCameraPosition = new Vec3(0.0f, 0.0f, 0.0f);
	protected Vec3 cameraPosition = new Vec3(0.0f, 0.0f, 0.0f);
	protected Vec3 cameraFront = new Vec3(0.0f, 0.0f, -1.0f);
	protected Vec3 cameraUp = new Vec3(0.0f, 1.0f, 0.0f);

	private float angleHorizontal = 0.0f;
	private float angleVertical = 0.0f;

	private Mat4 view = new Mat4();
	private Mat4 projectionMatrix = new Mat4();

	private final float cameraSpeed = 0.3f;

	private float distanceFromPlayer = 10f;

	private Vec3 focusPoint;

	public CameraMode cameraMode = CameraMode.PLAYER_CAMERA;

	private final float NEAR_CLIPPING_PLANE = 0.1f, FAR_CLIPPING_PLANE = 1000f;

	private boolean buttonV_ready = true;

	public Camera(Player player) {
		calculateProjectionMatrix(45.0f, (float) Engine_Main.windowWidth / (float) Engine_Main.windowHeight,
				NEAR_CLIPPING_PLANE, FAR_CLIPPING_PLANE);
		focusPoint = new Vec3();
		this.player = player;
	}
	
	protected void calculateProjectionMatrix(float fov, float aspect, float near, float far) {
		projectionMatrix = Glm.perspective_(fov, aspect,
				near, far);
	}

	public void update() {
		moveCamera();

		toggleCameraMode();
	}

	private void toggleCameraMode() {
		if (Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_V) && buttonV_ready) {
			if (cameraMode == CameraMode.POV_CAMERA) {
				cameraMode = CameraMode.PLAYER_CAMERA;
			} else if (cameraMode == CameraMode.PLAYER_CAMERA) {
				cameraMode = CameraMode.POV_CAMERA;
			}
			buttonV_ready = false;
		}
		buttonV_ready = Engine_Main.keyHandler.isReleased(GLFW.GLFW_KEY_V);
	}

	private void rotation() {
		double rotationSpeed = cameraMode == CameraMode.POV_CAMERA ? -POV_CAM_VERTICAL_SPEED
				: POV_CAM_VERTICAL_SPEED;

		switch (cameraMode) {
		case PLAYER_CAMERA: {
			angleHorizontal = player.getRotationAngle() - (float) Math.toRadians(180);

			if (Engine_Main.mouseHandler.getYoffset() > 0) {
				angleVertical += rotationSpeed;
			}
			if (Engine_Main.mouseHandler.getYoffset() < 0) {
				angleVertical -= rotationSpeed;
			}
			
			angleVertical = Math.max(angleVertical, -89);
			angleVertical = Math.min(angleVertical, 89);

			focusPoint = new Vec3(player.getFocusPoint());

			calculateCameraPos(angleVertical, angleHorizontal);
			break;
		}
		case POV_CAMERA: {
			if (Engine_Main.mouseHandler.getYoffset() > 0) {
				angleVertical -= rotationSpeed;
			}
			if (Engine_Main.mouseHandler.getYoffset() < 0) {
				angleVertical += rotationSpeed;
			}

			angleVertical = Math.max(angleVertical, -89);
			angleVertical = Math.min(angleVertical, 89);

			cameraFront = new Vec3(player.getPlayerFront());
			cameraFront.y = (float) Math.sin(Math.toRadians(angleVertical));

			cameraPosition = new Vec3(player.getPosition()).add(initialCameraPosition);

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

		float groundHeight = groundIntersection();
		cameraPosition.y = Math.max(cameraPosition.y, groundHeight + 2f);
	}

	private float groundIntersection() {

		MultiTextureTerrain terrain = (MultiTextureTerrain) player.getCurrentTerrain();
		if (terrain == null) {
			return 0f;
		}
		return terrain.heightAtPosition(new Vec2(cameraPosition.x, cameraPosition.z));
	}

	public void moveCamera() {

		distanceFromPlayer = Math.min(200,
				Math.max(3, distanceFromPlayer - Engine_Main.mouseHandler.getScrollY() * 5f));

		rotation();

		view = Glm.lookAt_(cameraPosition, focusPoint, cameraUp);
	}

	@SuppressWarnings("unused")
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
