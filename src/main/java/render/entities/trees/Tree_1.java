package main.java.render.entities.trees;

import glm.mat._4.Mat4;
import main.java.render.model.MultiModel;
import main.java.render.model.SingleModel;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Tree_1 extends MultiModel {

	public Tree_1(Mat4[] matrices) {
		super((MultiModel) ModelLoader.loadMultiModelFromResource("Trees", "Tree1.obj"), matrices);
		setShaderFolder("Transformation");
		try {
			getMaterial().setTexture(ImageLoader
					.loadTextureFromResource("Tree1.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterInit() {
		super.afterInit();
	}

}
