package main.java.render.debug;

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
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.awt.Color;
import java.io.File;

import glm.vec._3.Vec3;
import main.java.render.Renderer;
import main.java.render.renderobject.IRenderObject;
import main.java.shader.ShaderProgram;

public class DebugLine implements IRenderObject {

	private boolean init = false;

	private int vao = 0, vbo = 0;
	private int viewID;
	private int projID;

	private ShaderProgram program;

	private final Vec3 start, end, color;

	public DebugLine(Vec3 start, Vec3 end, Color color) {
		this.start = start;
		this.end = end;
		this.color = new Vec3(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
	}

	@Override
	public void init() {
		initVAOs();
		initShader();
		init = true;
	}

	private void initVAOs() {
		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		// @formatter:off
		float[] vertices = {
			start.x, start.y, start.z, 1.0f,
			color.x, color.y, color.z, 1.0f,
			end.x, end.y, end.z, 1.0f,
			color.x, color.y, color.z, 1.0f,
		};
		// @formatter:on

		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_READ);

			// define Vertex Attributes
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 8 * 4, 0 * 4);

			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 8 * 4, 4 * 4);
		}
		glBindVertexArray(0);
	}

	private void initShader() {

		program = new ShaderProgram("debug" + File.separator + "DebugLine");
		viewID = glGetUniformLocation(program.getProgramID(), "viewMatrix");
		projID = glGetUniformLocation(program.getProgramID(), "projectionMatrix");

	}

	@Override
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

					Renderer.framebuffer.bindFbo();
					
					glDrawArrays(GL_LINES, 0, 2);

					Renderer.framebuffer.unbindFbo();

				}
				glBindVertexArray(0);
			}
			glUseProgram(0);
		}
	}

	@Override
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
