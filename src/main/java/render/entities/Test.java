package main.java.render.entities;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import glm.mat._4.Mat4;
import glm.vec._4.Vec4;
import main.java.render.IRenderObject;
import main.java.render.Renderer;
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;

public class Test implements IRenderObject{

	private boolean init = false;
	private int vao, vbo;
	private Map<String, Integer> uniforms = new HashMap<>();
	private ShaderProgram program;
	
	private Mat4 modelMatrix = new Mat4(1.0f);
	
	public Test() {
	}

	@Override
	public void init() {
		initVao();
		initShader("Test");
		
		init = true;
	}
	
	private void initVao() {
		
		float[] data = {
				0.0f, 3.0f, 0.0f,	1.0f, 0.0f, 0.0f,
				2.0f, 3.0f, 0.0f,	0.0f, 1.0f, 0.0f,
				-2.0f, 3.0f, 0.0f,	0.0f, 0.0f, 1.0f,
		};
		
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		
		{

			glBindVertexArray(vao);
			{
				// upload VBO
				glBindBuffer(GL_ARRAY_BUFFER, vbo);
				glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_READ);

				glEnableVertexAttribArray(0);
				glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * 4, 0 * 4);

				glEnableVertexAttribArray(1);
				glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * 4, 3 * 4);
			}
			glBindVertexArray(0);
		}
	}
	
	private void initShader(String shaderFolder) {
		program = new ShaderProgram(shaderFolder);
		ModelUtils.createUniform(program, uniforms, "modelMatrix");
		ModelUtils.createUniform(program, uniforms, "viewMatrix");
		ModelUtils.createUniform(program, uniforms, "projectionMatrix");
		
		System.out.println(uniforms);
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

					glUniformMatrix4fv(uniforms.get("viewMatrix"), false, Renderer.camera.getView().toFa_());
					glUniformMatrix4fv(uniforms.get("projectionMatrix"), false, Renderer.camera.getProjectionMatrix().toFa_());
					glUniformMatrix4fv(uniforms.get("modelMatrix"), false, modelMatrix.toFa_());
					
					glPointSize(20);
					glDrawArrays(GL_POINTS, 0, 3);
					glPointSize(1);
				}
			}
			glBindVertexArray(0);
		}
		glUseProgram(0);
		
	}

	@Override
	public void dispose() {
		if(program != null) {
			program.dispose();
		}

		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		
		init = false;
		
		
	}

}
