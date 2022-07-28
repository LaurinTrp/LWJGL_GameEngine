package main.java.render.passes.transformation;

import org.lwjgl.glfw.GLFW;

import main.java.gui.Engine_Main;
import main.java.render.model.Model;
import main.java.utils.loaders.ModelLoader;

public class Cottage extends Model {

	public Cottage() {
		super(ModelLoader.loadModelFromResource("Cottage", "cottage.obj"));
		setShaderFolder("Cottage");
		getMaterial().setTexture(ModelLoader.loadMaterialFromResource("Cottage", "cottage.mtl"));
		
	}
	
	@Override
	protected void renderProcess() {
		super.renderProcess();
		if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_LEFT)) {
			modelMatrix.rotateY(Math.toRadians(-1));
		}
		if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_RIGHT)) {
			modelMatrix.rotateY(Math.toRadians(1));
		}
	}

	@Override
	public void afterInit() {
		modelMatrix.scale(.2f);
	}
}
