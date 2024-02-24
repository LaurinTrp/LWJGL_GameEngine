package main.java.render.camera;

import glm.Glm;
import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;

public class ViewPole {

	private Vec3 cameraPosition = new Vec3(0.0f, 0.0f, 0.0f);
	private Vec3 cameraFront = new Vec3(0.0f, 0.0f, -1.0f);
	private Vec3 cameraUp = new Vec3(0.0f, 1.0f, 0.0f);
	private Vec3 cameraRight = new Vec3(1.0f, 0.0f, 0.0f);

	private Vec3 focusPoint;
	
	private float angleHorizontal = 0.0f;
	private float angleVertical = 0.0f;

	private Mat4 view = new Mat4();
	private Mat4 projectionMatrix = new Mat4();

	private final float NEAR_CLIPPING_PLANE = 0.1f, FAR_CLIPPING_PLANE = 100f;
	

	public ViewPole() {
		projectionMatrix = Glm.perspective_(45.0f, (float) Engine_Main.windowWidth / (float) Engine_Main.windowHeight,
				NEAR_CLIPPING_PLANE, FAR_CLIPPING_PLANE);
		focusPoint = new Vec3();
	}
	
	public void rotation() {
		angleVertical += Engine_Main.mouseHandler.getYoffset();

		if (angleVertical <= -89f) {
			angleVertical = -89f;
		}
		if (angleVertical >= 89f) {
			angleVertical = 89f;
		}

		focusPoint = new Vec3(cameraPosition).add(cameraFront);
	}
	
//	public void translate() {
//		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_W)) {
//			direction.add(new Vec3(playerFront).mul(speed));
//		}
//		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_S)) {
//			direction.sub(new Vec3(playerFront).mul(speed));
//		}
//
//		playerRight = new Vec3(playerFront).cross(playerUp);
//
//		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_A)) {
//			direction.sub(new Vec3(playerRight).mul(speed));
//		}
//		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_D)) {
//			direction.add(new Vec3(playerRight).mul(speed));
//		}
//
//		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_Q)) {
//			direction.add(new Vec3(playerUp).mul(speed));
//		}
//		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_X)) {
//			direction.sub(new Vec3(playerUp).mul(speed));
//		}
//	}
}
