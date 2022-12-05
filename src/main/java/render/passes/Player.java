package main.java.render.passes;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

import java.util.Arrays;

import org.lwjgl.glfw.GLFW;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.Renderer;
import main.java.render.model.Model;
import main.java.utils.loaders.ModelLoader;
import main.java.utils.math.Glm;

public class Player extends Model {

	private Vec3 position;
	private final float speed = 0.05f;
	
	private Vec3 cameraFront = new Vec3(0.0f, 0.0f, -1.0f);
	private Vec3 cameraUp = new Vec3(0.0f, 1.0f, 0.0f);
	private Vec3 cameraRight = new Vec3(1.0f, 0.0f, 0.0f);
	
	public Player() {
		super(ModelLoader.loadModelFromResource("AmongUs", "AmongUs.obj"));
		
		setShaderFolder("Transformation");
		getMaterial().setTexture(ModelLoader.loadMaterialFileFromResource("AmongUs", "AmongUs.mtl"));
	}
	
	@Override
	protected void renderProcess() {
		super.renderProcess();

		rotation();
		movement();
//		updateMinmax();
		gravity();
		
		modelMatrix = modelMatrix.cleanTranslation();
		modelMatrix = modelMatrix.translation(position);
	}
	
	@Override
	public void afterInit() {
		super.afterInit();
		
		position = new Vec3(5.0, 5.0, 0.0);
		modelMatrix.translation(position);
		
		modelMatrix.print("Player");
	}
	
	/**
	 * Rotate the player around its axis
	 */
	private void rotation() {

		if(!Engine_Main.mouseHandler.isRMB_Down()) {
			return;
		}
		
		double mouseXOffset = Engine_Main.mouseHandler.getXoffset();
		modelMatrix.rotateY(Math.toRadians(mouseXOffset * Renderer.camera.getCameraSpeed()));
				
//		if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_LEFT)) {
//			modelMatrix.rotateY(Math.toRadians(-1));
//		}
//		if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_RIGHT)) {
//			modelMatrix.rotateY(Math.toRadians(1));
//		}	
		
	}
	
	/**
	 * Move the player model
	 */
	private void movement() {
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_W)) {
			position = main.java.utils.math.Glm.add(position, (main.java.utils.math.Glm.times(cameraFront, speed)));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_S)) {
			position = main.java.utils.math.Glm.subtract(position, (main.java.utils.math.Glm.times(cameraFront, speed)));
		}

		cameraRight = main.java.utils.math.Glm.cross(cameraFront, cameraUp);

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_A)) {
			position = main.java.utils.math.Glm.subtract(position, (main.java.utils.math.Glm.times(cameraRight, speed)));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_D)) {
			position = main.java.utils.math.Glm.add(position, (main.java.utils.math.Glm.times(cameraRight, speed)));
		}

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_Q)) {
			position = main.java.utils.math.Glm.add(position, (main.java.utils.math.Glm.times(cameraUp, speed)));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_X)) {
			position = main.java.utils.math.Glm.subtract(position, (main.java.utils.math.Glm.times(cameraUp, speed)));
		}
	}
	
	/**
	 * Applying gravity to the player
	 */
	private void gravity() {
		boolean onTerrain = false;

		TerrainModel terrain = null;
		for (TerrainModel myTerrain : Renderer.terrains) {
			onTerrain = myTerrain.isOnTerrain(new Vec2(position.x,position.z));

			if(onTerrain) {
				terrain = myTerrain;	
				break;
			}
		}
		
		if(onTerrain) {
			position.y = terrain.heightAtPosition(new Vec2(position.x, position.z)) - minmax[2];
		}else {
			position.y -= speed;
		}
	}
	
	/**
	 * Get the position of the player
	 * @return position as vec3
	 */
	public Vec3 getPosition() {
		return position;
	}
}
