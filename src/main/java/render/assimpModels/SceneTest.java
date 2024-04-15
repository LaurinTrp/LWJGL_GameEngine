package main.java.render.assimpModels;

import org.lwjgl.glfw.GLFW;

import glm.vec._3.Vec3;
import main.java.data.Material;
import main.java.gui.Engine_Main;
import main.java.model.AssimpModel;
import main.java.utils.loaders.AssimpModelLoader;
import main.java.utils.loaders.ImageLoader;

public class SceneTest extends AssimpModel {

	public SceneTest() {
		super(AssimpModelLoader.loadStaticFromResource("old_kitchen", "scene_models.stl"));
		this.material = new Material(ImageLoader.loadTextureFromResource("old_kitchen", "colorAtlas.png"));
	}

	@Override
	protected void renderProcessBegin() {
	}
	
	@Override
	protected void renderProcessEnd() {
	}

	@Override
	public void afterInit() {
		modelMatrix = modelMatrix.translate(new Vec3(10f, 1f, 10f));
		modelMatrix = modelMatrix.scale(0.5f);
	}
}
