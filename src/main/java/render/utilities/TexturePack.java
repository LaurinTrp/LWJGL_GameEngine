package main.java.render.utilities;

import java.io.File;

import static org.lwjgl.opengl.GL20.*;

import resources.ResourceLoader;
import main.java.utils.loaders.ImageLoader;

public class TexturePack {
	private int blendMap, background, rTexture, gTexture, bTexture;

	public TexturePack() {
	}

	public TexturePack(String blendMap, String background, String rTexture, String gTexture, String bTexture) {
		this.blendMap = ImageLoader.loadTextureFromResource(blendMap);
		this.background = ImageLoader.loadTextureFromResource(background);
		this.rTexture = ImageLoader.loadTextureFromResource(rTexture);
		this.gTexture = ImageLoader.loadTextureFromResource(gTexture);
		this.bTexture = ImageLoader.loadTextureFromResource(bTexture);
	}

	public int getBlendMap() {
		return blendMap;
	}

	public int getBackground() {
		return background;
	}

	public int getrTexture() {
		return rTexture;
	}

	public int getgTexture() {
		return gTexture;
	}

	public int getbTexture() {
		return bTexture;
	}

	public void dispose() {
		glDeleteTextures(blendMap);
		glDeleteTextures(rTexture);
		glDeleteTextures(gTexture);
		glDeleteTextures(bTexture);
		glDeleteTextures(background);
	}

}
