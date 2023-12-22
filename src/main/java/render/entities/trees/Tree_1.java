package main.java.render.entities.trees;

import glm.mat._4.Mat4;
import main.java.render.model.Model;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Tree_1 extends Model {

	public Tree_1(Mat4[] matrices) {
		super(ModelLoader.loadModelFromResource("Trees", "Tree1.obj", matrices), matrices);
//		setModelMatrices();
		
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
