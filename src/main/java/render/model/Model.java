package main.java.render.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1f;
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
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;

public class Model {

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

	protected int[] indices;

	// min x, max x, min y, max y, min z, max z
	protected Float[] startMinmax = new Float[6];
	protected Float[] minmax = new Float[6];

	private float scale = 1.0f;

	private String shaderFolder;

	private NormalDrawing normalDrawing;

	private boolean showNormals;

	private Vec4 translation;

	public Model() {
	}
	
	/**
	 * Model constructor with ebo
	 * @param vertices The vertices data (v0x, v0y, v0z, v0w, v1x, ...)
	 * @param uvs The data of the uv coordinates
	 * @param normals The data of the normals
	 * @param indices The indices
	 * @param triangles Number of triangles
	 * @param material Material object
	 * @param minmax Min and Max coordinates (minX, maxX, minY, maxY, minZ, maxZ)
	 */
	public Model(Float[] vertices, Float[] uvs, Float[] normals, int[] indices, int triangles, Material material, Float[] minmax) {
		this(vertices, uvs, normals, triangles, material, minmax);
		hasEbo = true;
		this.indices = indices;
	}

	/**
	 * Model constructor without ebo
	 * @param vertices The vertices data (v0x, v0y, v0z, v0w, v1x, ...)
	 * @param uvs The data of the uv coordinates
	 * @param normals The data of the normals
	 * @param indices The indices
	 * @param triangles Number of triangles
	 * @param material Material object
	 * @param minmax Min and Max coordinates (minX, maxX, minY, maxY, minZ, maxZ)
	 */
	public Model(Float[] vertices, Float[] uvs, Float[] normals, int triangles, Material material, Float[] minmax) {
		this.triangles = triangles;
		this.vertices = vertices;
		this.uvs = uvs;
		this.normals = normals;
		this.material = material;
		this.startMinmax = minmax;
		this.minmax = startMinmax;
	}

	/**
	 * Constructor copying other model
	 * @param model Model to copy
	 */
	public Model(Model model) {
		this(model.vertices, model.uvs, model.normals, model.triangles, model.material, model.minmax);

		this.program = model.program;

		this.modelMatrix = model.modelMatrix;

		this.uniforms = model.uniforms;
	}

	/**
	 * Initializing the model
	 */
	public void init() {
		
		initShader(shaderFolder);
		initMatrixes();
		bindModel();

		updateMinmax();

		afterInit();

		normalDrawing = new NormalDrawing(this);

		init = true;
	}

	/**
	 * Method that can be called after the initialization
	 * Needs override
	 */
	protected void afterInit() {
	}

	/**
	 * Initializing the matrices
	 */
	private void initMatrixes() {
		modelMatrix = new Mat4(1.0f);
	}

	/**
	 * Initializing the shader and the uniform variables
	 * @param shaderFolder The name of the shader folder in the resources
	 */
	protected void initShader(String shaderFolder) {

		program = new ShaderProgram(shaderFolder);
		ModelUtils.createUniform(program, uniforms, "modelMatrix");
		ModelUtils.createUniform(program, uniforms, "viewMatrix");
		ModelUtils.createUniform(program, uniforms, "projectionMatrix");
		ModelUtils.createUniform(program, uniforms, "cameraPos");

		ModelUtils.createUniform(program, uniforms, "ambientColor");

		ModelUtils.createUniform(program, uniforms, "material.ambient");
		ModelUtils.createUniform(program, uniforms, "material.diffuse");
		ModelUtils.createUniform(program, uniforms, "material.specular");
		ModelUtils.createUniform(program, uniforms, "material.reflectance");
		ModelUtils.createUniform(program, uniforms, "material.hasTexture");

		ModelUtils.createUniform(program, uniforms, "lightsources");
		ModelUtils.createUniform(program, uniforms, "numOfLights");

		ModelUtils.createUniform(program, uniforms, "sunPosition");
		ModelUtils.createUniform(program, uniforms, "sunColor");
		
		ModelUtils.createUniform(program, uniforms, "texDiffuse");
		ModelUtils.createUniform(program, uniforms, "texReflectance");
		

	}

	/**
	 * Binding the model and the data
	 */
	private void bindModel() {

		Float[] colors = new Float[vertices.length];

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
	protected void renderProcess() {
	}

	/**
	 * upload the material to the shader
	 */
	protected void uploadMaterial() {
		glUniform1f(uniforms.get("material.ambient"), material.getAmbient());
		glUniform1f(uniforms.get("material.diffuse"), material.getAmbient());
		glUniform1f(uniforms.get("material.specular"), material.getAmbient());
		glUniform1f(uniforms.get("material.reflectance"), material.getReflectance());
		glUniform1i(uniforms.get("material.hasTexture"), material.hasTexture() ? 1 : 0);
		
		glUniform1i(uniforms.get("texDiffuse"), 0);
		glUniform1i(uniforms.get("texReflectance"), 1);
	}

	/**
	 * upload lighting specific values to the shader
	 */
	protected void uploadLighting() {
		glUniform4fv(uniforms.get("ambientColor"), Renderer.ambientColor.toFA_());

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
	
	/**
	 * update the minmax for example after scaling
	 */
	protected void updateMinmax() {
		translation = new Vec4(modelMatrix.mul(new Vec4(0.0f, 0.0f, 0.0f, 1.0f)));
		minmax[0] = (startMinmax[0] * scale) + translation.x;
		minmax[1] = (startMinmax[1] * scale) + translation.x;
		minmax[2] = (startMinmax[2] * scale) + translation.y;
		minmax[3] = (startMinmax[3] * scale) + translation.y;
		minmax[4] = (startMinmax[4] * scale) + translation.z;
		minmax[5] = (startMinmax[5] * scale) + translation.z;
	}

	/**
	 * Render method to render the model
	 */
	public void render() {
		if (!init) {
			init();
		}
		if(!init){
			return;
		}
		
		{
			glUseProgram(program.getProgramID());
			{
				if (material != null) {
					glBindTexture(GL_TEXTURE_2D, material.getTexture());
					glActiveTexture(GL_TEXTURE0 + 1);
					glBindTexture(GL_TEXTURE_2D, material.getReflectionTexture());
				}
				glBindVertexArray(vao);
				{
//					updateMinmax();
					renderProcess();

					// Upload the uniforms
					uploadMaterial();
					uploadLighting();
					uploadMatrixes();

					// draw the data (depends on if it has a ebo)
					if (!hasEbo) {
						glDrawArrays(GL_TRIANGLES, 0, triangles);
					} else {
						glDrawElements(GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
					}

				}
				glBindVertexArray(0);
			}
			glUseProgram(0);
		}
		// draw normals if necessary
		if (showNormals) {
			normalDrawing.render();
		}
	}

	/**
	 * Get the material object
	 * @return material object
	 */
	public Material getMaterial() {
		return material;
	}
	
	/**
	 * Set the material object
	 * @param material material for the model
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	/**
	 * Getter for the vertices data
	 * @return The vertices as float array
	 */
	public Float[] getVertices() {
		return vertices;
	}

	/**
	 * Getter for the normals data
	 * @return The normals as float array
	 */
	public Float[] getNormals() {
		return normals;
	}

	/**
	 * Getter for the uvs data
	 * @return The uvs as float array
	 */
	public Float[] getUvs() {
		return uvs;
	}

	/**
	 * Set the shader folder
	 * @param shaderFolder new Shader folder
	 */
	public void setShaderFolder(String shaderFolder) {
		this.shaderFolder = shaderFolder;
	}

	/**
	 * Get the shader program
	 * @return current shader program
	 */
	public ShaderProgram getProgram() {
		return program;
	}

	/**
	 * set the option to show the normals
	 * @param showNormals boolean if normals should be shown
	 */
	public void setShowNormals(boolean showNormals) {
		this.showNormals = showNormals;
	}

	/**
	 * get the current scale of the model
	 * @return scale as float
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * get the model matrix
	 * @return model matrix as mat4
	 */
	public Mat4 getModelMatrix() {
		return modelMatrix;
	}

	/**
	 * Set a new model matrix
	 * @param modelMatrix new Model matrix
	 */
	public void setModelMatrix(Mat4 modelMatrix) {
		this.modelMatrix = modelMatrix;
	}

	/**
	 * get the minmax values
	 * @return minmax values as an float array
	 */
	public Float[] getMinmax() {
		return minmax;
	}
	
	/**
	 * set the scale of the model
	 * @param scale scaling factor
	 */
	public void setScale(float scale) {
		for (int i = 0; i < minmax.length; i++) {
			minmax[i] *= scale;
		}
		this.scale = scale;
		modelMatrix.scale(scale);
	}

	/**
	 * Clear the data
	 */
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

		init = false;
	}

}
