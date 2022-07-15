package main.java.render.passes.transformation;

import org.lwjgl.glfw.GLFW;

import main.java.gui.Engine_Main;
import main.java.render.model.Model;
import main.java.utils.loaders.ModelLoader;

public class Cottage extends Model {

	private final static String model = "/media/laurin/Laurin Festplatte/Blender/Models/Cottage/cottageWithCube.obj";
	
	public Cottage() {
		super(ModelLoader.loadModel(model));
		setShaderFolder("Transformation");
		getMaterial().setTexture(ModelLoader.loadMaterial(model));
		
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
