package main.java.render.passes.transformation;

import org.lwjgl.glfw.GLFW;

import main.java.gui.Engine_Main;
import main.java.render.model.Model;
import main.java.render.model.NormalDrawing;
import main.java.utils.loaders.ModelLoader;

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
