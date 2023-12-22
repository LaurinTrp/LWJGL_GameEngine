package main.java.render.passes.lighting;

import glm.mat._4.Mat4;
import main.java.render.model.MultiModel;
import main.java.utils.loaders.ModelLoader;

public class Lightsources extends MultiModel {

	public Lightsources(Mat4[] modelMatrices) {
		super(ModelLoader.loadMultiModelFromResource("Cube", "cube.obj", modelMatrices), modelMatrices);
		setShaderFolder("LightSource");
	}

}
