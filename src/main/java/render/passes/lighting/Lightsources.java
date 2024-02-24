package main.java.render.passes.lighting;

import glm.mat._4.Mat4;
import main.java.render.model.Model;

public class Lightsources extends Model {

	public Lightsources(Mat4[] modelMatrices) {
		super("Cube", "cube.obj", modelMatrices);
		setShaderFolder("LightSource");
	}

}
