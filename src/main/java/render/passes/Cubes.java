package main.java.render.passes;

import java.util.ArrayList;

import main.java.render.model.SingleModel;
import main.java.render.renderobject.IRenderObject;
import main.java.render.renderobject.RenderObject;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Cubes extends RenderObject {
	ArrayList<IRenderObject> models = new ArrayList<>();

	public Cubes() {
	}

	@Override
	public void init() {
		models = ModelLoader.loadMultipleModelsFromObj("/media/laurin/Laurin Festplatte/Blender/Models/Cubes.obj");
		for (IRenderObject model : models) {
			((SingleModel)model).setShaderFolder("Transformation");
			((SingleModel)model).getMaterial().setTexture(ImageLoader.loadTextureFromResource("Warn.png"));
		}
	}

	@Override
	public void render() {
		for (IRenderObject model : models) {
			model.render();
		}
	}

	@Override
	public void dispose() {
		for (IRenderObject model : models) {
			model.dispose();
		}
	}

}
