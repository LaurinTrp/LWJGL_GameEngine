package main.java.render.entities.trees;

import glm.mat._4.Mat4;
import main.java.render.model.Material;
import main.java.render.model.assimp.AssimpModel;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.StaticMeshesLoader;

public class Tree_1 extends AssimpModel {

	public Tree_1(Mat4[] matrices) {
		super(StaticMeshesLoader.loadFromResource("Trees", "Tree1.obj"));

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
