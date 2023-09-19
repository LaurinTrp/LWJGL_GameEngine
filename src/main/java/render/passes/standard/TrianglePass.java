package main.java.render.passes.standard;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
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
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import main.java.render.renderobject.RenderObjectSingle;
import main.java.shader.ShaderProgram;

public class TrianglePass extends RenderObjectSingle{

	private boolean init = false;

	private int vao = 0, vbo = 0;
	private int offset;
	int modelID;
	int viewID;
	int projID;
	private ShaderProgram program;
	FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	private void initVAOs() {

		// create vertex array
		float[] vertices = new float[] {
				// Bottom Left
				-0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,

				// Bottom Right
				0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,

				// Top
				0.0f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, };

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

	@Override
	public void init() {
		initVAOs();

		program = new ShaderProgram("Triangle");
		offset = glGetUniformLocation(program.getProgramID(), "offset");

		init = true;
	}

	float offsetY = 0;

	@Override
	public void render() {

		if (!init) {
			init();
		}

		if (!init) {
			return;
		}

		{
			glEnable(GL_DEPTH_TEST);
			glUseProgram(program.getProgramID());
			{
				glBindVertexArray(vao);
				{
					glDrawArrays(GL_TRIANGLES, 0, 3);
				}
				glBindVertexArray(0);
			}
		}
	}

	@Override
	public void dispose() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		vao = 0;
		vbo = 0;
		if (program != null) {
			program.dispose();
		}

		init = false;
	}

}
