package main.java.render.passes;

import main.java.render.model.SingleModel;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Cube extends SingleModel {

	public Cube() {
		super((SingleModel) ModelLoader.loadModel("/media/laurin/Laurin Festplatte/Blender/Models/Cube.obj"));
		setShaderFolder("Transformation");
		getMaterial().setTexture(ImageLoader.loadTextureFromResource("Terrain/Terrain.png"));
	}

	@Override
	protected void afterInit() {
		setScale(3);
	}

}
