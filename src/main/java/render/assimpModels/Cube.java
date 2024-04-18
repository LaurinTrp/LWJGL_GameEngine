package main.java.render.assimpModels;

import main.java.data.Material;
import main.java.model.AssimpModel;
import main.java.utils.loaders.AssimpModelLoader;
import main.java.utils.loaders.ImageLoader;

public class Cube extends AssimpModel {
	
	public Cube() {
		super(AssimpModelLoader.loadStaticFromResource("Cube", "cube.obj"));

		this.material = new Material(ImageLoader.loadTextureFromResource("", "Numbers.png"));
	}

	@Override
	protected void afterInit() {
		modelMatrix.translate(10, 0, 10);
//		scale(3);
	}

	@Override
	protected void renderProcessBegin() {
	}

	@Override
	protected void renderProcessEnd() {
	}

}
