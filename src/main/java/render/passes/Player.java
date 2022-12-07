package main.java.render.passes;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.Renderer;
import main.java.render.model.Model;
import main.java.utils.loaders.ModelLoader;

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
	}
	
	/**
	 * Move the player model
	 */
	private void movement() {
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_W)) {
			position.add(new Vec3(cameraFront).mul(speed));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_S)) {
			position.sub(new Vec3(cameraFront).mul(speed));
		}

		cameraRight = new Vec3(cameraFront).cross(cameraUp);

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_A)) {
			position.sub(new Vec3(cameraRight).mul(speed));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_D)) {
			position.add(new Vec3(cameraRight).mul(speed));
		}

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_Q)) {
			position.add(new Vec3(cameraUp).mul(speed));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_X)) {
			position.sub(new Vec3(cameraUp).mul(speed));
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
