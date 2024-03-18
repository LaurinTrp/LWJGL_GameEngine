package main.java.render.passes;

import main.java.render.model.Material;
import main.java.render.model.assimp.AssimpModel;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.StaticMeshesLoader;

public class Cube extends AssimpModel {
	
	public Cube() {
		super(StaticMeshesLoader.loadFromResource("Cube", "cube.obj"));

		this.material = new Material(ImageLoader.loadTextureFromResource("", "Numbers.png"));
	}

	@Override
	protected void afterInit() {
		modelMatrix.translate(10, 10, 0);
//		scale(3);
	}

	@Override
	protected void renderProcessBegin() {
	}

	@Override
	protected void renderProcessEnd() {
	}

}
