package main.java.render.passes.trees;

import java.util.Arrays;

import glm.vec._3.Vec3;
import main.java.render.model.Model;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Tree_1 extends Model {

	public Tree_1() {
		super((Model) ModelLoader.loadModel("/media/laurin/Laurin Festplatte/Blender/Models/tree_1.obj"));
		setShaderFolder("Transformation");
		try {
			getMaterial().setTexture(ImageLoader
					.loadTextureFromMemory("/media/laurin/Laurin Festplatte/Blender/Models/tree_X12_+X1_Rock_Pack/_1_tree.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void afterInit() {
		super.afterInit();
		modelMatrix.translate(new Vec3(10.0f, 0f, 0f));
		updateMinmax();
	}

}
