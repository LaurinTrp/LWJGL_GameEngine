package main.java.render.passes.skybox;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.HashMap;

import glm.mat._4.Mat4;
import main.java.render.IRenderObject;
import main.java.render.Renderer;
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;
import main.java.utils.loaders.ImageLoader;

public class Skybox implements IRenderObject {

	private int vao, vbo, textureID;
	private int texture;
	private boolean init = false;

	private ShaderProgram program;

	private HashMap<String, Integer> uniforms = new HashMap<>();

	public Skybox(String[] faces) {
		try {
			textureID = ImageLoader.loadSkybox(faces);
			
			texture = ImageLoader.loadTextureFromResource("Warn.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		bindVAO();
		initShader();

		init = true;
	}

	private void bindVAO() {
		// @formatter:off
		float skyboxVertices[] = {
			    // positions          
			    -1.0f,  1.0f, -1.0f,
			    -1.0f, -1.0f, -1.0f,
			     1.0f, -1.0f, -1.0f,
			     1.0f, -1.0f, -1.0f,
			     1.0f,  1.0f, -1.0f,
			    -1.0f,  1.0f, -1.0f,

			    -1.0f, -1.0f,  1.0f,
			    -1.0f, -1.0f, -1.0f,
			    -1.0f,  1.0f, -1.0f,
			    -1.0f,  1.0f, -1.0f,
			    -1.0f,  1.0f,  1.0f,
			    -1.0f, -1.0f,  1.0f,

			     1.0f, -1.0f, -1.0f,
			     1.0f, -1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
			     1.0f,  1.0f, -1.0f,
			     1.0f, -1.0f, -1.0f,

			    -1.0f, -1.0f,  1.0f,
			    -1.0f,  1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
			     1.0f, -1.0f,  1.0f,
			    -1.0f, -1.0f,  1.0f,

			    -1.0f,  1.0f, -1.0f,
			     1.0f,  1.0f, -1.0f,
			     1.0f,  1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
			    -1.0f,  1.0f,  1.0f,
			    -1.0f,  1.0f, -1.0f,

			    -1.0f, -1.0f, -1.0f,
			    -1.0f, -1.0f,  1.0f,
			     1.0f, -1.0f, -1.0f,
			     1.0f, -1.0f, -1.0f,
			    -1.0f, -1.0f,  1.0f,
			     1.0f, -1.0f,  1.0f
			};
		// @formatter:on

		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, skyboxVertices, GL_DYNAMIC_READ);

			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 3 * 4, 0 * 4);
		}
		glBindVertexArray(0);
	}

	private void initShader() {
		program = new ShaderProgram("skybox");

		ModelUtils.createUniform(program, uniforms, "proj");
		ModelUtils.createUniform(program, uniforms, "view");
	}

	@Override
	public void render() {
		if (!init) {
			init();
		}
		if (!init) {
			return;
		}
		glDepthMask(false);
		glUseProgram(program.getProgramID());
		{
			glUniformMatrix4fv(uniforms.get("proj"), false, Renderer.camera.getProjectionMatrix().toFa_());

			Mat4 view = new Mat4(Renderer.camera.getView().toMat3_());

			glUniformMatrix4fv(uniforms.get("view"), false, view.toFa_());
			{
				glBindVertexArray(vao);
				glActiveTexture(GL_TEXTURE0+0);
				glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);
				{
					glDrawArrays(GL_TRIANGLES, 0, 36);
				}
			}
		}

		glDepthMask(true);
	}

	@Override
	public void dispose() {
		if (init) {
			glDeleteTextures(textureID);
			glDeleteBuffers(vao);
			glDeleteBuffers(vbo);
			program.dispose();
		}
	}
}
