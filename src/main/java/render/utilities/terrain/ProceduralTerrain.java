package main.java.render.utilities.terrain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.render.Camera;
import main.java.render.IRenderObject;
import main.java.render.passes.TerrainModel;
import main.java.render.utilities.TexturePack;
import main.java.utils.math.MathFunctions;

public class ProceduralTerrain {

	private List<IRenderObject> terrainList;
	private List<IRenderObject> tempTerrainList;

	public static List<String> ids = new ArrayList<>();

	public boolean first = true;

	public ProceduralTerrain(List<IRenderObject> terrains) {
		this.terrainList = terrains;
	}

	public void update(Camera camera) {
		if (terrainList == null) {
			return;
		}
		if (tempTerrainList != null) {
			terrainList.addAll(tempTerrainList);
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

			if (distances.x > 50 && distances.y > 50 && distances.z > 50 && distances.w > 50) {
				terrainModel.dispose();
				iterator.remove();
				ids.remove(terrainModel.getGenerator().id());
				first = true;
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

			if (distances.x <= 70 || distances.y <= 70 || distances.z <= 70 || distances.w <= 70) {
				tempTerrainList = new ArrayList<>();
				createTerrain(new TerrainGenerator(64, 2, terrainEdges[0].x + 64, terrainEdges[0].z + 0));
//				createTerrain(new TerrainGenerator(64, 2, terrainEdges[0].x, terrainEdges[0].z + 64));
			}
		}
	}

	private void createTerrain(TerrainGenerator generator) {
		if (!ids.contains(generator.id())) {
			generator.generate();
			TerrainModel terrainModel = new TerrainModel(generator, TexturePack.DEFAULT_TERRAIN);
			tempTerrainList.add(terrainModel);
			ids.add(generator.id());
		}
	}
}
