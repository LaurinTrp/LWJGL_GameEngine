package main.java.render.model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import glm.mat._4.Mat4;
import glm.vec._4.Vec4;
import main.java.render.Renderer;
import main.java.render.renderobject.RenderObject;
import main.java.render.utilities.BoundingBox;
import main.java.render.utilities.NormalDrawing;
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;

public class SingleModel extends RenderObject {

	private Material material;

	protected boolean init = false;
	protected boolean hasEbo = false;

	protected int vao, vbo, ebo;
	private int triangles;

	protected ShaderProgram program;

	protected Mat4 modelMatrix;

	protected HashMap<String, Integer> uniforms = new HashMap<>();

	protected Float[] vertices;
	protected Float[] uvs;
	protected Float[] normals;
	protected Float[] colors;

	protected int[] indices;

	// min x, max x, min y, max y, min z, max z
//	protected final Float[] startMinmax;
//	protected Float[] minmax = new Float[6];

	private float scale = 1.0f;

	private String shaderFolder;

	private NormalDrawing<SingleModel> normalDrawing;
	private BoundingBox<SingleModel> boundingBox;

	private boolean showNormals;
	private boolean showMinMax;

	private Vec4 translation;

	/**
	 * Model constructor without ebo
	 *
	 * @param vertices  The vertices data (v0x, v0y, v0z, v0w, v1x, ...)
	 * @param uvs       The data of the uv coordinates
	 * @param normals   The data of the normals
	 * @param indices   The indices
	 * @param triangles Number of triangles
	 * @param material  Material object
	 * @param minmax    Min and Max coordinates (minX, maxX, minY, maxY, minZ, maxZ)
	 */
	public SingleModel(Float[] vertices, Float[] uvs, Float[] normals, int triangles, Material material, BoundingBox<SingleModel> minmax) {
		this.triangles = triangles;
		this.vertices = vertices;
		this.uvs = uvs;
		this.normals = normals;
		this.material = material;
		this.boundingBox = minmax;
	}
	/**
	 * Constructor copying other model
	 *
	 * @param model Model to copy
	 */
	public SingleModel(SingleModel model) {
		this(model.vertices, model.uvs, model.normals, model.triangles, model.material, model.boundingBox);

		this.program = model.program;

		this.modelMatrix = model.modelMatrix;

		this.uniforms = model.uniforms;
	}

	@Override
	public void init() {

		initShader(shaderFolder);
		initMatrixes();
		bindModel();

		afterInit();

		normalDrawing = new NormalDrawing<>(this);
		boundingBox.setModelMatrix(modelMatrix);

		init = true;
	}

	/**
	 * Method that can be called after the initialization Needs override
	 */
	protected void afterInit() {
	}

	/**
	 * Initializing the matrices
	 */
	public void initMatrixes() {
		modelMatrix = new Mat4(1.0f);
	}

	/**
	 * Initializing the shader and the uniform variables
	 *
	 * @param shaderFolder The name of the shader folder in the resources
	 */
	protected void initShader(String shaderFolder) {

		program = new ShaderProgram(shaderFolder);
		ModelUtils.createUniform(program, uniforms, "modelMatrix");
		ModelUtils.createUniform(program, uniforms, "viewMatrix");
		ModelUtils.createUniform(program, uniforms, "projectionMatrix");
		ModelUtils.createUniform(program, uniforms, "cameraPos");

		ModelUtils.createUniform(program, uniforms, "lightsources");
		ModelUtils.createUniform(program, uniforms, "numOfLights");

		ModelUtils.createUniform(program, uniforms, "sunPosition");
		ModelUtils.createUniform(program, uniforms, "sunColor");

	}

	/**
	 * Binding the model and the data
	 */
	private void bindModel() {

		colors = new Float[vertices.length];

		for (int i = 0; i < colors.length; i += 4) {
			colors[i] = 1.0f;
			colors[i + 1] = 0.0f;
			colors[i + 2] = 0.0f;
			colors[i + 3] = 1.0f;
		}
		
		float[] data = ModelUtils.flattenArrays(vertices, colors, uvs, normals);

		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		// create VAO
		if (!hasEbo) {

			glBindVertexArray(vao);
			{
				// upload VBO
				glBindBuffer(GL_ARRAY_BUFFER, vbo);
				glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_READ);

				setAttributePointers();
			}
			glBindVertexArray(0);
		} else {
			ebo = glGenBuffers();

			glBindVertexArray(vao);
			{
				// upload VBO
				glBindBuffer(GL_ARRAY_BUFFER, vbo);
				glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_READ);

				glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
				glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

				setAttributePointers();

			}
			glBindVertexArray(0);

		}
	}

	/**
	 * Set the attribute pointers for the render data
	 */
	private void setAttributePointers() {
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 4, GL_FLOAT, false, 16 * 4, 0 * 4);

		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 16 * 4, 4 * 4);

		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 4, GL_FLOAT, false, 16 * 4, 8 * 4);

		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 4, GL_FLOAT, false, 16 * 4, 12 * 4);
	}

	/**
	 * Needs override to run functions in the render process
	 */
	protected void renderProcessBegin() {
	}

	/**
	 * Needs override to run functions in the render process
	 */
	protected void renderProcessEnd() {
	}

	/**
	 * upload lighting specific values to the shader
	 */
	protected void uploadLighting() {
//		glUniform4fv(uniforms.get("ambientColor"), Renderer.ambientColor.toFA_());

		ArrayList<Vec4> lights = Renderer.lightSourcePositions;
		float[] lightsources = new float[lights.size() * 4];
		for (int i = 0; i < lights.size(); i++) {
			lightsources[i * 4 + 0] = lights.get(i).x;
			lightsources[i * 4 + 1] = lights.get(i).y;
			lightsources[i * 4 + 2] = lights.get(i).z;
			lightsources[i * 4 + 3] = lights.get(i).w;
		}
		glUniform4fv(uniforms.get("lightsources"), lightsources);
		glUniform1i(uniforms.get("numOfLights"), lights.size());

		glUniform4fv(uniforms.get("sunPosition"), Renderer.sun.getLightPosition().toFA_());
		glUniform4fv(uniforms.get("sunColor"), Renderer.sun.getColor().toFA_());

	}

	/**
	 * Upload the different matrices to the shader
	 */
	protected void uploadMatrixes() {
		glUniformMatrix4fv(uniforms.get("viewMatrix"), false, Renderer.camera.getView().toFa_());
		glUniformMatrix4fv(uniforms.get("projectionMatrix"), false, Renderer.camera.getProjectionMatrix().toFa_());
		glUniformMatrix4fv(uniforms.get("modelMatrix"), false, modelMatrix.toFa_());
		glUniform4fv(uniforms.get("cameraPos"), new Vec4(Renderer.camera.getCameraPosition(), 1.0f).toFA_());
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
				if (material != null) {
					glActiveTexture(GL_TEXTURE0 + 0);
					glBindTexture(GL_TEXTURE_2D, material.getTexture());
				}
				glBindVertexArray(vao);
				{
//					updateMinmax();
					renderProcessBegin();

					// Upload the uniforms
					uploadLighting();
					uploadMatrixes();

					// draw the data (depends on if it has a ebo)
					if (!hasEbo) {
						glDrawArrays(GL_TRIANGLES, 0, triangles);
					} else {
						glDrawElements(GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
					}

					renderProcessEnd();

				}
				glBindVertexArray(0);
			}
			glUseProgram(0);
		}
		// draw normals if necessary
		if (showNormals) {
			normalDrawing.render();
		}
		if(showMinMax) {
			boundingBox.render();
		}
	}


	/**
	 * Get the material object
	 *
	 * @return material object
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * Set the material object
	 *
	 * @param material material for the model
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	/**
	 * Getter for the vertices data
	 *
	 * @return The vertices as float array
	 */
	public Float[] getVertices() {
		return vertices;
	}

	/**
	 * Getter for the normals data
	 *
	 * @return The normals as float array
	 */
	public Float[] getNormals() {
		return normals;
	}

	/**
	 * Getter for the uvs data
	 *
	 * @return The uvs as float array
	 */
	public Float[] getUvs() {
		return uvs;
	}

	/**
	 * Set the shader folder
	 *
	 * @param shaderFolder new Shader folder
	 */
	public void setShaderFolder(String shaderFolder) {
		this.shaderFolder = shaderFolder;
	}

	/**
	 * Get the shader program
	 *
	 * @return current shader program
	 */
	public ShaderProgram getProgram() {
		return program;
	}

	/**
	 * set the option to show the normals
	 *
	 * @param showNormals boolean if normals should be shown
	 */
	public void setShowNormals(boolean showNormals) {
		this.showNormals = showNormals;
	}
	
	public void setShowMinMax(boolean showMinMax) {
		this.showMinMax = showMinMax;
	}

	/**
	 * get the current scale of the model
	 *
	 * @return scale as float
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * get the model matrix
	 *
	 * @return model matrix as mat4
	 */
	public Mat4 getModelMatrix() {
		return modelMatrix;
	}

	/**
	 * Set a new model matrix
	 *
	 * @param modelMatrix new Model matrix
	 */
	public void setModelMatrix(Mat4 modelMatrix) {
		this.modelMatrix = modelMatrix;
	}


	/**
	 * set the scale of the model
	 *
	 * @param scale scaling factor
	 */
	public void scale(float scale) {
		modelMatrix.scale(scale);
	}

	public BoundingBox<SingleModel> getBoundingBox() {
		return boundingBox;
	}
	
	@Override
	public void dispose() {

		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);

		if (program != null) {
			program.dispose();
		}
		if (material != null) {
			material.dispose();
		}

		vao = 0;

		if (normalDrawing != null) {
			normalDrawing.dispose();
		}
		if(boundingBox != null) {
			boundingBox.dispose();
		}

		init = false;
	}

}
