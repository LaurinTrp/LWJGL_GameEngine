package main.java.render.passes;

import org.lwjgl.opengl.GL20;

import glm.vec._2.Vec2;
import main.java.render.model.MultiTextureTerrain;
import main.java.render.utils.TexturePack;
import main.java.render.utils.terrain.TerrainGenerator;
import main.java.utils.ModelUtils;

public class TerrainModel extends MultiTextureTerrain {

	private TerrainGenerator generator;

	public TerrainModel(TerrainGenerator generator, TexturePack texturePack) {
		super(generator);

		this.generator = generator;

		setShaderFolder("TerrainMultiTexture");

		setShowNormals(false);

		setTexturePack(texturePack);

	}

	@Override
	protected void afterInit() {
		super.afterInit();
		ModelUtils.createUniform(program, uniforms, "size");
	}

	@Override
	protected void renderProcessBegin() {
		GL20.glUniform1f(uniforms.get("size"), generator.getSize());
	}

	@Override
	protected void renderProcessEnd() {
	}

	public boolean isOnTerrain(Vec2 position) {
		float xPositionOnTerrain = position.x - generator.getStartX();
		float zPositionOnTerrain = position.y - generator.getStartZ();
		return xPositionOnTerrain >= 0.0 && zPositionOnTerrain >= 0.0 && xPositionOnTerrain <= generator.getSize()
				&& zPositionOnTerrain <= generator.getSize();
	}

	public TerrainGenerator getGenerator() {
		return generator;
	}

}
