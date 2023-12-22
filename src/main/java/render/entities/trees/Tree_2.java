package main.java.render.entities.trees;

import glm.mat._4.Mat4;
import main.java.render.model.Model;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Tree_2 extends Model {

	public Tree_2(Mat4[] matrices) {
		super(ModelLoader.loadModelFromResource("Trees", "Tree2.obj", matrices), matrices);
//		setModelMatrices(matrices);
		
		setShaderFolder("Transformation");
		try {
			getMaterial().setTexture(ImageLoader
					.loadTextureFromResource("trees/Tree2.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setShowMinMax(true);
	}

	@Override
	public void afterInit() {
		super.afterInit();
	}

	@Override
	protected void renderProcessBegin() {
	}

	@Override
	protected void renderProcessEnd() {
	}

}
