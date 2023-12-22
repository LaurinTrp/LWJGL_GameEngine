package main.java.render.passes.lighting;

import glm.mat._4.Mat4;
import main.java.render.model.Model;
import main.java.utils.loaders.ModelLoader;

public class Lightsources extends Model {

	public Lightsources(Mat4[] modelMatrices) {
		super(ModelLoader.loadModelFromResource("Cube", "cube.obj", modelMatrices), modelMatrices);
		setShaderFolder("LightSource");
	}

}