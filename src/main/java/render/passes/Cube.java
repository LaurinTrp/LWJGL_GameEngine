package main.java.render.passes;

import glm.mat._4.Mat4;
import main.java.render.model.Model;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Cube extends Model {

	private static Mat4 modelMatrix = new Mat4(1.0f);
	
	public Cube() {
		super("Cube", "cube.obj", modelMatrix);
		setShaderFolder("Transformation");
		getMaterial().setTexture(ImageLoader.loadTextureFromResource("Terrain/Terrain.png"));
	}

	@Override
	protected void afterInit() {
		scale(3);
	}

	@Override
	protected void renderProcessBegin() {
	}

	@Override
	protected void renderProcessEnd() {
	}

}
