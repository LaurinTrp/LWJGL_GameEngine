package main.java.render;

import glm.Glm;
import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.passes.Player;

public class Camera {
	
	private Player player;
	
	private Vec3 cameraPosition = new Vec3(0.0f, 0.0f, 0.0f);
	private Vec3 cameraFront = new Vec3(0.0f, 0.0f, -1.0f);
	private Vec3 cameraUp = new Vec3(0.0f, 1.0f, 0.0f);
	private Vec3 cameraRight = new Vec3(1.0f, 0.0f, 0.0f);
	
	private float angleHorizontal = 0.0f;
	private float angleVertical = 0.0f;

	private Mat4 view = new Mat4();
	private Mat4 projectionMatrix = new Mat4();

	private final float cameraSpeed = 0.5f;
	
	private float distanceFromPlayer = 10f;

	public Camera(Player player) {
		
		this.player = player;
		
		projectionMatrix = Glm.perspective_(45.0f, (float)Engine_Main.windowWidth/(float)Engine_Main.windowHeight, 0.1f, 100.0f);
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
		
		calculateCameraPos(angleVertical, angleHorizontal);
	}
	
	private void calculateCameraPos(float angleVertical, float angleHorizontal) {
		float verticalDistance = (float) (distanceFromPlayer * Math.sin(Math.toRadians(angleVertical * cameraSpeed)));
		float horizontalDistance = (float) (distanceFromPlayer * Math.cos(Math.toRadians(angleVertical * cameraSpeed)));
		
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(angleHorizontal * cameraSpeed)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(angleHorizontal * cameraSpeed)));
		
		cameraPosition.x = player.getPosition().x - offsetX;
		cameraPosition.y = player.getPosition().y + verticalDistance;
		cameraPosition.z = player.getPosition().z - offsetZ;
		

		float terrainHeight = Renderer.terrain.heightAtPosition(new Vec2(cameraPosition.x, cameraPosition.z));
		if(cameraPosition.y < terrainHeight + 0.2f) {
			cameraPosition.y = terrainHeight + 0.2f;
		}
	}
	
	public void moveCamera() {
		
		updateProjectionMatrix();
		
		if(distanceFromPlayer <= 10 && distanceFromPlayer >= 2) {
			distanceFromPlayer = Math.min(10, Math.max(2, distanceFromPlayer - Engine_Main.mouseHandler.getScrollY() * 0.4f));
		}
		
		rotation();
		
		view = Glm.lookAt_(cameraPosition, main.java.utils.math.Glm.add(player.getPosition(), cameraFront), cameraUp);
		
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
