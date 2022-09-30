package main.java.render.passes;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import glm.Glm;
import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.render.Renderer;
import main.java.render.model.NormalDrawing;
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.math.MathFunctions;
import main.java.utils.math.SimplexNoise;

public class TerrainPass {

	private boolean init = false;

	private int vao = 0, vbo = 0, tex = 0, ebo = 0;
	private int modelID;
	private int viewID;
	private int projID;
	private int lightSources;
	private int numOfLights;
	private int sunPosition;
	private int sunColor;
	
	private double minY = Float.MAX_VALUE, maxY = -Float.MAX_VALUE;

	private ShaderProgram program;

	private Mat4 modelMatrix;

	private ArrayList<ArrayList<Vec4>> vertices = new ArrayList<>();
	private ArrayList<Vec4> normalsList = new ArrayList<>();
	private Float[] verticesBuffer;
	private Float[] uvs;
	private Float[] normals;
	int[] indicesArray;
	private final int width = 100, height = 100;
	private final float density = 0.5f;
	
	private float startX, startZ;

	private NormalDrawing normalDrawing;
	
	private BufferedImage heightMap;

	private void init() {
		generateHeightMap();
		
		generateMesh();
		initVAOs();
		initShader();
		initMatrixes();
		initTextures();

		normalDrawing = new NormalDrawing(verticesBuffer, normals, modelMatrix);
		
		init = true;
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
	
	private void generateHeightMap() {
		int imageWidth = (int)(width/density)+1;
		int imageHeight = (int)(height/density)+1;
		
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_GRAY);
		double[] data = new double[imageWidth*imageHeight];
		for (int y = 0; y <= height / density; y++) {
		    int off = y * imageWidth;
			for (int x = 1; x <= width / density; x++) {
				data[off + x-1] = SimplexNoise.noise(y / 15f, x / 15f);
			}
		}
		
		double minValue = data[0], maxValue = data[0];
	    for (int i = 0; i < data.length; i++) {
	      minValue = Math.min(data[i], minValue);
	      maxValue = Math.max(data[i], maxValue);
	    }
		int[] pixelData = new int[data.length];
	    for (int i = 0; i < data.length; i++) {
	      pixelData[i] = (int) (255 * (data[i] - minValue) / (maxValue - minValue));
	    }
	    
	    image.getRaster().setPixels(0, 0, imageWidth, imageHeight, pixelData);
	    
	    heightMap = image;
	    try {
			ImageIO.write(image, "PNG", new File("imageOutputs/heightMap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateMesh() {
		vertices.clear();

		ArrayList<Vec4> uvs = new ArrayList<>();
		
		startX = -width/2;
		startZ = -height/2;

		for (int y = 0; y <= height / density; y++) {
			ArrayList<Vec4> vertexRow = new ArrayList<>();
			vertexRow.add(new Vec4((-width / 2), 0, (-height / 2) + y * density, 1.0f));

			uvs.add(new Vec4(0 % 2, y % 2, 0.0f, 1.0f));

			for (int x = 1; x <= width / density; x++) {
				
				int heightFromImage = heightMap.getRaster().getSample(x, y, 0);
				
				vertexRow.add(new Vec4((-width / 2) + x * density, heightFromImage / 150f, (-height / 2) + y * density, 1.0f));

				uvs.add(new Vec4(x % 2, y % 2, 0.0f, 1.0f));
			}
			vertices.add(vertexRow);
		}
		for (int i = 0; i < vertices.size(); i++) {
			for (int j = 0; j < vertices.get(i).size(); j++) {
				Triangle t = getTriangle(i, j);
				Vec4 normal = calculateNormal(t).toVec4_();
				normalsList.add(normal);
			}
		}
//		for (int i = normalsList.size(); i < vertices.size() * vertices.get(0).size(); i++) {
//			normalsList.add(new Vec4(1.0f));
//		}

		verticesBuffer = ModelUtils.flattenListOfListsStream(vertices);
		ArrayList<Vec4> normalsFromIndices = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();
		for (int i = 0; i < vertices.size() - 1; i++) {
			for (int j = 0; j < vertices.get(i).size() - 1; j++) {
				Triangle t1 = getTriangle(i, j, true);
				normalsFromIndices.add(calculateNormal(t1).toVec4_());

				Triangle t2 = getTriangle(i, j, false);
				normalsFromIndices.add(calculateNormal(t2).toVec4_());

				indices.add(t1.point1);
				indices.add(t1.point2);
				indices.add(t1.point3);
				indices.add(t2.point1);
				indices.add(t2.point2);
				indices.add(t2.point3);
			}
		}

//		System.out.println(normalsFromIndices.size());

		indicesArray = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}

		this.uvs = new Float[uvs.size() * 4];
		for (int i = 0; i < uvs.size(); i++) {
			this.uvs[i * 4 + 0] = uvs.get(i).x;
			this.uvs[i * 4 + 1] = uvs.get(i).y;
			this.uvs[i * 4 + 2] = uvs.get(i).z;
			this.uvs[i * 4 + 3] = uvs.get(i).w;
		}

		this.normals = new Float[normalsList.size() * 4];
		for (int i = 0; i < normalsList.size(); i++) {
			this.normals[i * 4 + 0] = normalsList.get(i).x;
			this.normals[i * 4 + 1] = normalsList.get(i).y;
			this.normals[i * 4 + 2] = normalsList.get(i).z;
			this.normals[i * 4 + 3] = normalsList.get(i).w;
		}

	}

	private Triangle getTriangle(int i, int j) {
		Triangle t = new Triangle();
		if (i < vertices.size() - 1 && j < vertices.get(0).size() - 1) {
			t.vertex1 = vertices.get(i).get(j);
			t.vertex2 = vertices.get(i + 1).get(j);
			t.vertex3 = vertices.get(i).get(j + 1);
		} else if (i >= vertices.size() - 1 && j < vertices.get(0).size() - 1) {
			t.vertex1 = vertices.get(i).get(j);
			t.vertex2 = vertices.get(i - 1).get(j + 1);
			t.vertex3 = vertices.get(i - 1).get(j);
		} else if (i < vertices.size() - 1 && j >= vertices.get(0).size() - 1) {
			t.vertex1 = vertices.get(i).get(j);
			t.vertex2 = vertices.get(i).get(j - 1);
			t.vertex3 = vertices.get(i + 1).get(j - 1);
		} else if (i >= vertices.size() - 1 && j >= vertices.get(0).size() - 1) {
			t.vertex1 = vertices.get(i).get(j);
			t.vertex2 = vertices.get(i - 1).get(j);
			t.vertex3 = vertices.get(i).get(j - 1);
		}
		return t;
	}

	private Triangle getTriangle(int i, int j, boolean upperLeft) {
		Triangle t = new Triangle();
		if (upperLeft) {
			t.point1 = i * vertices.size() + j;
			t.point2 = i * vertices.size() + j + 1;
			t.point3 = (i + 1) * vertices.size() + j;

			t.vertex1 = vertices.get(i).get(j);
			t.vertex2 = vertices.get(i).get(j + 1);
			t.vertex3 = vertices.get(i + 1).get(j);
		} else {
			t.point1 = (i + 1) * vertices.size() + j;
			t.point2 = i * vertices.size() + j + 1;
			t.point3 = (i + 1) * vertices.size() + j + 1;

			t.vertex1 = vertices.get(i + 1).get(j);
			t.vertex2 = vertices.get(i).get(j + 1);
			t.vertex3 = vertices.get(i + 1).get(j + 1);
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

	private void initTextures() {

		try {
			tex = ImageLoader.loadTextureFromResource("Terrain/Terrain.png");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initVAOs() {
		float[] dataBuffer = ModelUtils.flattenArrays(verticesBuffer, null, uvs, normals);

		// create VAO
		IntBuffer buffer = MemoryUtil.memAllocInt(1);
		glGenVertexArrays(buffer);
		vao = buffer.get(0);
		glGenBuffers(buffer);
		vbo = buffer.get(0);
		glGenVertexArrays(buffer);
		ebo = buffer.get(0);
		MemoryUtil.memFree(buffer);

		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_DYNAMIC_READ);

			glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
			glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesArray, GL15.GL_STATIC_DRAW);

			// define Vertex Attributes
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 12 * 4, 0 * 4);

			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 12 * 4, 4 * 4);

			glEnableVertexAttribArray(3);
			glVertexAttribPointer(3, 4, GL_FLOAT, false, 12 * 4, 8 * 4);

		}
		glBindVertexArray(0);

	}

	private void initMatrixes() {
		modelMatrix = new Mat4(1.0f);
	}

	private void initShader() {

		program = new ShaderProgram("Terrain");
		modelID = glGetUniformLocation(program.getProgramID(), "modelMatrix");
		viewID = glGetUniformLocation(program.getProgramID(), "viewMatrix");
		projID = glGetUniformLocation(program.getProgramID(), "projectionMatrix");
		
		lightSources = glGetUniformLocation(program.getProgramID(), "lightsources");
		numOfLights = glGetUniformLocation(program.getProgramID(), "numOfLights");
		
		sunPosition = glGetUniformLocation(program.getProgramID(), "sunPosition");
		sunColor = glGetUniformLocation(program.getProgramID(), "sunColor");
		
	}

	int angle = 0;

	public void render() {

		if (!init) {
			init();
		}

		if (!init) {
			return;
		}

		{
			glUseProgram(program.getProgramID());
			{
				glActiveTexture(GL_TEXTURE0 + 0);
				glBindTexture(GL_TEXTURE_2D, tex);
				glBindVertexArray(vao);
				{

					glUniformMatrix4fv(viewID, false, Renderer.camera.getView().toFa_());
					glUniformMatrix4fv(projID, false, Renderer.camera.getProjectionMatrix().toFa_());

					glUniformMatrix4fv(modelID, false, modelMatrix.toFa_());

					uploadLighting();

//						GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
					GL15.glDrawElements(GL_TRIANGLES, indicesArray.length, GL11.GL_UNSIGNED_INT, 0);
//						GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

				}
				glBindVertexArray(0);
			}
			glUseProgram(0);
//			normalDrawing.render();
		}
	}
	
	protected void uploadLighting() {

		ArrayList<Vec4> lights = Renderer.lightSourcePositions;
		float[] lightsources = new float[lights.size() * 4];
		for (int i = 0; i < lights.size(); i++) {
			lightsources[i * 4 + 0] = lights.get(i).x;
			lightsources[i * 4 + 1] = lights.get(i).y;
			lightsources[i * 4 + 2] = lights.get(i).z;
			lightsources[i * 4 + 3] = lights.get(i).w;
		}
		glUniform4fv(lightSources, lightsources);
		glUniform1i(numOfLights, lights.size());
		
		glUniform4fv(sunPosition, Renderer.sun.getLightPosition().toFA_());
		glUniform4fv(sunColor, Renderer.sun.getColor().toFA_());
	}
	
	public BufferedImage createHeightMap() {
		BufferedImage image = new BufferedImage(vertices.size(), vertices.get(0).size(), BufferedImage.TYPE_4BYTE_ABGR);
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				
				float height = MathFunctions.map(vertices.get(i).get(j).y, (float)minY, (float)maxY, 0.0f, 1.0f);
				Color color = new Color(height, height, height);
				image.setRGB(i, j, color.getRGB());
			}
		}
		return image;
	}
	public BufferedImage createNormalMap() {
		BufferedImage image = new BufferedImage(vertices.size(), vertices.get(0).size(), BufferedImage.TYPE_4BYTE_ABGR);
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				Vec4 normal = new Vec4(normalsList.get(i * image.getWidth() + j)).mul_(0.5f).add_(0.5f);
				Color color = new Color(normal.x, normal.y, normal.z);
				image.setRGB(i, j, color.getRGB());
			}
		}
		return image;
	}
	
	public float heightAtPosition(Vec2 position) {
		float xPositionOnTerrain = position.x - getStartX();
		int xGridPosition = (int) Math.floor(xPositionOnTerrain / getDensity());
		float zPositionOnTerrain = position.y - getStartZ();
		int zGridPosition = (int) Math.floor(zPositionOnTerrain / getDensity());
		
		float xCoord = (xPositionOnTerrain%getDensity()) / getDensity();
		float zCoord = (zPositionOnTerrain%getDensity()) / getDensity();
		float currentTerrainHeight = 0;
		if (xCoord <= (1-zCoord)) {
			currentTerrainHeight = MathFunctions.barryCentric(new Vec3(0, getVertices().get(zGridPosition).get(xGridPosition).y, 0),
					new Vec3(1, getVertices().get(zGridPosition + 1).get(xGridPosition).y, 0), 
					new Vec3(0, getVertices().get(zGridPosition).get(xGridPosition + 1).y, 1), new Vec2(zCoord, xCoord));
		} else {
			currentTerrainHeight = MathFunctions.barryCentric(new Vec3(1, getVertices().get(zGridPosition+1).get(xGridPosition).y, 0), 
					new Vec3(1, getVertices().get(zGridPosition + 1).get(xGridPosition + 1).y, 1), 
					new Vec3(0, getVertices().get(zGridPosition).get(xGridPosition + 1).y, 1), new Vec2(zCoord, xCoord));
		}
		return currentTerrainHeight;
	}
	
	public ArrayList<ArrayList<Vec4>> getVertices() {
		return vertices;
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
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

	public void dispose() {

		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteTextures(tex);

		if (program != null) {
			program.dispose();
		}

		vao = 0;
		vbo = 0;
		tex = 0;

		normalDrawing.dispose();

		init = false;
	}

}
