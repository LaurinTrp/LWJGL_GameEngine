package main.java.render;

import glm.glm.mat._4.Mat4;
import glm.glm.vec._2.Vec2;
import glm.glm.vec._4.Vec4;
import main.java.gui.Engine_Main;

public class MousePicker {
	private Vec4 currentRay;
	private Mat4 projectionMatrix;
	private Mat4 viewMatrix;
	private Camera camera;
	
	public MousePicker(Camera camera) {
		this.camera = camera;
		this.projectionMatrix = new Mat4(camera.getProjectionMatrix());
		this.viewMatrix = new Mat4(camera.getView());
	}
	
	public Vec4 getCurrentRay() {
		return currentRay;
	}
	
	public void update() {
		this.viewMatrix = new Mat4(camera.getView());
		currentRay = calculateMouseRay();
//		System.err.println(currentRay);
	}
	
	private Vec4 calculateMouseRay() {
		double mouseX = Engine_Main.mouseHandler.getMouseX();
		double mouseY = Engine_Main.mouseHandler.getMouseY();
		Vec2 normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		Vec4 clipCoords = new Vec4(normalizedCoords.x, normalizedCoords.y, -1d, 1d); 
		Vec4 eyeCoords = toEyeCoords(clipCoords);
		Vec4 worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	private Vec4 toWorldCoords(Vec4 eyeCoords) {
		Mat4 invertedView = new Mat4(viewMatrix).inverse_();
		Vec4 rayWorld = invertedView.mul(eyeCoords);
		rayWorld.normalize();
		return rayWorld;
	}
	
	private Vec4 toEyeCoords(Vec4 clipCoords) {
		Mat4 invertedProj = new Mat4(projectionMatrix).inverse_();
		Vec4 eyeCoords = invertedProj.mul(clipCoords);
		return new Vec4(eyeCoords.x, eyeCoords.y, -1d, 1d);
	}
	
	private Vec2 getNormalizedDeviceCoords(double mouseX, double mouseY) {
		double x = (2d*mouseX) / Engine_Main.windowWidth-1;
		double y = (2d*mouseY) / Engine_Main.windowHeight-1;
		return new Vec2(x,y);
	}
	
}
