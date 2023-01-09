package main.java.render.passes;

import java.util.ArrayList;

import main.java.render.IRenderObject;
import main.java.render.model.Model;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Cubes implements IRenderObject {
	ArrayList<Model> models = new ArrayList<>();
	
	public Cubes() {
	}

	@Override
	public void init() {
		models = ModelLoader.loadMultipleModelsFromObj("/media/laurin/Laurin Festplatte/Blender/Models/Cubes.obj");
		for (Model model : models) {
			model.setShaderFolder("Transformation");
			model.getMaterial().setTexture(ImageLoader.loadTextureFromResource("Warn.png"));
		}
	}
	
	@Override
	public void render() {
		for (Model model : models) {
			model.render();
		}
	}

	@Override
	public void dispose() {
		for (Model model : models) {
			model.dispose();
		}
	}

}
