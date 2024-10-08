package main.java.render.utils;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glLineWidth;
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

import java.util.Arrays;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.render.Renderer;
import main.java.render.renderobject.IRenderObject;
import main.java.shader.ShaderProgram;

public class BoundingBox implements IRenderObject {

	private boolean init = false;

	private int vao = 0, vbo = 0;
	private int modelID;
	private int viewID;
	private int projID;

	private ShaderProgram program;

	private Mat4 modelMatrix;

	private final Vec3 minVectorStart, maxVectorStart;
	private Vec3 minVector, maxVector;

	public BoundingBox(Vec3 minVector, Vec3 maxVector) {
		this(minVector, maxVector, new Mat4(1.0f));
	}
	
	public BoundingBox(Vec3 minVector, Vec3 maxVector, Mat4 modelMatrix) {
		this.minVectorStart = new Vec3(minVector);
		this.maxVectorStart = new Vec3(maxVector);
		this.minVector = new Vec3(minVector);
		this.maxVector = new Vec3(maxVector);
		
		this.modelMatrix = modelMatrix;
	}

	@Override
	public void init() {
		initVAOs();
		initShader();
		initMatrixes();
		init = true;
	}

	private void initVAOs() {
		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		// @formatter:off
		float[] vertices = { minVector.x, minVector.y, minVector.z, 1.0f, maxVector.x, minVector.y, minVector.z, 1.0f,

				minVector.x, minVector.y, minVector.z, 1.0f, minVector.x, maxVector.y, minVector.z, 1.0f,

				minVector.x, minVector.y, minVector.z, 1.0f, minVector.x, minVector.y, maxVector.z, 1.0f,

				maxVector.x, minVector.y, minVector.z, 1.0f, maxVector.x, maxVector.y, minVector.z, 1.0f,

				maxVector.x, minVector.y, minVector.z, 1.0f, maxVector.x, minVector.y, maxVector.z, 1.0f,

				minVector.x, minVector.y, maxVector.z, 1.0f, minVector.x, maxVector.y, maxVector.z, 1.0f,

				minVector.x, minVector.y, maxVector.z, 1.0f, maxVector.x, minVector.y, maxVector.z, 1.0f,

				maxVector.x, minVector.y, maxVector.z, 1.0f, maxVector.x, maxVector.y, maxVector.z, 1.0f,

				minVector.x, maxVector.y, minVector.z, 1.0f, minVector.x, maxVector.y, maxVector.z, 1.0f,

				minVector.x, maxVector.y, minVector.z, 1.0f, maxVector.x, maxVector.y, minVector.z, 1.0f,

				minVector.x, maxVector.y, maxVector.z, 1.0f, maxVector.x, maxVector.y, maxVector.z, 1.0f,

				maxVector.x, maxVector.y, minVector.z, 1.0f, maxVector.x, maxVector.y, maxVector.z, 1.0f,

		};
		// @formatter:on
		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_READ);

			// define Vertex Attributes
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 4 * 4, 0 * 4);
		}
		glBindVertexArray(0);
	}

	private void initMatrixes() {
	}

	private void initShader() {

		program = new ShaderProgram("MinMax");
		modelID = glGetUniformLocation(program.getProgramID(), "modelMatrix");
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
//		glDisable(GL_DEPTH_TEST);
		{
			glLineWidth(5.0f);
			glUseProgram(program.getProgramID());
			{
				glBindVertexArray(vao);
				{
					glUniformMatrix4fv(viewID, false, Renderer.camera.getView().toFa_());
					glUniformMatrix4fv(projID, false, Renderer.camera.getProjectionMatrix().toFa_());

					glUniformMatrix4fv(modelID, false, modelMatrix.toFa_());

					Renderer.framebuffer.bindFbo();
					
					glDrawArrays(GL_LINES, 0, 32);
					
					Renderer.framebuffer.unbindFbo();

				}
				glBindVertexArray(0);
			}
			glUseProgram(0);
		}
//		glEnable(GL_DEPTH_TEST);
	}

	private Vec4[] transformedMinmax() {
		Vec4[] cornerPoints = {
				new Vec4(minVector.x, minVector.y, minVector.z, 1), 	// Left Bottom Front
				new Vec4(maxVector.x, maxVector.y, maxVector.z, 1), 	// Right Top Back
		};	
		for (Vec4 cornerPoint : cornerPoints) {
			cornerPoint = modelMatrix.mul(cornerPoint);
		}
		
		Vec4 min = new Vec4(Math.min(cornerPoints[0].x, cornerPoints[1].x), Math.min(cornerPoints[0].y, cornerPoints[1].y), Math.min(cornerPoints[0].z, cornerPoints[1].z), 1);
		Vec4 max = new Vec4(Math.max(cornerPoints[0].x, cornerPoints[1].x), Math.max(cornerPoints[0].y, cornerPoints[1].y), Math.max(cornerPoints[0].z, cornerPoints[1].z), 1);
		
		cornerPoints[0] = min;
		cornerPoints[1] = max;
		
		return cornerPoints;
	}
	
	public static boolean collision(BoundingBox minmax0, BoundingBox minmax1) {
		Vec4[] cornerPoints0 = minmax0.transformedMinmax();
		Vec4[] cornerPoints1 = minmax1.transformedMinmax();
		
		return (cornerPoints0[0].x <= cornerPoints1[1].x &&
				cornerPoints0[1].x >= cornerPoints1[0].x &&
				cornerPoints0[0].y <= cornerPoints1[1].y &&
				cornerPoints0[1].y >= cornerPoints1[0].y &&
				cornerPoints0[0].z <= cornerPoints1[1].z &&
				cornerPoints0[1].z >= cornerPoints1[0].z);
	}
	
	public void setModelMatrix(Mat4 modelMatrix) {
		this.modelMatrix = modelMatrix;
	}
	
	public Vec3 getMinVectorStart() {
		return minVectorStart;
	}
	
	public Vec3 getMaxVectorStart() {
		return maxVectorStart;
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
