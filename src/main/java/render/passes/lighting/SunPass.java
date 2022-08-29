package main.java.render.passes.lighting;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDrawArrays;
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

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.system.MemoryUtil;

import glm.glm.mat._4.Mat4;
import glm.glm.vec._3.Vec3;
import glm.glm.vec._4.Vec4;
import main.java.render.Renderer;
import main.java.shader.ShaderProgram;
import main.java.utils.Shapes;

public class SunPass {

	private boolean init = false;

	private Vec4 lightPosition = new Vec4(0.0, 1.0, 0.0, 0.0);

	private int vao = 0, vbo = 0;
	private int modelID;
	private int viewID;
	private int projID;
	private int lightPosID;
	
	private float angle = 0.0f;

	private ShaderProgram program;

	private Mat4 modelMatrix;

	private void init() {
		initVAOs();
		initShader();
		initMatrixes();
		init = true;
		
//		rotateSun();
	}

	private void initVAOs() {

		// create vertex array
		float[] vertices = Shapes.Cube.buffer;

		// create VAO
		IntBuffer buffer = MemoryUtil.memAllocInt(1);
		glGenVertexArrays(buffer);
		vao = buffer.get(0);
		glGenBuffers(buffer);
		vbo = buffer.get(0);
		MemoryUtil.memFree(buffer);

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
		modelMatrix = new Mat4(1.0f);
		modelMatrix.translate(new Vec3(lightPosition.x, lightPosition.y, lightPosition.z));
	}

	private void initShader() {

		program = new ShaderProgram("LightSource");
		modelID = glGetUniformLocation(program.getProgramID(), "modelMatrix");
		viewID = glGetUniformLocation(program.getProgramID(), "viewMatrix");
		projID = glGetUniformLocation(program.getProgramID(), "projectionMatrix");
		lightPosID = glGetUniformLocation(program.getProgramID(), "lightPos");
	}

	private void rotateSun() {
		modelMatrix.translate(new Vec3(lightPosition).negate().toFa_());
		modelMatrix.rotateX(Math.toRadians(1));
		modelMatrix.translate(new Vec3(lightPosition).toFa_());
		angle += Math.toRadians(1);
		
		
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
				glBindVertexArray(vao);
				{

					glUniformMatrix4fv(viewID, false, Renderer.camera.getView().toFa_());
					glUniformMatrix4fv(projID, false, Renderer.camera.getProjectionMatrix().toFa_());

					glUniformMatrix4fv(modelID, false, modelMatrix.toFa_());
//					glUniform4fv(lightPosID, lightPosition.toFA_());

					glDrawArrays(GL_TRIANGLES, 0, Shapes.Cube.triangleCount);
					
					rotateSun();
				}
				glBindVertexArray(0);
			}
		}
	}

	public Vec4 getLightPosition() {
		return lightPosition;
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
