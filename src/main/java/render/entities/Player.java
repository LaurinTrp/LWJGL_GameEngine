package main.java.render.entities;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

import java.util.ArrayList;

import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.Renderer;
import main.java.render.camera.CameraMode;
import main.java.render.model.Model;
import main.java.render.model.MultiTextureTerrain;
import main.java.render.renderobject.IRenderObject;
import main.java.render.utils.BoundingBox;
import static main.java.utils.constants.Constants.*;
import main.java.utils.loaders.ModelLoader;

public class Player extends Model {

	private static Mat4 modelMatrix = new Mat4(1.0f);

	private Vec3 position;
	private Vec3 prevPosition;
	private boolean hasMoved;
	private Vec3 playerFront = new Vec3(0.0f, 0.0f, -1.0f);
	private Vec3 playerUp = new Vec3(0.0f, 1.0f, 0.0f);
	private Vec3 playerRight = new Vec3(1.0f, 0.0f, 0.0f);
	private Vec3 direction = new Vec3();

	private MultiTextureTerrain currentTerrain;

	private float rotationAngle = 0;

	private ArrayList<IRenderObject> intersectors = new ArrayList<>();

	public Player() {
		super("AmongUs", "AmongUs.obj", modelMatrix);
		setShaderFolder("Transformation");
		getMaterial().setTexture(loader.loadMaterialFileFromResource("AmongUs", "AmongUs.mtl"));

		setShowMinMax(true);
	}

	@Override
	protected void renderProcessBegin() {
		if (Renderer.camera.cameraMode == CameraMode.POV_CAMERA) {
//			glEnable(GL_CULL_FACE);
		}
		rotation();
	}

	@Override
	protected void renderProcessEnd() {
		if (Renderer.camera.cameraMode == CameraMode.POV_CAMERA) {
//			glDisable(GL_CULL_FACE);
		}
	}

	@Override
	public void afterInit() {
		super.afterInit();

		position = new Vec3(0.0, 0.0, 0.0);
		prevPosition = new Vec3(position);
		modelMatrix = modelMatrix.rotate((float) Math.toRadians(180), new Vec3(0.0, 1.0, 0.0));
		rotationAngle = (float) Math.toRadians(180);
		modelMatrix = modelMatrix.translation(position);
	}

	/**
	 * Rotate the player around its axis
	 */
	private void rotation() {
		double rotationSpeed = Renderer.camera.cameraMode == CameraMode.POV_CAMERA ? -PLAYER_ROTATION_SPEED
				: PLAYER_ROTATION_SPEED;

		if (Engine_Main.mouseHandler.getXoffset() > 0) {
			modelMatrix.rotateY(rotationSpeed);
			rotationAngle += rotationSpeed;
		}
		if (Engine_Main.mouseHandler.getXoffset() < 0) {
			modelMatrix.rotateY(-rotationSpeed);
			rotationAngle -= rotationSpeed;
		}

		rotationAngle %= Math.PI * 2;

		playerFront = new Vec3(-Math.sin(rotationAngle), 0, -Math.cos(rotationAngle));

	}

	/**
	 * Move the player model
	 */
	public boolean checkMovement() {

		direction = new Vec3(0.0f);
		hasMoved = false;

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_W)) {
			direction.add(new Vec3(playerFront)).mul(PLAYER_WALKING_SPEED);
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_S)) {
			direction.sub(new Vec3(playerFront)).mul(PLAYER_WALKING_SPEED);
		}

		playerRight = new Vec3(playerFront).cross(playerUp);

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_A)) {
			direction.sub(new Vec3(playerRight)).mul(PLAYER_WALKING_SPEED);
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_D)) {
			direction.add(new Vec3(playerRight)).mul(PLAYER_WALKING_SPEED);
		}

		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_Q)) {
			direction.add(new Vec3(playerUp)).mul(PLAYER_WALKING_SPEED);
		}
		if (Engine_Main.keyHandler.isPressed(GLFW_KEY_X)) {
			direction.sub(new Vec3(playerUp)).mul(PLAYER_WALKING_SPEED);
		}

		if (!direction.equals(new Vec3(0.0f))) {

			if (!isFutureIntersecting(direction)) {
				hasMoved = true;
			}
		}

		return hasMoved;
	}

	private boolean isFutureIntersecting(Vec3 direction) {

		Vec3 tempPosition = new Vec3(position).add(direction);
		Mat4 tempModelMatrix = new Mat4(modelMatrix).cleanTranslation().translation(tempPosition);

		BoundingBox tempBoundryBox = new BoundingBox(getBoundingBox().getStartMinmax(), tempModelMatrix);
		for (IRenderObject model : intersectors) {
			Model intersectorModel = (Model) model;
			if(intersectorModel.getBoundingBoxes() == null) {
				continue;
			}
			for (BoundingBox boundingBox : intersectorModel.getBoundingBoxes()) {
				if (BoundingBox.collision(tempBoundryBox, boundingBox)) {
					return true;
				}
			}
		}
		return false;
	}

	public void move() {
		if (hasMoved) {
			position = position.add(direction);
			modelMatrix = modelMatrix.cleanTranslation();
			modelMatrix = modelMatrix.translation(position);
		}
	}

	/**
	 * Applying gravity to the player
	 */
	public void gravity(MultiTextureTerrain terrain) {
		if (!init) {
			return;
		}
		if (terrain != null || terrain != currentTerrain) {
			currentTerrain = terrain;
		}
		position.y = terrain.heightAtPlayerPos() - getBoundingBox().getStartMinmax()[2];
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
