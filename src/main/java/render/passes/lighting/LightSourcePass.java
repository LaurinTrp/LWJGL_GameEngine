package main.java.render.passes.lighting;


import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.ArrayList;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.render.Renderer;
import main.java.render.renderobject.RenderObject;
import main.java.shader.ShaderProgram;
import main.java.utils.Shapes;

public class LightSourcePass extends RenderObject {

	private boolean init = false;

	private ArrayList<Vec4> lightsourcePositions = new ArrayList<>();

	private int vao = 0, vbo = 0, tex = 0;
	private int modelID;
    private int viewID;
    private int projID;
    private int lightPosID;


	private ShaderProgram program;

	private ArrayList<Mat4> modelMatrices = new ArrayList<>();

	@Override
	public void init() {
		initVAOs();
		initShader();
		initMatrixes();
		init = true;
	}

	private void initVAOs() {

		// create vertex array
		float[] vertices = Shapes.Cube.buffer;

		// create VAO
		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_READ);

			// define Vertex Attributes
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 12 * 4, 0 * 4);

			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 12 * 4, 4 * 4);

			glEnableVertexAttribArray(2);
			glVertexAttribPointer(2, 4, GL_FLOAT, false, 12 * 4, 8 * 4);
		}
		glBindVertexArray(0);
	}

	private void initMatrixes() {
		for(int i = 0; i < lightsourcePositions.size(); i++) {
			modelMatrices.add(new Mat4(1.0f));
			modelMatrices.get(i).translate(new Vec3(lightsourcePositions.get(i).x, lightsourcePositions.get(i).y, lightsourcePositions.get(i).z));
			modelMatrices.get(i).scale(0.2f);
		}
	}

	private void initShader() {

		program = new ShaderProgram("LightSource");
		modelID = glGetUniformLocation(program.getProgramID(), "modelMatrix");
		viewID = glGetUniformLocation(program.getProgramID(), "viewMatrix");
		projID = glGetUniformLocation(program.getProgramID(), "projectionMatrix");
		lightPosID = glGetUniformLocation(program.getProgramID(), "lightPos");

	}


	@Override
	public void render() {

		if (!init) {
			init();
		}

		if (!init) {
			return;
		}
		glDisable(GL_CULL_FACE);
		{
			glUseProgram(program.getProgramID());
			{
				glBindTexture(GL_TEXTURE_2D, tex);
				glBindVertexArray(vao);
				{

					glUniformMatrix4fv(viewID, false, Renderer.camera.getView().toFa_());
					glUniformMatrix4fv(projID, false, Renderer.camera.getProjectionMatrix().toFa_());

					for (int i = 0; i < modelMatrices.size(); i++) {
						glUniformMatrix4fv(modelID, false, modelMatrices.get(i).toFa_());
						glUniform4fv(lightPosID, lightsourcePositions.get(i).toFA_());

						glDrawArrays(GL_TRIANGLES, 0, Shapes.Cube.triangleCount);
					}

				}
				glBindVertexArray(0);
			}
		}
		glEnable(GL_CULL_FACE);
	}

	public void setLightsourcePositions(ArrayList<Vec4> lightsourcePositions) {
		this.lightsourcePositions = lightsourcePositions;
	}

	public ArrayList<Mat4> getModelMatrices() {
		return modelMatrices;
	}

	@Override
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
