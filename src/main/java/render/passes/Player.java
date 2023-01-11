package main.java.render.passes;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.IRenderObject;
import main.java.render.Renderer;
import main.java.render.model.Model;
import main.java.utils.constants.CameraMode;
import main.java.utils.constants.Constants;
import main.java.utils.loaders.ModelLoader;

public class Player extends Model {

	private Vec3 position;
	private final float speed = 0.05f;

	private Vec3 playerFront = new Vec3(0.0f, 0.0f, -1.0f);
	private Vec3 playerUp = new Vec3(0.0f, 1.0f, 0.0f);
	private Vec3 playerRight = new Vec3(1.0f, 0.0f, 0.0f);

	private float rotationAngle = 0;

	public Player() {
		super(ModelLoader.loadModelFromResource("AmongUs", "AmongUs.obj"));

		setShaderFolder("Transformation");
		getMaterial().setTexture(ModelLoader.loadMaterialFileFromResource("AmongUs", "AmongUs.mtl"));
	}

	@Override
	protected void renderProcessBegin() {
		super.renderProcessBegin();
		
		if(Renderer.camera.cameraMode == CameraMode.POV_CAMERA) {
			glEnable(GL_CULL_FACE);
		}
		
		rotation();
		movement();
//		updateMinmax();
		gravity();

		modelMatrix = modelMatrix.cleanTranslation();
		modelMatrix = modelMatrix.translation(position);
	}
	
	@Override
	protected void renderProcessEnd() {
		super.renderProcessEnd();
		
		if(Renderer.camera.cameraMode == CameraMode.POV_CAMERA) {
			glDisable(GL_CULL_FACE);
		}
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
		if (Engine_Main.mouseHandler.getXoffset() > 0) {
			modelMatrix.rotateY(Constants.PLAYER_ROTATION_SPEED);
			rotationAngle += Constants.PLAYER_ROTATION_SPEED;
		}	
		if (Engine_Main.mouseHandler.getXoffset() < 0) {
			modelMatrix.rotateY(-Constants.PLAYER_ROTATION_SPEED);
			rotationAngle -= Constants.PLAYER_ROTATION_SPEED;
		}

		rotationAngle %= Math.PI * 2;

		playerFront = new Vec3(-Math.sin(rotationAngle), 0, -Math.cos(rotationAngle));

	}

	/**
	 * Move the player model
	 */
	private void movement() {
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_W)) {
			position.add(new Vec3(playerFront).mul(speed));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_S)) {
			position.sub(new Vec3(playerFront).mul(speed));
		}

		playerRight = new Vec3(playerFront).cross(playerUp);

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_A)) {
			position.sub(new Vec3(playerRight).mul(speed));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_D)) {
			position.add(new Vec3(playerRight).mul(speed));
		}

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_Q)) {
			position.add(new Vec3(playerUp).mul(speed));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_X)) {
			position.sub(new Vec3(playerUp).mul(speed));
		}
	}

	/**
	 * Applying gravity to the player
	 */
	private void gravity() {
		boolean onTerrain = false;

		IRenderObject terrain = null;
		for (IRenderObject myTerrain : Renderer.terrains) {
			onTerrain = ((TerrainModel) myTerrain).isOnTerrain(new Vec2(position.x, position.z));

			if (onTerrain) {
				terrain = myTerrain;
				break;
			}
		}

		if (onTerrain) {
			position.y = ((TerrainModel) terrain).heightAtPosition(new Vec2(position.x, position.z)) - minmax[2];
		} else {
			position.y -= speed;
		}
	}

	/**
	 * Get the position of the player
	 * 
	 * @return position as vec3
	 */
	public Vec3 getPosition() {
		return position;
	}

	public float getRotationAngle() {
		return rotationAngle;
	}
	
	public Vec3 getPlayerFront() {
		return playerFront;
	}
	
	public Vec3 getPlayerRight() {
		return playerRight;
	}
	
	public Vec3 getPlayerUp() {
		return playerUp;
	}
}
