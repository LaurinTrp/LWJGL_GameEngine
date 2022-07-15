package main.java.render.passes.texture;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import glm.glm.mat._4.Mat4;
import main.java.shader.ShaderProgram;
import main.java.utils.loaders.ImageLoader;

public class TexturePass {

	private boolean init = false;

	private int vao = 0, vbo = 0, tex = 0;
	private int offset;
	int modelID;
	int viewID;
	int projID;
	ByteBuffer texture = null;
	private ShaderProgram program;
	FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	private Mat4 model;

	private void initTextures() {

		try {
			tex = ImageLoader.loadTextureFromResource("Tex.png");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initVAOs() {

		// create vertex array
		float[] vertices = new float[] {
				// positions // colors // texture coords
				0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, // top right
				0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, // bottom right
				-0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // bottom left

				0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, // top right
				-0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, // top left
				-0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // bottom left
		};

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

	private void init() {
		initVAOs();
		initTextures();
		program = new ShaderProgram("Texture");

		// compile and upload shader
		init = true;
	}

	float offsetY = 0;

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
				glBindTexture(GL_TEXTURE_2D, tex);
				glBindVertexArray(vao);
				{
					glDrawArrays(GL_TRIANGLES, 0, 6);
				}
				glBindVertexArray(0);
			}
		}
	}

	public void dispose() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteTextures(tex);
		vao = 0;
		vbo = 0;
		tex = 0;

		if (program != null) {
			program.dispose();
		}
		init = false;
	}

}
