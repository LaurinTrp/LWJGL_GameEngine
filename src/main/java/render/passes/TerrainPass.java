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
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import glm.glm.mat._4.Mat4;
import glm.glm.vec._4.Vec4;
import main.java.render.Renderer;
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.math.ImprovedNoise;

public class TerrainPass {

	private boolean init = false;

	private int vao = 0, vbo = 0, tex = 0, ebo = 0;
	private int modelID;
	private int viewID;
	private int projID;
	private int lightPosID;

	private ShaderProgram program;

	private Mat4 modelMatrix;

	private ArrayList<ArrayList<Vec4>> vertices = new ArrayList<>();
	private float[] verticesBuffer;
	private float[] uvs;
	int[] indicesArray;
	private final int width = 100, height = 100;
	private final float density = 0.5f;

	private void init() {
		generateMesh();
		initVAOs();
		initShader();
		initMatrixes();
		initTextures();
		init = true;
	}

	private class Triangle {
		int point1;
		int point2;
		int point3;
	}

	public void generateMesh() {
		vertices.clear();

		ArrayList<Vec4> uvs = new ArrayList<>();

		for (int i = 0; i <= height / density; i++) {
			ArrayList<Vec4> vertexRow = new ArrayList<>();
			vertexRow.add(new Vec4((width/2), 0, (height/2) - i * density, 1.0f));
			uvs.add(new Vec4(0%2, i%2, 0.0f, 1.0f));
			for (int j = 1; j <= width / density; j++) {
//				System.out.println(main.java.utils.Glm.ImprovedNoise.noise(j/10f, 10, i/10f));
				vertexRow.add(new Vec4((width/2) - j * density, ImprovedNoise.noise(j/10f, 1.0, i/10f), (height/2) - i * density, 1.0f));

				uvs.add(new Vec4(j%2, i%2, 0.0f, 1.0f));

			}
			vertices.add(vertexRow);
		}

		verticesBuffer = ModelUtils.flattenListOfListsStream(vertices);

		ArrayList<Integer> indices = new ArrayList<>();
		for (int i = 0; i < vertices.size() - 1; i++) {
			for (int j = 0; j < vertices.get(i).size() - 1; j++) {
				Triangle t = new Triangle();
				t.point1 = i * vertices.size() + j;
				t.point2 = i * vertices.size() + j + 1;
				t.point3 = (i + 1) * vertices.size() + j;

				Triangle t2 = new Triangle();
				t2.point1 = (i + 1) * vertices.size() + j;
				t2.point2 = i * vertices.size() + j + 1;
				t2.point3 = (i + 1) * vertices.size() + j + 1;

				indices.add(t.point1);
				indices.add(t.point2);
				indices.add(t.point3);
				indices.add(t2.point1);
				indices.add(t2.point2);
				indices.add(t2.point3);
			}
		}

		indicesArray = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}

		this.uvs = new float[uvs.size() * 4];
		for (int i = 0; i < uvs.size(); i++) {
			this.uvs[i * 4 + 0] = uvs.get(i).x;
			this.uvs[i * 4 + 1] = uvs.get(i).y;
			this.uvs[i * 4 + 2] = uvs.get(i).z;
			this.uvs[i * 4 + 3] = uvs.get(i).w;
		}

	}

	private void initTextures() {

		try {
			tex = ImageLoader.loadTextureFromResource("Terrain.png");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initVAOs() {

		float[] dataBuffer = ModelUtils.flattenArrays(verticesBuffer, null, uvs, null);

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
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 8 * 4, 0 * 4);

			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 8 * 4, 4 * 4);

//			glEnableVertexAttribArray(1);
//			glVertexAttribPointer(1, 4, GL_FLOAT, false, 12 * 4, 4 * 4);
//
//			glEnableVertexAttribArray(2);
//			glVertexAttribPointer(2, 4, GL_FLOAT, false, 12 * 4, 8 * 4);

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
		lightPosID = glGetUniformLocation(program.getProgramID(), "lightPos");
	}


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
				glBindTexture(GL_TEXTURE_2D, tex);
				glBindVertexArray(vao);
				{

					glUniformMatrix4fv(viewID, false, Renderer.camera.getView().toFa_());
					glUniformMatrix4fv(projID, false, Renderer.camera.getProjectionMatrix().toFa_());

//					lightPosition.set(Math.sin(Math.toRadians(x)), lightPosition.y, lightPosition.z, lightPosition.w);
//					glUniform1f(xID, Math.sin(Math.toRadians(x)));

					glUniformMatrix4fv(modelID, false, modelMatrix.toFa_());
//						glUniform4fv(lightPosID, lightsourcePositions.get(i).toFA_());

//						glDrawArrays(GL_TRIANGLES, 0, Shapes.Cube.triangleCount);	
//						GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
//						glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
					GL15.glDrawElements(GL_TRIANGLES, indicesArray.length, GL11.GL_UNSIGNED_INT, 0);
//						glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//						GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

				}
				glBindVertexArray(0);
			}
		}
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

		init = false;
	}

}
