package main.java.render.passes;

import org.lwjgl.opengl.GL20;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.render.model.Material;
import main.java.render.model.MultiTextureTerrain;
import main.java.render.utilities.TerrainGenerator;
import main.java.render.utilities.TexturePack;
import main.java.utils.ModelUtils;
import main.java.utils.math.MathFunctions;

public class TerrainModel extends MultiTextureTerrain {
	
	private TerrainGenerator generator;
	
	public TerrainModel(TerrainGenerator generator, TexturePack texturePack) {
		super(generator.getVerticesBuffer(), generator.getUvsBuffer(), generator.getNormalsBuffer(), 
				generator.getIndicesBuffer(), generator.getIndicesBuffer().length, new Material(), generator.getMinmax());
		
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
		super.renderProcessBegin();
		GL20.glUniform1f(uniforms.get("size"), generator.getSize());
	}
	
	public boolean isOnTerrain(Vec2 position) {
		float xPositionOnTerrain = position.x - generator.getStartX();
		float zPositionOnTerrain = position.y - generator.getStartZ();
		return xPositionOnTerrain >= 0.0 && zPositionOnTerrain >= 0.0 && xPositionOnTerrain <= generator.getSize()
				&& zPositionOnTerrain <= generator.getSize();
	}

	public float heightAtPosition(Vec2 position) {
		if (isOnTerrain(position)) {
			float xPositionOnTerrain = position.x - generator.getStartX();
			int xGridPosition = (int) Math.floor(xPositionOnTerrain / generator.getDensity());
			float zPositionOnTerrain = position.y - generator.getStartZ();
			int zGridPosition = (int) Math.floor(zPositionOnTerrain / generator.getDensity());

			float xCoord = (xPositionOnTerrain % generator.getDensity()) / generator.getDensity();
			float zCoord = (zPositionOnTerrain % generator.getDensity()) / generator.getDensity();
			float currentTerrainHeight = 0;
			if (xCoord <= (1 - zCoord)) {
				currentTerrainHeight = MathFunctions.barryCentric(
						new Vec3(0, generator.getVerticesList().get(zGridPosition).get(xGridPosition).y, 0),
						new Vec3(1, generator.getVerticesList().get(zGridPosition + 1).get(xGridPosition).y, 0),
						new Vec3(0, generator.getVerticesList().get(zGridPosition).get(xGridPosition + 1).y, 1),
						new Vec2(zCoord, xCoord));
			} else {
				currentTerrainHeight = MathFunctions.barryCentric(
						new Vec3(1, generator.getVerticesList().get(zGridPosition + 1).get(xGridPosition).y, 0),
						new Vec3(1, generator.getVerticesList().get(zGridPosition + 1).get(xGridPosition + 1).y, 1),
						new Vec3(0, generator.getVerticesList().get(zGridPosition).get(xGridPosition + 1).y, 1),
						new Vec2(zCoord, xCoord));
			}
			return currentTerrainHeight;
		}
		return -20f;
	}
	
}
