package main.java.utils.math;

import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._4.Vec4;
import main.java.gui.Engine_Main;
import main.java.render.camera.Camera;
import main.java.render.model.SingleModel;
import main.java.render.passes.TerrainModel;
import main.java.render.renderobject.IRenderObject;

public class MousePicker {
	private Vec4 currentRay;
	private Mat4 projectionMatrix;
	private Mat4 viewMatrix;
	private Camera camera;

	private final int RECURSION_STEPS = 5;
	private final byte START_POINT = 0;
	private final byte END_POINT = 1;

	public MousePicker(Camera camera) {
		this.camera = camera;
		this.projectionMatrix = new Mat4(camera.getProjectionMatrix());
		this.viewMatrix = new Mat4(camera.getView());
	}

	public Vec4 getCurrentRay() {
		return currentRay;
	}

	private void update() {
		this.viewMatrix = new Mat4(camera.getView());
		currentRay = calculateMouseRay();
	}

	public Vec4 terrainIntersection(SingleModel terrain) {
		update();
		System.out.println(intersectionInRange(0, 200, currentRay, terrain));
//		System.out.println(getCurrentRay());
//		intersectionInRange(0, 6000, getCurrentRay(), terrain);


		return null;
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


	// ####################
	// Terrain Intersection
	// ####################

	private Vec4 getPointOnRay(Vec4 ray, float distance) {
		Vec4 camPos = new Vec4(camera.getCameraPosition(), 1.0f);
		Vec4 start = new Vec4(camPos);
		Vec4 scaledRay = new Vec4(ray).mul(distance);
		System.out.println(scaledRay);
		return new Vec4(start).add(scaledRay);
	}

	private boolean intersectionInRange(float start, float end, Vec4 ray, IRenderObject terrain) {
		Vec4 startPoint = getPointOnRay(ray, start);
		Vec4 endPoint = getPointOnRay(ray, end);
		if(!isUnderGround(startPoint, terrain, START_POINT) && isUnderGround(endPoint, terrain, END_POINT)) {
			return true;
		}
		return false;
	}

	private boolean isUnderGround(Vec4 point, IRenderObject terrain, byte pointType) {
		float height = 0;
		if(terrain != null) {
			if(!((TerrainModel)terrain).isOnTerrain(new Vec2(point.x, point.z))) {
				return pointType == END_POINT;
			}
			height = ((TerrainModel) terrain).heightAtPosition(new Vec2(point.x, point.z));
		}
		if(point.y < height) {
			return true;
		}
		return false;
	}


}
