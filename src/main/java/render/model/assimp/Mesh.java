package main.java.render.model.assimp;

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

import java.util.Arrays;

import glm.mat._4.Mat4;
import jassimp.AiMesh;
import main.java.render.Renderer;

public class Mesh {

	public int vao, vboPositions, vboTexCoords, vboNormals, ebo;
	public int elements;

	private Mat4 modelMatrix;

	private final AiMesh mesh;
	
	private float[] vertices;
	private float[] texCoords;
	private float[] normals;
	private int[] indices;
	
	private float[] minmax = new float[6];
	
	private static int idCounter;
	private final int id;
	
	private boolean selected;
	
	public Mesh(AiMesh mesh) {
		id=Mesh.idCounter;
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
		
		this.modelMatrix = new Mat4(1.0f);
		this.modelMatrix.translate(10, 3, 10);
	}
	
	private void loadData() {
		vertices = new float[mesh.getNumVertices() * 3];
		texCoords = new float[mesh.getNumVertices() * 2];
		normals = new float[mesh.getNumVertices() * 3];
		indices = new int[mesh.getNumFaces() * 3];
		
		elements = indices.length;
	
		int counter = 0;
	    for (int v = 0; v < mesh.getNumVertices(); v++) {
	        vertices[counter++] = mesh.getPositionX(v);
	        vertices[counter++] = mesh.getPositionY(v);
	        vertices[counter++] = mesh.getPositionZ(v);
	        
	        minmax[0] = Math.min(minmax[0], mesh.getPositionX(v));
	        minmax[1] = Math.max(minmax[1], mesh.getPositionX(v));
	        minmax[2] = Math.min(minmax[2], mesh.getPositionY(v));
	        minmax[3] = Math.max(minmax[3], mesh.getPositionY(v));
	        minmax[4] = Math.min(minmax[4], mesh.getPositionZ(v));
	        minmax[5] = Math.max(minmax[5], mesh.getPositionZ(v));
	    }
	    counter = 0;
	    for (int t = 0; t < mesh.getNumVertices(); t++) {
	        texCoords[counter++] = mesh.getTexCoordU(t, 0);
	        texCoords[counter++] = mesh.getTexCoordV(t, 0);
	    }
	    counter = 0;
	    for (int n = 0; n < mesh.getNumVertices(); n++) {
	        normals[counter++] = mesh.getNormalX(n);
	        normals[counter++] = mesh.getNormalY(n);
	        normals[counter++] = mesh.getNormalZ(n);
	    }

	    counter = 0;
	    for (int f = 0; f < mesh.getNumFaces(); f++) {
	        indices[counter++] = mesh.getFaceVertex(f, 0);
	        indices[counter++] = mesh.getFaceVertex(f, 1);
	        indices[counter++] = mesh.getFaceVertex(f, 2);
	    }
	}

	private void bindData() {
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

	public int getId() {
		return id;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public float[] getMinmax() {
		return minmax;
	}
	
}
