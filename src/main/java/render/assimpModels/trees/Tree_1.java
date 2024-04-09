package main.java.render.assimpModels.trees;

import glm.mat._4.Mat4;
import main.java.data.Material;
import main.java.model.AssimpModel;
import main.java.utils.loaders.AssimpModelLoader;
import main.java.utils.loaders.ImageLoader;

public class Tree_1 extends AssimpModel {

	public Tree_1(Mat4[] matrices) {
		super(AssimpModelLoader.loadStaticFromResource("Trees", "Tree1.obj"));

		modelMatrix = matrices[0];
		
		material = new Material(ImageLoader.loadTextureFromResource("trees", "Tree1.png"));
	}

	@Override
	protected void renderProcessBegin() {
	}

	@Override
	protected void renderProcessEnd() {
	}

	@Override
	public void afterInit() {
		scale(3f);
	}

}
