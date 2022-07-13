package render.passes.transformation;

import org.lwjgl.glfw.GLFW;

import glm.vec._4.Vec4;
import gui.Engine_Main;
import render.model.Model;
import render.model.NormalDrawing;
import utils.loaders.ModelLoader;

public class Cottage extends Model {

	private final static String model = "/media/laurin/Laurin Festplatte/Blender/Models/Cottage/cottageWithCube.obj";
	
	public Cottage() {
		super(ModelLoader.loadModel(model));
		initShader("Transformation");
		getMaterial().setTexture(ModelLoader.loadMaterial(model));
		bindModel();
		
//		getMaterial().setAmbientColor(new Vec4(1.0f, 0.0f, 0.0f, 1.0f));
		
		modelMatrix.scale(.2f);
		
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
}
