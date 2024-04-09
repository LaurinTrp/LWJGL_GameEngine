package main.java.render.assimpModels.trees;

import glm.mat._4.Mat4;
import main.java.data.Material;
import main.java.model.AssimpModel;
import main.java.utils.loaders.AssimpModelLoader;
import main.java.utils.loaders.ImageLoader;

public class Tree_2 extends AssimpModel {

	public Tree_2(Mat4[] matrices) {
		super(AssimpModelLoader.loadStaticFromResource("Trees", "Tree2.obj"));
		this.material = new Material(ImageLoader.loadTextureFromResource("trees", "Tree2.png"));
	}

	@Override
	public void afterInit() {
	}

	@Override
	protected void renderProcessBegin() {
	}

	@Override
	protected void renderProcessEnd() {
	}

}
