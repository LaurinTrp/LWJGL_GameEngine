package main.java.render.model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	public Model(Float[] vertices, Float[] uvs, Float[] normals, int[] indices, int triangles, Material material, Float[] minmax) {
		this(vertices, uvs, normals, triangles, material, minmax);
		hasEbo = true;
		this.indices = indices;
	}
	
	public Model(Float[] vertices, Float[] uvs, Float[] normals, int triangles, Material material, Float[] minmax) {
		this.triangles = triangles;
		this.vertices = vertices;
		this.uvs = uvs;
		this.normals = normals;
		this.material = material;
		this.startMinmax = minmax;
		this.minmax = startMinmax;
	}

	public Model(Model model) {
		this(model.vertices, model.uvs, model.normals, model.triangles, model.material, model.minmax);

		this.program = model.program;

		this.modelMatrix = model.modelMatrix;

		this.uniforms = model.uniforms;
	}

	public void init() {
		
		initShader(shaderFolder);
		initMatrixes();
		bindModel();

		updateMinmax();

		afterInit();

		normalDrawing = new NormalDrawing(this);

		init = true;
	}

	public void afterInit() {
	}

	private void initMatrixes() {
		modelMatrix = new Mat4(1.0f);
	}

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

	}

	private void bindModel() {

		Float[] colors = new Float[vertices.length];

		for (int i = 0; i < colors.length; i += 4) {
			colors[i] = 1.0f;
			colors[i + 1] = 0.0f;
			colors[i + 2] = 0.0f;
			colors[i + 3] = 1.0f;
		}

		// create VAO
		if (!hasEbo) {
			float[] data = ModelUtils.flattenArrays(vertices, colors, uvs, normals);

			vao = glGenVertexArrays();

			vbo = glGenBuffers();

			glBindVertexArray(vao);
			{
				// upload VBO
				glBindBuffer(GL_ARRAY_BUFFER, vbo);
				glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_READ);
				glEnableVertexAttribArray(0);
				glVertexAttribPointer(0, 4, GL_FLOAT, false, 16 * 4, 0 * 4);

				glEnableVertexAttribArray(1);
				glVertexAttribPointer(1, 4, GL_FLOAT, false, 16 * 4, 4 * 4);

				glEnableVertexAttribArray(2);
				glVertexAttribPointer(2, 4, GL_FLOAT, false, 16 * 4, 8 * 4);

				glEnableVertexAttribArray(3);
				glVertexAttribPointer(3, 4, GL_FLOAT, false, 16 * 4, 12 * 4);
			}
			glBindVertexArray(0);
		} else {
			float[] dataBuffer = ModelUtils.flattenArrays(vertices, null, uvs, normals);

			vao = glGenVertexArrays();
			vbo = glGenBuffers();
			ebo = glGenBuffers();

			glBindVertexArray(vao);
			{
				// upload VBO
				glBindBuffer(GL_ARRAY_BUFFER, vbo);
				glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_DYNAMIC_READ);

				glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
				glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

				// define Vertex Attributes
				glEnableVertexAttribArray(0);
				glVertexAttribPointer(0, 4, GL_FLOAT, false, 12 * 4, 0 * 4);

				glEnableVertexAttribArray(1);
				glVertexAttribPointer(1, 4, GL_FLOAT, false, 12 * 4, 4 * 4);

				glEnableVertexAttribArray(3);
				glVertexAttribPointer(3, 4, GL_FLOAT, false, 12 * 4, 8 * 4);

			}
			glBindVertexArray(0);

		}
	}

	protected void renderProcess() {
	}

	protected void uploadMaterial() {
		glUniform1f(uniforms.get("material.ambient"), material.getAmbient());
		glUniform1f(uniforms.get("material.diffuse"), material.getAmbient());
		glUniform1f(uniforms.get("material.specular"), material.getAmbient());
		glUniform1f(uniforms.get("material.reflectance"), material.getReflectance());
		glUniform1i(uniforms.get("material.hasTexture"), material.hasTexture() ? 1 : 0);
	}

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

	protected void uploadMatrixes() {
		glUniformMatrix4fv(uniforms.get("viewMatrix"), false, Renderer.camera.getView().toFa_());
		glUniformMatrix4fv(uniforms.get("projectionMatrix"), false, Renderer.camera.getProjectionMatrix().toFa_());
		glUniformMatrix4fv(uniforms.get("modelMatrix"), false, modelMatrix.toFa_());
		glUniform4fv(uniforms.get("cameraPos"), new Vec4(Renderer.camera.getCameraPosition(), 1.0f).toFA_());
	}

	protected void updateMinmax() {
		translation = new Vec4(modelMatrix.mul(new Vec4(0.0f, 0.0f, 0.0f, 1.0f)));
		minmax[0] = (startMinmax[0] * scale) + translation.x;
		minmax[1] = (startMinmax[1] * scale) + translation.x;
		minmax[2] = (startMinmax[2] * scale) + translation.y;
		minmax[3] = (startMinmax[3] * scale) + translation.y;
		minmax[4] = (startMinmax[4] * scale) + translation.z;
		minmax[5] = (startMinmax[5] * scale) + translation.z;
	}

	public void render() {
		if (!init) {
			init();
		}
		{
			glUseProgram(program.getProgramID());
			{
				if (material != null) {
					glBindTexture(GL_TEXTURE_2D, material.getTexture());
				}
				glBindVertexArray(vao);
				{
					updateMinmax();
					renderProcess();

					uploadMaterial();
					uploadLighting();
					uploadMatrixes();

					if (!hasEbo) {
						glDrawArrays(GL_TRIANGLES, 0, triangles);
					} else {
						GL15.glDrawElements(GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
					}

				}
				glBindVertexArray(0);
			}
			glUseProgram(0);
		}
		if (showNormals) {
			normalDrawing.render();
		}
	}

	public Material getMaterial() {
		return material;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}

	public Float[] getVertices() {
		return vertices;
	}

	public Float[] getNormals() {
		return normals;
	}

	public Float[] getUvs() {
		return uvs;
	}

	public void setShaderFolder(String shaderFolder) {
		this.shaderFolder = shaderFolder;
	}

	public ShaderProgram getProgram() {
		return program;
	}

	public void setShowNormals(boolean showNormals) {
		this.showNormals = showNormals;
	}

	public float getScale() {
		return scale;
	}

	public Mat4 getModelMatrix() {
		return modelMatrix;
	}

	public void setModelMatrix(Mat4 modelMatrix) {
		this.modelMatrix = modelMatrix;
	}

	public Float[] getMinmax() {
		return minmax;
	}
	
	public void setHasEbo(boolean hasEbo) {
		this.hasEbo = hasEbo;
	}

	public void setScale(float scale) {
		for (int i = 0; i < minmax.length; i++) {
			minmax[i] *= scale;
		}
		this.scale = scale;
		modelMatrix.scale(scale);
	}

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
