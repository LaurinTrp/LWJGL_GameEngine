package main.java.render.assimpModels;

import org.lwjgl.assimp.AIScene;

import main.java.data.Material;
import main.java.model.AssimpModel;
import main.java.utils.loaders.AssimpModelLoader;
import main.java.utils.loaders.ImageLoader;

public class Forest extends AssimpModel{

	public Forest() {
		super(AssimpModelLoader.loadStaticFromResource("forest", "demo.dae"));

		this.material = new Material(ImageLoader.loadTextureFromResource("forest", "DiffuseColor_Texture.png"));
	}

	@Override
	protected void afterInit() {
//		modelMatrix.translate(10, 0, 0);
		scale(0.5f);
	}

	@Override
	protected void renderProcessBegin() {
		
	}

	@Override
	protected void renderProcessEnd() {
		
	}
}
