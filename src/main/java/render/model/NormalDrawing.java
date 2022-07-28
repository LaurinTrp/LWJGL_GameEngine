package main.java.render.model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
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

import glm.glm.mat._4.Mat4;
import main.java.render.Renderer;
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;

public class NormalDrawing {

	float[] vertices;
	float[] normals;

	private HashMap<String, Integer> uniforms = new HashMap<String, Integer>();

	float[] data;
	ShaderProgram program;
	int vao = 0, vbo = 0;

	private Mat4 modelMatrix = new Mat4(1.0f);

	public NormalDrawing(Model model) {
		this.vertices = model.getVertices();
		this.normals = model.getNormals();

		data = ModelUtils.createNormals(vertices, normals);

		this.modelMatrix = model.modelMatrix;

		bind();
		initShader();
	}
	
	public NormalDrawing(float[] vertices, float[] normals, Mat4 modelMatrix) {
		this.vertices = vertices;
		this.normals = normals;
		
		data = ModelUtils.createNormals(vertices, normals);

		this.modelMatrix = modelMatrix;

		bind();
		initShader();
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

	protected void uploadMatrixes() {
		glUniformMatrix4fv(uniforms.get("viewMatrix"), false, Renderer.camera.getView().toFa_());
		glUniformMatrix4fv(uniforms.get("projectionMatrix"), false, Renderer.camera.getProjectionMatrix().toFa_());
		glUniformMatrix4fv(uniforms.get("modelMatrix"), false, modelMatrix.toFa_());
	}

	public void render() {
		{
			glUseProgram(program.getProgramID());
			{
				glBindVertexArray(vao);
				{
					uploadMatrixes();

					glDrawArrays(GL_LINES, 0, data.length / 4);

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

	}
}
