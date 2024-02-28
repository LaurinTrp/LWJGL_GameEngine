package main.java.render.passes;

import org.lwjgl.glfw.GLFW;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.model.Material;
import main.java.render.model.Model;
import main.java.render.model.assimp.AssimpModel;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.StaticMeshesLoader;

public class Cottage extends AssimpModel {

	public Cottage() {
		super(StaticMeshesLoader.loadFromResource("Cottage", "cottage.obj"));
//		super("Cottage", "cottage.obj", modelMatrix);
//		setShaderFolder("Transformation");
		this.material = new Material(ImageLoader.loadTextureFromResource("cottage", "cottage_diffuse.png"));
//		setShowMinMax(true);
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
