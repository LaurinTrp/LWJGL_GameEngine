package main.java.render.passes.lighting;

import java.util.Arrays;

import glm.mat._4.Mat4;
import main.java.data.Material;
import main.java.model.AssimpModel;
import main.java.render.Renderer;
import main.java.utils.loaders.AssimpModelLoader;
import main.java.utils.loaders.ImageLoader;

public class Lightsources extends AssimpModel {

	public Lightsources(Mat4[] modelMatrices) {
		super(AssimpModelLoader.loadStaticFromResource("Sun", "Sun.obj"), modelMatrices);

		for (Mat4 mat4 : modelMatrices) {
			mat4.print();
		}
		
		this.material = new Material(ImageLoader.loadTextureFromResource("Sun", "sun.png"));
	}

	@Override
	protected void afterInit() {
		scale(0.001f);
	}

	@Override
	protected void renderProcessBegin() {
	}

	@Override
	protected void renderProcessEnd() {
		
	}

}
