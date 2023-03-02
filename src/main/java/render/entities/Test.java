package main.java.render.entities;

import main.java.render.model.Model;
import main.java.utils.ModelUtils;
import main.java.utils.Shapes;
import main.java.utils.loaders.ImageLoader;

public class Test extends Model{

	public Test() {
		super(ModelUtils.createModel(Shapes.Cube.bufferNormals, 4));

		setShaderFolder("Test");
		getMaterial().setTexture(ImageLoader.loadTextureFromResource("Warn.png"));
	}

	@Override
	protected void afterInit() {
		super.afterInit();
		setScale(3.0f);
	}

}
