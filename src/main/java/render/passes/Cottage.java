package main.java.render.passes;

import org.lwjgl.glfw.GLFW;

import main.java.gui.Engine_Main;
import main.java.render.model.Model;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Cottage extends Model {

	public Cottage() {
		super((Model) ModelLoader.loadModelFromResource("Cottage", "cottage.obj"));
		setShaderFolder("Transformation");
		getMaterial().setTexture(ImageLoader.loadTextureFromResource("cottage_diffuse.png"));
		
	}
	
	@Override
	protected void renderProcessBegin() {
		super.renderProcessBegin();
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
