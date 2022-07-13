package render;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.opengl.GL11.GL_FASTEST;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;

import glm.Glm;
import glm.mat._4.Mat4;
import glm.mat._4.d.Mat4d;
import glm.vec._3.Vec3;
import gui.Engine_Main;

public class Camera {
	private Vec3 cameraPosition = new Vec3(0.0f, 2.0f, 3.0f);
	private Vec3 cameraFront = new Vec3(0.0f, 0.0f, -1.0f);
	private Vec3 cameraUp = new Vec3(0.0f, 1.0f, 0.0f);
	private Vec3 cameraRight = new Vec3(1.0f, 0.0f, 0.0f);
	
	private Vec3 direction = new Vec3();

	private float angleHorizontal = 0.0f;
	private float angleVertical = 0.0f;

	private Mat4 view = new Mat4();
	private Mat4 projectionMatrix = new Mat4();

	private final float cameraSpeed = 0.05f;

	public Camera() {
		view = Glm.lookAt_(cameraPosition, utils.Glm.add(cameraPosition, cameraFront), cameraUp);
		projectionMatrix = Glm.perspective_(45.0f, (float)Engine_Main.windowWidth/(float)Engine_Main.windowHeight, 0.1f, 100.0f);
	}
	
	private void translation() {
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_W)) {
			cameraPosition = utils.Glm.add(cameraPosition, (utils.Glm.times(cameraFront, cameraSpeed)));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_S)) {
			cameraPosition = utils.Glm.subtract(cameraPosition, (utils.Glm.times(cameraFront, cameraSpeed)));
		}

		cameraRight = utils.Glm.cross(cameraFront, cameraUp);

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_A)) {
			cameraPosition = utils.Glm.subtract(cameraPosition, (utils.Glm.times(cameraRight, cameraSpeed)));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_D)) {
			cameraPosition = utils.Glm.add(cameraPosition, (utils.Glm.times(cameraRight, cameraSpeed)));
		}

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_Q)) {
			cameraPosition = utils.Glm.add(cameraPosition, (utils.Glm.times(cameraUp, cameraSpeed)));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_X)) {
			cameraPosition = utils.Glm.subtract(cameraPosition, (utils.Glm.times(cameraUp, cameraSpeed)));
		}
	}
	
	private void rotation() {

		angleVertical += Engine_Main.mouseHandler.getYoffset();
		angleHorizontal += Engine_Main.mouseHandler.getXoffset();

		if(angleVertical <= -89f) {
			angleVertical = -89f;
		}
		if(angleVertical >= 89f) {
			angleVertical = 89f;
		}
		
		cameraFront.set(Math.sin(Math.toRadians(angleHorizontal)), Math.sin(Math.toRadians(angleVertical)),
				-Math.cos(Math.toRadians(angleHorizontal)));
	}
	private void rotationKeys() {

		if(Engine_Main.keyHandler.isPressed(GLFW_KEY_F)) {
			angleVertical = -1;
		}
		if(angleVertical >= 89f) {
			angleVertical = 89f;
		}
		
		cameraFront.set(Math.sin(Math.toRadians(angleHorizontal)), Math.sin(Math.toRadians(angleVertical)),
				-Math.cos(Math.toRadians(angleHorizontal)));
	}
	
	public void moveCamera() {
		
		updateProjectionMatrix();
		
//		rotationKeys();
		rotation();
		translation();
		
		view = Glm.lookAt_(cameraPosition, utils.Glm.add(cameraPosition, cameraFront), cameraUp);
	}
	
	private void updateProjectionMatrix() {
		projectionMatrix = Glm.perspective_((float)Math.toRadians(45), (float)Engine_Main.windowWidth/(float)Engine_Main.windowHeight, 0.1f, 100f);
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

}
