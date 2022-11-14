package main.java.render.passes;

import java.util.ArrayList;

import main.java.render.model.Model;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Cubes {
	ArrayList<Model> models = new ArrayList<>();
	
	public Cubes() {
		models = ModelLoader.loadMultipleModelsFromObj("/media/laurin/Laurin Festplatte/Blender/Models/Cubes.obj");
		for (Model model : models) {
			model.setShaderFolder("Transformation");
			model.getMaterial().setTexture(ImageLoader.loadTextureFromResource("Warn.png"));
		}
	}
	
	public void render() {
		for (Model model : models) {
			model.render();
		}
	}
	
	public void dispose() {
		for (Model model : models) {
			model.dispose();
		}
	}
}
