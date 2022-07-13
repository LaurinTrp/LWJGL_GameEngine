package render.passes.transformation;

import org.lwjgl.glfw.GLFW;

import gui.Engine_Main;
import render.model.Model;
import render.model.NormalDrawing;
import utils.loaders.ModelLoader;

public class MyModel extends Model{
	
	private NormalDrawing normalDrawing;
	
	public MyModel() {
		super(ModelLoader.loadModel("/media/laurin/Laurin Festplatte/Blender/Models/CubeNumbers.obj"));
		setShaderFolder("Transformation");
		getMaterial().setTexture(ModelLoader.loadMaterial("/media/laurin/Laurin Festplatte/Blender/Models/CubeNumbers.obj"));
		
		normalDrawing = new NormalDrawing(this);
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
	public void render() {
		super.render();
		normalDrawing.render();
	}

	@Override
	public void afterInit() {
		
	}
}
