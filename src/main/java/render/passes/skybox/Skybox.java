package main.java.render.passes.skybox;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.glActiveTexture;
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
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.HashMap;

import glm.mat._4.Mat4;
import main.java.render.Renderer;
import main.java.render.renderobject.IRenderObject;
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;
import main.java.utils.loaders.ImageLoader;

public class Skybox implements IRenderObject {

	private int vao, vbo, textureID;
	private boolean init = false;

	private ShaderProgram program;

	private Mat4 modelMatrix;

	private HashMap<String, Integer> uniforms = new HashMap<>();

	public Skybox(String[] faces) {
		try {
			textureID = ImageLoader.loadSkybox(faces);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		bindVAO();
		initShader();

		modelMatrix = new Mat4();

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
		ModelUtils.createUniform(program, uniforms, "model");
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
		glDisable(GL_CULL_FACE);
		glUseProgram(program.getProgramID());
		Renderer.framebuffer.bindFbo();
		
		{
			glUniformMatrix4fv(uniforms.get("proj"), false, Renderer.camera.getProjectionMatrix().toFa_());

			Mat4 view = new Mat4(Renderer.camera.getView().toMat3_());

			glUniformMatrix4fv(uniforms.get("view"), false, view.toFa_());
			glUniformMatrix4fv(uniforms.get("model"), false, modelMatrix.toFa_());
			{

//				modelMatrix.rotateY(Math.toRadians(0.01f));

				glBindVertexArray(vao);
				glActiveTexture(GL_TEXTURE0+0);
				glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);
				{
					glDrawArrays(GL_TRIANGLES, 0, 36);
				}
			}
		}
		Renderer.framebuffer.unbindFbo();

		glDepthMask(true);
		glDisable(GL_CULL_FACE);
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
