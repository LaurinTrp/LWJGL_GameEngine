package main.java.render.utilities.terrain;

import java.util.ArrayList;

import glm.Glm;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.utils.ModelUtils;
import main.java.utils.math.SimplexNoise;

public class TerrainGenerator {

	private Float[] verticesBuffer;
	private Float[] uvsBuffer;
	private Float[] normalsBuffer;

	private int[] indicesBuffer;

	private ArrayList<ArrayList<Vec4>> verticesList = new ArrayList<>();
	private ArrayList<Vec4> normalsList = new ArrayList<>();

	private float size, density, startX, startZ;

	private Float[] minmax;

	private Vec3[] edgePoints = new Vec3[4];

	private boolean isReady = false;

	public TerrainGenerator(float size, float density, float startX, float startZ) {
		this.size = size;
		this.density = density;
		this.startX = startX;
		this.startZ = startZ;

		minmax = new Float[] {
				startX, startX + size,
				0f, 0f,
				startZ, startZ+size
		};

		edgePoints[0] = new Vec3(startX, 0, startZ);
		edgePoints[1] = new Vec3(startX + size, 0, startZ);
		edgePoints[2] = new Vec3(startX, 0, startZ + size);
		edgePoints[3] = new Vec3(startX + size, 0, startZ + size);

	}

	public void generate() {
		new Thread(() -> {
			generateMesh();
			isReady = true;
		}).start();
	}

	private void generateMesh() {
		double noise = 0;
		ArrayList<Vec4> uvs = new ArrayList<>();

		for (int y = 0; y <= size / density; y++) {

			ArrayList<Vec4> vertexRow = new ArrayList<>();

			for (int x = 0; x <= size / density; x++) {
				double worldX = startX + x * density;
				double worldZ = startZ + y * density;

				noise = SimplexNoise.noise(worldX / 5f, worldZ / 5f);

				vertexRow.add(new Vec4(worldX, noise, worldZ, 1.0f));

				uvs.add(new Vec4(x / (size/density), y / (size/density), 0.0f, 1.0f));
			}
			verticesList.add(vertexRow);
		}
		for (int i = 0; i < verticesList.size(); i++) {
			for (int j = 0; j < verticesList.get(i).size(); j++) {
				Triangle t = getTriangle(i, j);
				Vec4 normal = calculateNormal(t).toVec4_();
				normalsList.add(normal);
			}
		}

		verticesBuffer = ModelUtils.flattenListOfListsStream(verticesList);

		ArrayList<Integer> indices = new ArrayList<>();
		for (int i = 0; i < verticesList.size() - 1; i++) {
			for (int j = 0; j < verticesList.get(i).size() - 1; j++) {
				Triangle t1 = getTriangle(i, j, true);

				Triangle t2 = getTriangle(i, j, false);

				indices.add(t1.point1);
				indices.add(t1.point3);
				indices.add(t1.point2);
				indices.add(t2.point1);
				indices.add(t2.point3);
				indices.add(t2.point2);
			}
		}

		indicesBuffer = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++) {
			indicesBuffer[i] = indices.get(i);
		}

		this.uvsBuffer = new Float[uvs.size() * 4];
		for (int i = 0; i < uvs.size(); i++) {
			this.uvsBuffer[i * 4 + 0] = uvs.get(i).x;
			this.uvsBuffer[i * 4 + 1] = uvs.get(i).y;
			this.uvsBuffer[i * 4 + 2] = uvs.get(i).z;
			this.uvsBuffer[i * 4 + 3] = uvs.get(i).w;
		}

		this.normalsBuffer = new Float[normalsList.size() * 4];
		for (int i = 0; i < normalsList.size(); i++) {
			this.normalsBuffer[i * 4 + 0] = normalsList.get(i).x;
			this.normalsBuffer[i * 4 + 1] = normalsList.get(i).y;
			this.normalsBuffer[i * 4 + 2] = normalsList.get(i).z;
			this.normalsBuffer[i * 4 + 3] = normalsList.get(i).w;
		}
	}

	public Float[] getVerticesBuffer() {
		return verticesBuffer;
	}

	public ArrayList<ArrayList<Vec4>> getVerticesList() {
		return verticesList;
	}

	public Float[] getUvsBuffer() {
		return uvsBuffer;
	}

	public Float[] getNormalsBuffer() {
		return normalsBuffer;
	}

	public int[] getIndicesBuffer() {
		return indicesBuffer;
	}

	public Float[] getMinmax() {
		return minmax;
	}

	public float getSize() {
		return size;
	}

	public float getDensity() {
		return density;
	}
	public float getStartX() {
		return startX;
	}
	public float getStartZ() {
		return startZ;
	}

	public Vec3[] getEdgePoints() {
		return edgePoints;
	}

	public boolean isReady() {
		return isReady;
	}


	private Triangle getTriangle(int i, int j) {
		Triangle t = new Triangle();
		if (i < verticesList.size() - 1 && j < verticesList.get(0).size() - 1) {
			t.vertex1 = verticesList.get(i).get(j);
			t.vertex2 = verticesList.get(i + 1).get(j);
			t.vertex3 = verticesList.get(i).get(j + 1);
		} else if (i >= verticesList.size() - 1 && j < verticesList.get(0).size() - 1) {
			t.vertex1 = verticesList.get(i).get(j);
			t.vertex2 = verticesList.get(i - 1).get(j + 1);
			t.vertex3 = verticesList.get(i - 1).get(j);
		} else if (i < verticesList.size() - 1 && j >= verticesList.get(0).size() - 1) {
			t.vertex1 = verticesList.get(i).get(j);
			t.vertex2 = verticesList.get(i).get(j - 1);
			t.vertex3 = verticesList.get(i + 1).get(j - 1);
		} else if (i >= verticesList.size() - 1 && j >= verticesList.get(0).size() - 1) {
			t.vertex1 = verticesList.get(i).get(j);
			t.vertex2 = verticesList.get(i - 1).get(j);
			t.vertex3 = verticesList.get(i).get(j - 1);
		}
		return t;
	}

	private Triangle getTriangle(int i, int j, boolean upperLeft) {
		Triangle t = new Triangle();
		if (upperLeft) {
			t.point1 = i * verticesList.size() + j;
			t.point2 = i * verticesList.size() + j + 1;
			t.point3 = (i + 1) * verticesList.size() + j;

			t.vertex1 = verticesList.get(i).get(j);
			t.vertex2 = verticesList.get(i).get(j + 1);
			t.vertex3 = verticesList.get(i + 1).get(j);
		} else {
			t.point1 = (i + 1) * verticesList.size() + j;
			t.point2 = i * verticesList.size() + j + 1;
			t.point3 = (i + 1) * verticesList.size() + j + 1;

			t.vertex1 = verticesList.get(i + 1).get(j);
			t.vertex2 = verticesList.get(i).get(j + 1);
			t.vertex3 = verticesList.get(i + 1).get(j + 1);
		}

		return t;
	}

	private Vec3 calculateNormal(Triangle triangle) {
		Vec3 a = triangle.vertex1.toVec3_();
		Vec3 b = triangle.vertex2.toVec3_();
		Vec3 c = triangle.vertex3.toVec3_();

		Vec3 bToA = new Vec3(b).sub(a);
		Vec3 cToA = new Vec3(c).sub(a);

		Vec3 normal = Glm.cross_(bToA, cToA);
		if (normal.y < 0) {
			normal.y *= -1;
		}
		if (!(normal.x == 0 && normal.y == 0 && normal.z == 0)) {
			return normal.normalize();
		}
		return normal;
	}

	private class Triangle {
		int point1;
		int point2;
		int point3;

		Vec4 vertex1;
		Vec4 vertex2;
		Vec4 vertex3;

		public void print() {
			System.out.println("TRIANGLE: " + vertex1 + "\t" + vertex2 + "\t" + vertex3);
		}
	}


	public String id() {
		return getStartX() + "|" + getStartZ() + "|" + getSize() + "|" + getDensity();
	}
}