package main.java.render.passes.trees;

import java.util.ArrayList;
import java.util.Iterator;

import glm.glm.vec._3.Vec3;
import main.java.render.model.Model;
import main.java.utils.ModelUtils;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Trees {

	private ArrayList<Model> models = new ArrayList<>();

	public Trees() {
		
		models = ModelLoader.loadMultipleModelsFromObj(
				"/media/laurin/Laurin Festplatte/Blender/Models/tree_X12_+X1_Rock_Pack/tree_X14_+X1_Rock_Pack.obj");
		for (Model m : models) {
			m.setShaderFolder("Transformation");
		}
		for (int i = 0; i < models.size()-1; i++) {
			try {
				models.get(i).getMaterial().setTexture(ImageLoader.loadTextureFromMemory(String.format(
						"/media/laurin/Laurin Festplatte/Blender/Models/tree_X12_+X1_Rock_Pack/_%d_tree.png", 
						(i+1))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Model> getModels() {
		return models;
	}
	
	public void render() {
		models.forEach(m -> m.render());
	}

	public void dispose() {
		models.forEach(m -> m.dispose());
	}

}
