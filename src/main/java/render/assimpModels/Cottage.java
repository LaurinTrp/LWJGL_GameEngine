package main.java.render.assimpModels;

import org.lwjgl.glfw.GLFW;

import glm.vec._3.Vec3;
import main.java.data.Material;
import main.java.gui.Engine_Main;
import main.java.model.AssimpModel;
import main.java.utils.loaders.AssimpModelLoader;
import main.java.utils.loaders.ImageLoader;

public class Cottage extends AssimpModel {

	public Cottage() {
		super(AssimpModelLoader.loadStaticFromResource("Cottage", "cottage.obj"));
		this.material = new Material(ImageLoader.loadTextureFromResource("cottage", "cottage_diffuse.png"));
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
		modelMatrix.scale(0.5f);
//		scale(0.5f);
	}
}
