package main.java.render.passes;

import java.util.ArrayList;

import main.java.render.IRenderObject;
import main.java.render.model.Model;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class Cube extends Model {
	
	public Cube() {
		super((Model)ModelLoader.loadModel("/media/laurin/Laurin Festplatte/Blender/Models/Cube.obj"));
		setShaderFolder("Transformation");
		getMaterial().setTexture(ImageLoader.loadTextureFromResource("Terrain/Terrain.png"));
	}
	
	@Override
	protected void afterInit() {
		setScale(3);
	}

}
