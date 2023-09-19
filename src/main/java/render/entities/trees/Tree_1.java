package main.java.render.entities.trees;

import glm.mat._4.Mat4;
import main.java.render.Renderer;
import main.java.render.model.MultiModel;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Tree_1 extends MultiModel {

	public Tree_1(Mat4[] matrices) {
		super((MultiModel) ModelLoader.loadMultiModelFromResource("Trees", "Tree1.obj"));
		setModelMatrices(matrices);
		
		setShaderFolder("Transformation");
		try {
			getMaterial().setTexture(ImageLoader
					.loadTextureFromResource("trees/Tree1.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setShowMinMax(true);
	}
	
	@Override
	protected void renderProcessBegin() {
	}
	
	@Override
	protected void renderProcessEnd() {
	}

	@Override
	public void afterInit() {
		super.afterInit();
	}

}
