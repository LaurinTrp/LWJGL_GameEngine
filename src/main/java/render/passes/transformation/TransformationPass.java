package main.java.render.passes.transformation;


import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;

import glm.mat._4.Mat4;
import glm.vec._4.Vec4;
import main.java.gui.Engine_Main;
import main.java.render.Renderer;
import main.java.shader.ShaderProgram;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ModelLoader;

public class TransformationPass {

	private boolean init = false;

	private int vao = 0, vbo = 0, tex = 0;
	private int offset;
	private int modelID;
    private int viewID;
    private int projID;
    private int triangles;
    
    private int lightPosition;
    
    private int cameraPositionID;
    
	private ShaderProgram program;
	private ByteBuffer texture;
	
	private Mat4 modelMatrix;

	private void init() {
		initVAOs();
		initShader();
		initMatrixes();
		initTextures();
		init = true;
	}
	
	private void initTextures() {
		try {
			tex = ImageLoader.loadTextureFromResource("Warn.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void initVAOs() {
		ModelLoader loader = new ModelLoader();
		loader.loadObj("/media/laurin/Laurin Festplatte/Blender/Models/Test.obj");
		triangles = loader.getTriangleCount();
//		vao = loader.bindObject();
	}
	
	private void initMatrixes() {
		modelMatrix = new Mat4(1.0f);
		modelMatrix.scale(1.5f);
	}

	int angleID;
	
	private void initShader() {

		program = new ShaderProgram("Transformation");
		modelID = glGetUniformLocation(program.getProgramID(), "modelMatrix");
		viewID = glGetUniformLocation(program.getProgramID(), "viewMatrix");
		projID = glGetUniformLocation(program.getProgramID(), "projectionMatrix");
		lightPosition = glGetUniformLocation(program.getProgramID(), "lightPos");
		
		angleID = glGetUniformLocation(program.getProgramID(), "angle");
		
		cameraPositionID = glGetUniformLocation(program.getProgramID(), "cameraPos");
		
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
				glBindTexture(GL_TEXTURE_2D, tex);
				glBindVertexArray(vao);
				{
					if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_RIGHT)) {
						modelMatrix.rotateY(Math.toRadians(1));
					}
					if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_LEFT)) {
						modelMatrix.rotateY(Math.toRadians(-1));
					}
					if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_UP)) {
						modelMatrix.rotateX(Math.toRadians(1));
					}
					if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_DOWN)) {
						modelMatrix.rotateX(Math.toRadians(-1));
					}
					glUniformMatrix4fv(viewID, false, Renderer.camera.getView().toFa_());
					glUniformMatrix4fv(projID, false, Renderer.camera.getProjectionMatrix().toFa_());
					
					glUniformMatrix4fv(modelID, false, modelMatrix.toFa_());
					
					glUniform4fv(cameraPositionID, Renderer.camera.getCameraPosition().toFA_());
					
					glDrawArrays(GL_TRIANGLES, 0, triangles);
					
				}
				glBindVertexArray(0);
			}
			glUseProgram(0);
		}
	}

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
