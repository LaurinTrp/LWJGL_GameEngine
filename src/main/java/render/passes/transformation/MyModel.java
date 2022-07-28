package main.java.render.passes.transformation;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;

import glm.glm.vec._4.Vec4;
import main.java.gui.Engine_Main;
import main.java.render.model.Model;
import main.java.render.model.NormalDrawing;
import main.java.utils.loaders.ModelLoader;

public class MyModel extends Model{
	
//	private NormalDrawing normalDrawing;
	
	public MyModel() {
		super(ModelLoader.loadModelFromResource("Cube", "cube.obj"));
		setShaderFolder("Transformation");
		getMaterial().setTexture(ModelLoader.loadMaterialFromResource("Cube", "cube.mtl"));
		
//		normalDrawing = new NormalDrawing(this);
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
		GL20.glUniform4fv(getProgram().getUniformLocation("lightsource"), new Vec4(0f, 10f, 10f, 0f).toFA_());
	}
	
	@Override
	public void render() {
		super.render();
//		normalDrawing.render();
	}

	@Override
	public void afterInit() {
//		modelMatrix.scale(2.0f);
		
	}
}
