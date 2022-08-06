package main.java.render.passes.trees;

import java.util.ArrayList;
import java.util.Random;

import glm.glm.vec._2.Vec2;
import glm.glm.vec._3.Vec3;
import main.java.render.Renderer;
import main.java.render.model.Model;
import main.java.render.model.ModelCluster;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Trees extends ModelCluster {

	public Trees() {
		super(ModelLoader.loadMultipleModelsFromObj(
				"/media/laurin/Laurin Festplatte/Blender/Models/tree_X12_+X1_Rock_Pack/tree_X14_+X1_Rock_Pack.obj"));
		for (Model m : models) {
			m.setShaderFolder("Transformation");
		}
		try {
			for (int i = 0; i < models.size() - 1; i++) {
				models.get(i).getMaterial()
						.setTexture(ImageLoader.loadTextureFromMemory(String.format(
								"/media/laurin/Laurin Festplatte/Blender/Models/tree_X12_+X1_Rock_Pack/_%d_tree.png",
								(i + 1))));
			}
			models.get(models.size() - 1).getMaterial().setTexture(ImageLoader.loadTextureFromMemory(
					"/media/laurin/Laurin Festplatte/Blender/Models/tree_X12_+X1_Rock_Pack/Rock_1_.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterInit() {
		super.afterInit();
		for (Model model : models) {
			Float[] minmax = model.getMinmax();
			float xOffset = (minmax[0] + minmax[1]) / 2f;
			float yOffset = (minmax[2] + minmax[3]) / 2f;
			float zOffset = (minmax[4] + minmax[5]) / 2f;
			Vec3 offset = new Vec3(xOffset, 0, zOffset);
			model.getModelMatrix().translate(offset.negate());

			Random random = new Random();
			float rX = random.nextInt(Renderer.terrain.getWidth()) + Renderer.terrain.getStartX();
			float rZ = random.nextInt(Renderer.terrain.getHeight()) + Renderer.terrain.getStartZ();
			model.getModelMatrix().translate(new Vec3(rX, Renderer.terrain.heightAtPosition(new Vec2(rX, rZ)), rZ));

		}
	}

	public ArrayList<Model> getModels() {
		return models;
	}

}
