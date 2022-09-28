package main.java.render.passes;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

import glm.glm.vec._2.Vec2;
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
	
	public Player() {
		super(ModelLoader.loadModelFromResource("AmongUs", "AmongUs.obj"));
		setShaderFolder("Transformation");
		getMaterial().setTexture(ModelLoader.loadMaterialFileFromResource("AmongUs", "AmongUs.mtl"));
	}
	
	@Override
	protected void renderProcess() {
		super.renderProcess();

//		rotation();
		movement();
		updateMinmax();
		gravity();

		modelMatrix.cleanTranslation();
		modelMatrix.translate(new Vec3(position).div(getScale()));
		
	}
	
	@Override
	public void afterInit() {
		super.afterInit();

		setScale(0.01f);
		
		position = new Vec3(0f, 5f, 0f);
		modelMatrix.translate(position);
	}
	
	private void rotation() {

		double offsetX = Engine_Main.mouseHandler.getXoffset();
		if(!Engine_Main.mouseHandler.isRMB_Down()) {
			return;
		}
		
		Vec3 position = Glm.matTranslation(modelMatrix);
		modelMatrix.translate(new Vec3());
		
		if(offsetX > 0) {
			modelMatrix = modelMatrix.rotateY(Math.toRadians(-1d));
		}
		if(offsetX < 0) {
			modelMatrix = modelMatrix.rotateY(Math.toRadians(1d));
		}
		
		modelMatrix.translate(position);
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
	}
	
	private void gravity() {
		if(position.x > Renderer.terrain.getStartX() && position.x < Renderer.terrain.getStartX() + Renderer.terrain.getWidth()
			&& position.z > Renderer.terrain.getStartZ() && position.z < Renderer.terrain.getStartZ() + Renderer.terrain.getHeight()) {
			float yDiff = position.y - minmax[2];
			position.y = Renderer.terrain.heightAtPosition(new Vec2(position.x, position.z)) + yDiff;
		}else {
			position.y -= speed;
		}
	}
	
	public Vec3 getPosition() {
		return position;
	}
}
