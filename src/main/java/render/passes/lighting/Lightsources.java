package main.java.render.passes.lighting;

import glm.mat._4.Mat4;
import main.java.model.AssimpModel;
import main.java.utils.loaders.AssimpModelLoader;

public class Lightsources extends AssimpModel {

	public Lightsources(Mat4[] modelMatrices) {
		super(AssimpModelLoader.loadStaticFromResource("Cube", "cube.obj"));
//		super("Cube", "cube.obj", modelMatrices);
//		setShaderFolder("LightSource");
	}

	@Override
	protected void afterInit() {
		
	}

	@Override
	protected void renderProcessBegin() {
		
	}

	@Override
	protected void renderProcessEnd() {
		
	}

}
