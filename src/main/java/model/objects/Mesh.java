package main.java.model.objects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import main.java.model.AssimpModel;
import main.java.render.Renderer;
import main.java.utils.model.ModelUtils;

public class Mesh {

	private final AssimpModel model;

	public int vao, vboPositions, vboTexCoords, vboNormals, ebo;
	public int elements;

	private Mat4 modelMatrix;

	protected final AIMesh mesh;

	protected float[] vertices;
	protected float[] texCoords;
	protected float[] normals;
	protected int[] indices;
	protected Vec3[] triangles;

	private float[] minmax = new float[6];

	private static int idCounter;
	private final int id;

	private boolean selected;
	
	private String name;

	public Mesh(AssimpModel model, AIMesh mesh, Mat4 modelMatrix) {
		this.model = model;
		
		id = Mesh.idCounter;
		Mesh.idCounter++;
		Renderer.modelObserver.addObjectToSelectables(this);
		
		this.mesh = mesh;

		minmax[0] = Float.MAX_VALUE;
		minmax[1] = -Float.MAX_VALUE;
		minmax[2] = Float.MAX_VALUE;
		minmax[3] = -Float.MAX_VALUE;
		minmax[4] = Float.MAX_VALUE;
		minmax[5] = -Float.MAX_VALUE;

		loadData();
		bindData();

		this.modelMatrix = modelMatrix;
	}

	protected void loadData() {
		processVertices();
		processUVs();
		processNormals();
		processFaces();
		
		this.name = mesh.mName().dataString();

		elements = indices.length;
	}

	private void processVertices() {
		vertices = new float[mesh.mNumVertices() * 3];
		int counter = 0;
		AIVector3D.Buffer aiVertices = mesh.mVertices();
		for (int v = 0; v < mesh.mNumVertices(); v++) {
			AIVector3D aiVertex = aiVertices.get();
			vertices[counter++] = aiVertex.x();
			vertices[counter++] = aiVertex.y();
			vertices[counter++] = aiVertex.z();

			minmax[0] = Math.min(minmax[0], aiVertex.x());
			minmax[1] = Math.max(minmax[1], aiVertex.x());
			minmax[2] = Math.min(minmax[2], aiVertex.y());
			minmax[3] = Math.max(minmax[3], aiVertex.y());
			minmax[4] = Math.min(minmax[4], aiVertex.z());
			minmax[5] = Math.max(minmax[5], aiVertex.z());
		}
	}

	private void processUVs() {
		texCoords = new float[mesh.mNumVertices() * 2];
		int counter = 0;
		AIVector3D.Buffer aiUVs = mesh.mTextureCoords(0);
		for (int t = 0; t < mesh.mNumVertices(); t++) {
			AIVector3D aiUV = aiUVs.get();
			texCoords[counter++] = aiUV.x();
			texCoords[counter++] = aiUV.y();
		}
	}

	private void processNormals() {
		normals = new float[mesh.mNumVertices() * 3];
		int counter = 0;
		AIVector3D.Buffer aiNormals = mesh.mNormals();
		for (int n = 0; n < mesh.mNumVertices(); n++) {
			AIVector3D aiNormal = aiNormals.get();
			normals[counter++] = aiNormal.x();
			normals[counter++] = aiNormal.y();
			normals[counter++] = aiNormal.z();
		}
	}

	private void processFaces() {
		indices = new int[mesh.mNumFaces() * 3];
		int counter = 0;
		AIFace.Buffer aiFaces = mesh.mFaces();
		for (int f = 0; f < mesh.mNumFaces(); f++) {
			AIFace aiFace = aiFaces.get();
			IntBuffer buffer = aiFace.mIndices();
			while (buffer.remaining() > 0) {
				indices[counter++] = buffer.get();
			}
		}

		triangles = new Vec3[indices.length];
		for (int i = 0; i < indices.length; i++) {
			int vertexIndex = indices[i] * 3;
			triangles[i] = new Vec3();
			triangles[i].x = vertices[vertexIndex];
			triangles[i].y = vertices[vertexIndex + 1];
			triangles[i].z = vertices[vertexIndex + 2];
		}

	}

	protected void bindData() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		int[] vbos = new int[3];
		glGenBuffers(vbos);
		vboPositions = vbos[0];
		vboTexCoords = vbos[1];
		vboNormals = vbos[2];

		ebo = glGenBuffers();

		glBindBuffer(GL_ARRAY_BUFFER, vboPositions);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);

		glBindBuffer(GL_ARRAY_BUFFER, vboTexCoords);
		glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 2 * 4, 0);

		glBindBuffer(GL_ARRAY_BUFFER, vboNormals);
		glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 3 * 4, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

		glBindVertexArray(0);
	}
	
	public Mat4 getModelMatrix() {
		return modelMatrix;
	}

	public int getId() {
		return id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void clicked() {
		model.clicked(this);

		setSelected(!isSelected());
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public float[] getMinmax() {
		return minmax;
	}

	public String getName() {
		return name;
	}
}