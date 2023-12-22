package main.java.render.passes;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
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

import main.java.shader.ShaderProgram;

public class TrianglePass {

	int vao, vbo;

	private boolean init = false;

	private ShaderProgram shader;
	
	private void init() {
		initShader();
		bindVertices();
		
		init = true;
	}
	
	private void initShader() {
		shader = new ShaderProgram("Triangle");
	}

	private void bindVertices() {
		float vertices[] = { 
				-0.5f, -0.5f, 0.0f, 
				0.5f, -0.5f, 0.0f, 
				0.0f, 0.5f, 0.0f };

		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		
		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_READ);

			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0 * 4);
		}
		glBindVertexArray(0);
	}
	
	public void render() {
		if(!init) {
			init();
		}
	
		glUseProgram(shader.getProgramID());
		{
			glBindVertexArray(vao);
			glDrawArrays(GL_TRIANGLES, 0, 3);
		}
		glUseProgram(0);
	}

	public void dispose() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		if(shader != null) {
			shader.dispose();
		}
	}
	
}
