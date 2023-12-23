package main.java.render.passes;

import org.lwjgl.glfw.GLFW;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.model.Model;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Cottage extends Model {

	private static Mat4 modelMatrix = new Mat4(1.0f);
	
	public Cottage() {
		super("Cottage", "cottage.obj", modelMatrix);
		setShaderFolder("Transformation");
		getMaterial().setTexture(ImageLoader.loadTextureFromResource("cottage_diffuse.png"));
		setShowMinMax(true);
	}

	@Override
	protected void renderProcessBegin() {
		if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_LEFT)) {
			modelMatrix.rotateY(Math.toRadians(-1));
		}
		if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_RIGHT)) {
			modelMatrix.rotateY(Math.toRadians(1));
		}
	}
	
	@Override
	protected void renderProcessEnd() {
	}

	@Override
	public void afterInit() {
		modelMatrix.translate(new Vec3(10f, 1f, 10f));

		scale(0.5f);
	}
}
