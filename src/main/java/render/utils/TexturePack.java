package main.java.render.utilities;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

import main.java.utils.loaders.ImageLoader;

public class TexturePack {
	private int blendMap, background, rTexture, gTexture, bTexture;

	public static final TexturePack DEFAULT_TERRAIN = new TexturePack("Terrain/BlendMap.png", "Terrain/Grass.png",
			"Terrain/Rocks.png", "Terrain/Mushroom.png", "Terrain/Flowers.png");

	public TexturePack() {
	}

	public TexturePack(String blendMap, String background, String rTexture, String gTexture, String bTexture) {
		this();
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
		if (this != TexturePack.DEFAULT_TERRAIN) {
			glDeleteTextures(blendMap);
			glDeleteTextures(rTexture);
			glDeleteTextures(gTexture);
			glDeleteTextures(bTexture);
			glDeleteTextures(background);
		}
	}

}
