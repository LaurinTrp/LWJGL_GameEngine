package main.java.render.passes;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

import glm.glm.vec._3.Vec3;
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
	
	private float currentTerrainHeight;
	
	public Player() {
		super(ModelLoader.loadModelFromResource("AmongUs", "AmongUs.obj"));
		setShaderFolder("Transformation");
		getMaterial().setTexture(ModelLoader.loadMaterialFromResource("AmongUs", "AmongUs.mtl"));
	}
	
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
		
		
		modelMatrix.cleanTranslation();
		modelMatrix.translate(new Vec3(position).div(getScale()));
	}
	
	private void gravity() {
		
//		if(minmax[2]-speed > currentTerrainHeight) {
//			position = Glm.subtract(position, Glm.times(cameraUp, speed));
//		}else {
//		}
		float yDiff = position.y - minmax[2];
		position.y = currentTerrainHeight + yDiff;
	}
	
	@Override
	protected void renderProcess() {
		super.renderProcess();

		movement();
		updateMinmax();
		terrainCollision(Renderer.terrain);
		gravity();
		
	}
	
	public void terrainCollision(TerrainPass terrain) {
		if(minmax[0] > -terrain.getWidth()/2f && minmax[1] < terrain.getWidth()/2f
				&& minmax[4] > -terrain.getHeight()/2f && minmax[5] < terrain.getHeight()/2f) {
			currentTerrainHeight = terrain.heightAtPosition(position);
		}
	}

	@Override
	public void afterInit() {
		super.afterInit();

		setScale(0.01f);
		
		position = new Vec3(0f, 5f, 0f);
		modelMatrix.translate(position);
		
	}
	
	public Vec3 getPosition() {
		return position;
	}
}
