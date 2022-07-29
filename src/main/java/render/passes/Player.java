package main.java.render.passes;

import static org.lwjgl.glfw.GLFW.*;

import glm.glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.model.Model;
import main.java.utils.loaders.ModelLoader;

public class Player extends Model {

	private Vec3 position;
	private final float speed = 0.05f;
	
	private Vec3 cameraFront = new Vec3(0.0f, 0.0f, -1.0f);
	private Vec3 cameraUp = new Vec3(0.0f, 1.0f, 0.0f);
	private Vec3 cameraRight = new Vec3(1.0f, 0.0f, 0.0f);
	
	public Player() {
		super(ModelLoader.loadModelFromResource("Cube", "cube.obj"));
		setShaderFolder("Transformation");
		getMaterial().setTexture(ModelLoader.loadMaterialFromResource("Cube", "cube.mtl"));
	}
	
	@Override
	protected void renderProcess() {
		super.renderProcess();
		
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
		modelMatrix.translate(position);
	}

	@Override
	public void afterInit() {
		super.afterInit();

		position = new Vec3(0f, 5f, 0f);
		modelMatrix.translate(position);
	}
	
	public Vec3 getPosition() {
		return position;
	}
}
