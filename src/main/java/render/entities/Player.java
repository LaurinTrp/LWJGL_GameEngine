package main.java.render.entities;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;

import java.util.ArrayList;
import java.util.Arrays;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.IRenderObject;
import main.java.render.Renderer;
import main.java.render.model.Model;
import main.java.render.model.MultiTextureTerrain;
import main.java.utils.ModelUtils;
import main.java.utils.constants.CameraMode;
import main.java.utils.constants.Constants;
import main.java.utils.loaders.ModelLoader;

public class Player extends Model {

	private Vec3 position;
	private Vec3 prevPosition;
	private final float speed = 0.5f;
	private boolean hasMoved;
	private Vec3 playerFront = new Vec3(0.0f, 0.0f, -1.0f);
	private Vec3 playerUp = new Vec3(0.0f, 1.0f, 0.0f);
	private Vec3 playerRight = new Vec3(1.0f, 0.0f, 0.0f);
	private Vec3 direction = new Vec3();
	
	private IRenderObject currentTerrain;

	private float rotationAngle = 0;

	private ArrayList<IRenderObject> intersectors = new ArrayList<>();

	public Player() {
		super((Model) ModelLoader.loadModelFromResource("AmongUs", "AmongUs.obj"));
		System.out.println(Arrays.toString(startMinmax));
		setShaderFolder("Transformation");
		getMaterial().setTexture(ModelLoader.loadMaterialFileFromResource("AmongUs", "AmongUs.mtl"));
	}

	@Override
	protected void renderProcessBegin() {
		super.renderProcessBegin();

		rotation();

	}

	@Override
	protected void renderProcessEnd() {
		super.renderProcessEnd();

		if (Renderer.camera.cameraMode == CameraMode.POV_CAMERA) {
			glDisable(GL_CULL_FACE);
		}
	}

	@Override
	public void afterInit() {
		super.afterInit();

		position = new Vec3(0.0, 0.0, 0.0);
		prevPosition = new Vec3(position);
		modelMatrix = modelMatrix.rotate((float)Math.toRadians(180), new Vec3(0.0, 1.0, 0.0));
		rotationAngle = (float) Math.toRadians(180);
		modelMatrix = modelMatrix.translation(position);
		updateMinmax();
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
	public boolean checkMovement() {
		direction = new Vec3();
		hasMoved = false;

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_W)) {
			direction.add(new Vec3(playerFront).mul(speed));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_S)) {
			direction.sub(new Vec3(playerFront).mul(speed));
		}

		playerRight = new Vec3(playerFront).cross(playerUp);

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_A)) {
			direction.sub(new Vec3(playerRight).mul(speed));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_D)) {
			direction.add(new Vec3(playerRight).mul(speed));
		}

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_Q)) {
			direction.add(new Vec3(playerUp).mul(speed));
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_X)) {
			direction.sub(new Vec3(playerUp).mul(speed));
		}

		if (!direction.equals(new Vec3())) {
			boolean isIntersecting = false;

			Float[] tempMinmax = ModelUtils.calculateMinmax(startMinmax, new Vec3(position).add(direction));

			for (IRenderObject model : intersectors) {
				if (ModelUtils.isIntersecting(tempMinmax, ((Model) model).getMinmax())) {
					isIntersecting = true;
					break;
				}
			}
			if (!isIntersecting) {
				hasMoved = true;
			}
		}
		
		return hasMoved;
	}
	
	public void move() {
		if (hasMoved) {
			position.add(direction);
			modelMatrix = modelMatrix.cleanTranslation();
			modelMatrix = modelMatrix.translation(position);
			updateMinmax();
		}
	}

	/**
	 * Applying gravity to the player
	 */
	public void gravity(MultiTextureTerrain terrain) {
		if(!init) {
			return;
		}
		if(terrain != null) {
			currentTerrain = terrain;
		}
		position.y = terrain.heightAtPlayerPos() - startMinmax[2];
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
	
	public Vec2 getPlayerPosXZ() {
		return new Vec2(position.x, position.z);
	}

	public void addIntersector(IRenderObject intersector) {
		intersectors.add(intersector);
	}

	public void removeIntersector(Model intersector) {
		intersectors.remove(intersector);
	}
	
	public IRenderObject getCurrentTerrain() {
		return currentTerrain;
	}
}
