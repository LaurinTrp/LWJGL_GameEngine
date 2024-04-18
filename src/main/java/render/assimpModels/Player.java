package main.java.render.assimpModels;

import static main.java.utils.constants.Constants.PLAYER_ROTATION_SPEED;
import static main.java.utils.constants.Constants.PLAYER_WALKING_SPEED;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;

import org.lwjgl.assimp.AIScene;

import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.camera.CameraMode;
import main.java.camera.PlayerCamera;
import main.java.data.Material;
import main.java.data.sounds.SoundListener;
import main.java.gui.Engine_Main;
import main.java.model.AssimpModel;
import main.java.render.Renderer;
import main.java.render.passes.MultiTextureTerrain;
import main.java.render.renderobject.IRenderObject;
import main.java.utils.loaders.AssimpModelLoader;
import main.java.utils.loaders.ImageLoader;

public class Player extends AssimpModel {

	private Vec3 position;
	@SuppressWarnings("unused")
	private Vec3 prevPosition;
	private boolean hasMoved;
	private Vec3 playerFront = new Vec3(0.0f, 0.0f, -1.0f);
	private Vec3 playerUp = new Vec3(0.0f, 1.0f, 0.0f);
	private Vec3 playerRight = new Vec3(1.0f, 0.0f, 0.0f);
	private Vec3 direction = new Vec3();

	private Vec3 focusPoint = new Vec3();

	private MultiTextureTerrain currentTerrain;

	private float rotationAngle = 0;

	private ArrayList<IRenderObject> intersectors = new ArrayList<>();

	private static AIScene scene;
	private PlayerCamera camera;

	private SoundListener soundListener;

	static {
		scene = AssimpModelLoader.loadStaticFromResource("Collada", "model.dae");
	}

	public Player() {
		super(scene);

		camera = new PlayerCamera(this, scene);

		this.material = new Material(ImageLoader.loadTextureFromResource("collada", "diffuse.png"));

		soundListener = new SoundListener();
	}

	@Override
	protected void renderProcessBegin() {
	}

	@Override
	public void render() {
		glEnable(GL_CULL_FACE);
		if (camera.cameraMode == CameraMode.POV_CAMERA) {
			render = false;
		} else {
			render = true;
		}

		super.render();
		rotation();

		glDisable(GL_CULL_FACE);
	}

	@Override
	protected void renderProcessEnd() {
	}

	@Override
	public void afterInit() {
		position = new Vec3(0.0, 0.0, 0.0);
		prevPosition = new Vec3(position);
		
		soundListener.setPosition(position.x, position.y, position.z);
		Engine_Main.soundManager.setListener(soundListener);

		modelMatrix = modelMatrix.translation(position);

		scale(0.5f);

		rotationAngle = (float) Math.toRadians(180);

		render = false;
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
		@SuppressWarnings("unused")
		Mat4 tempModelMatrix = new Mat4(modelMatrix).cleanTranslation().translation(tempPosition);

//		BoundingBox tempBoundryBox = new BoundingBox(getBoundingBox().getStartMinmax(), tempModelMatrix);
//		for (IRenderObject model : intersectors) {
//			AssimpModel intersectorModel = (AssimpModel) model;
//			if (intersectorModel.getBoundingBox() == null) {
//				continue;
//			}
//			if (BoundingBox.collision(tempBoundryBox, intersectorModel.getBoundingBox())) {
//				return true;
//			}
//		}
		return false;
	}

	public void move() {
		if (hasMoved) {
			position = position.add(direction);
			modelMatrix = modelMatrix.cleanTranslation();
			modelMatrix = modelMatrix.translation(position);
			
			soundListener.setPosition(position.x, position.y, position.z);
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
		position.y = terrain.heightAtPlayerPos() - startMinmax[2];

//		if(Renderer.camera.cameraMode == CameraMode.PLAYER_CAMERA) {
		focusPoint.set(position.x, terrain.heightAtPlayerPos() + Math.abs(startMinmax[3]) + Math.abs(startMinmax[2]),
				position.z);
//		}else if(Renderer.camera.cameraMode == CameraMode.POV_CAMERA) {
//			focusPoint.set(position.x, terrain.heightAtPlayerPos() + Math.abs(startMinmax[3]) + Math.abs(startMinmax[2]), position.z);
//		}
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

	public Vec3 getFocusPoint() {
		return focusPoint;
	}

	public void addIntersector(IRenderObject intersector) {
		intersectors.add(intersector);
	}

	public void removeIntersector(AssimpModel intersector) {
		intersectors.remove(intersector);
	}

	public IRenderObject getCurrentTerrain() {
		return currentTerrain;
	}

	public PlayerCamera getCamera() {
		return camera;
	}
}
