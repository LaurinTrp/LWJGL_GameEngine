package main.java.render.utilities.terrain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.gui.Engine_Main;
import main.java.render.Camera;
import main.java.render.IRenderObject;
import main.java.render.passes.TerrainModel;
import main.java.render.utilities.TexturePack;
import main.java.utils.math.MathFunctions;

public class ProceduralTerrain {
	
	private List<IRenderObject> terrainList;
	
	public ProceduralTerrain(List<IRenderObject> terrains) {
		this.terrainList = terrains;
	}
	
	public void update(Camera camera) {
		if(terrainList == null) {
			return;
		}
		removeTerrains(camera);
		addTerrains(camera);
	}
	
	private void removeTerrains(Camera camera) {
		Vec4 distances = new Vec4();
		for (Iterator iterator = terrainList.iterator(); iterator.hasNext();) {
			TerrainModel terrainModel = (TerrainModel) iterator.next();
			Vec3[] terrainEdges = terrainModel.getGenerator().getEdgePoints();
			distances.x = MathFunctions.VECTOR_MATH.distance(camera.getCameraPosition(), terrainEdges[0]);
			distances.y = MathFunctions.VECTOR_MATH.distance(camera.getCameraPosition(), terrainEdges[1]);
			distances.z = MathFunctions.VECTOR_MATH.distance(camera.getCameraPosition(), terrainEdges[2]);
			distances.w = MathFunctions.VECTOR_MATH.distance(camera.getCameraPosition(), terrainEdges[3]);
			
			if(distances.x > 50 && distances.y > 50 && distances.z > 50 && distances.w > 50) {
				terrainModel.dispose();
				iterator.remove();
			}
		}
	}
	private void addTerrains(Camera camera) {
		Vec4 distances = new Vec4();
		for (IRenderObject listObject : terrainList) {
			TerrainModel terrainModel = (TerrainModel) listObject;
			Vec3[] terrainEdges = terrainModel.getGenerator().getEdgePoints();
			distances.x = MathFunctions.VECTOR_MATH.distance(camera.getCameraPosition(), terrainEdges[0]);
			distances.y = MathFunctions.VECTOR_MATH.distance(camera.getCameraPosition(), terrainEdges[1]);
			distances.z = MathFunctions.VECTOR_MATH.distance(camera.getCameraPosition(), terrainEdges[2]);
			distances.w = MathFunctions.VECTOR_MATH.distance(camera.getCameraPosition(), terrainEdges[3]);
			
			if(distances.x <= 50 || distances.y <= 50 || distances.z <= 50 || distances.w <= 50) {
				createTerrainThreaded(new TerrainGenerator(64, 2, terrainEdges[0].x, terrainEdges[0].z));
			}
		}
	}
	
	private void createTerrainThreaded(TerrainGenerator generator) {
		new Thread(() -> {
			Engine_Main.makeContextCurrent();
			Engine_Main.createCapabilities();
			TexturePack tp = new TexturePack("Terrain/BlendMap.png", "Terrain/Grass.png", "Terrain/Rocks.png", "Terrain/Mushroom.png", "Terrain/Flowers.png");
			TerrainModel terrainModel = new TerrainModel(generator, tp);
			terrainList.add(terrainModel);
		}).start();
	}
}
