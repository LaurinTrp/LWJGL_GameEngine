package main.java.render.utils;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.HashMap;

import glm.mat._4.Mat4;
import main.java.render.Renderer;
import main.java.render.model.Model;
import main.java.render.model.MultiTextureTerrain;
import main.java.render.renderobject.IRenderObject;
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;

public class NormalDrawing {

	private Float[] vertices;
	private Float[] normals;

	private HashMap<String, Integer> uniforms = new HashMap<>();

	private float[] data;
	private ShaderProgram program;
	private int vao = 0, vbo = 0;

	private Mat4[] modelMatrices;

	private boolean init = false;

	public NormalDrawing(IRenderObject model) {
		if (model instanceof Model) {
			Model myModel = (Model) model;
			this.vertices = myModel.getVertices();
			this.normals = myModel.getNormals();

			data = ModelUtils.createNormals(vertices, normals);

			this.modelMatrices = myModel.getModelMatrices();
		}
//
		if (model instanceof MultiTextureTerrain) {
			MultiTextureTerrain terrain = (MultiTextureTerrain) model;
			this.vertices = terrain.getVertices();
			this.normals = terrain.getNormals();

			data = ModelUtils.createNormals(vertices, normals);

			this.modelMatrices = new Mat4[] {terrain.getModelMatrix()};
		}

		bind();
		initShader();
	}

	public NormalDrawing(Float[] vertices, Float[] normals, Mat4[] modelMatrix) {
		this.vertices = vertices;
		this.normals = normals;
		this.modelMatrices = modelMatrix;
	}

	private void init() {
		data = ModelUtils.createNormals(vertices, normals);

		bind();
		initShader();

		init = true;
	}

	private void bind() {

		vao = glGenVertexArrays();

		vbo = glGenBuffers();

		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_READ);

			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 4 * 4, 0 * 4);
		}
		glBindVertexArray(0);
	}

	public void initShader() {
		program = new ShaderProgram("Normals");

		ModelUtils.createUniform(program, uniforms, "modelMatrix");
		ModelUtils.createUniform(program, uniforms, "viewMatrix");
		ModelUtils.createUniform(program, uniforms, "projectionMatrix");
	}

	protected void uploadMatrixes(Mat4 matrix) {
		glUniformMatrix4fv(uniforms.get("viewMatrix"), false, Renderer.camera.getView().toFa_());
		glUniformMatrix4fv(uniforms.get("projectionMatrix"), false, Renderer.camera.getProjectionMatrix().toFa_());
		glUniformMatrix4fv(uniforms.get("modelMatrix"), false, matrix.toFa_());
	}

	public void render() {
		if (!init) {
			init();
		}
		glLineWidth(1);
		{
			glUseProgram(program.getProgramID());
			{
				glBindVertexArray(vao);
				{
					for (Mat4 matrix : modelMatrices) {
						uploadMatrixes(matrix);

						glDrawArrays(GL_LINES, 0, data.length / 4);
					}

				}
				glBindVertexArray(0);
			}
			glUseProgram(0);
		}
	}

	public void dispose() {

		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);

		if (program != null) {
			program.dispose();
		}

		vao = 0;
		vbo = 0;

		init = false;
	}
}
