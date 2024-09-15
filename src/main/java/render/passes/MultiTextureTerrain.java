package main.java.render.passes;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.gui.Engine_Main;
import main.java.render.Renderer;
import main.java.render.renderobject.IRenderObject;
import main.java.render.utils.NormalDrawing;
import main.java.render.utils.TexturePack;
import main.java.shader.ShaderProgram;
import main.java.utils.ImageUtils;
import main.java.utils.model.ModelUtils;
import main.java.utils.terrain.TerrainGenerator;

public class MultiTextureTerrain implements IRenderObject {

	protected boolean init = false;

	protected int vao, vbo, ebo;
	@SuppressWarnings("unused")
	private int triangles;

	protected ShaderProgram program;

	protected Mat4 modelMatrix;

	protected HashMap<String, Integer> uniforms = new HashMap<>();

	protected float[] vertices;
	protected float[] uvs;
	protected float[] normals;

	protected int[] indices;

	// min x, max x, min y, max y, min z, max z
	protected final float[] startMinmax;
	protected float[] minmax = new float[6];

	private float scale = 1.0f;

	private String shaderFolder;

	private NormalDrawing normalDrawing;

	private boolean showNormals;

	private Vec4 translation;

	private TexturePack texturePack;

	private TerrainGenerator generator;

	private int heightMapId;

	private final float heightMapMultiplier = 2f;
	
	private Vec2 globalPosition = new Vec2();

	public MultiTextureTerrain(TerrainGenerator generator) {
		while (!generator.isReady()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		startMinmax = generator.getMinmax();
		minmax = generator.getMinmax();
		vertices = generator.getVerticesBuffer();
		uvs = generator.getUvsBuffer();
		normals = generator.getNormalsBuffer();
		indices = generator.getIndicesBuffer();
		triangles = generator.getIndicesBuffer().length;

		this.generator = generator;
	}

	@Override
	public void init() {

		initShader(shaderFolder);
		initMatrixes();
		bindModel();

		afterInit();

		normalDrawing = new NormalDrawing(this);

		updateHeightMap(0, 0);

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

		ModelUtils.createUniform(program, uniforms, "blendMap");
		ModelUtils.createUniform(program, uniforms, "backgroundTexture");
		ModelUtils.createUniform(program, uniforms, "rTexture");
		ModelUtils.createUniform(program, uniforms, "gTexture");
		ModelUtils.createUniform(program, uniforms, "bTexture");

		ModelUtils.createUniform(program, uniforms, "heightMap");

		ModelUtils.createUniform(program, uniforms, "terrainSize");

		ModelUtils.createUniform(program, uniforms, "yOffset");
		ModelUtils.createUniform(program, uniforms, "multiplier");
		
		ModelUtils.createUniform(program, uniforms, "cameraPos");

		glUseProgram(program.getProgramID());
		uploadTextures();
		glUseProgram(0);
	}

	/**
	 * Binding the model and the data
	 */
	private void bindModel() {

		float[] colors = new float[vertices.length];

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
		ebo = glGenBuffers();

		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_READ);

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

			setAttributePointers();

		}
		glBindVertexArray(0);

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
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}

	/**
	 * Needs override to run functions in the render process
	 */
	protected void renderProcessEnd() {
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}

	/**
	 * upload lighting specific values to the shader
	 */
	protected void uploadLighting() {

		Engine_Main.lightManager.update(program, uniforms);
		Engine_Main.lightManager.uploadData(program, uniforms);

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
		translation = new Vec4(0.0f, 0.0f, 0.0f, 1.0f).mul(modelMatrix);
		minmax = ModelUtils.calculateMinmax(startMinmax, translation);
	}

	private void uploadTextures() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, heightMapId);
		glUniform1i(uniforms.get("heightMap"), 0);

		glUniform1i(uniforms.get("blendMap"), 1);
		glUniform1i(uniforms.get("backgroundTexture"), 2);
		glUniform1i(uniforms.get("rTexture"), 3);
		glUniform1i(uniforms.get("gTexture"), 4);
		glUniform1i(uniforms.get("bTexture"), 5);

		glActiveTexture(GL_TEXTURE0 + 1);
		glBindTexture(GL_TEXTURE_2D, texturePack.getBlendMap());
		glActiveTexture(GL_TEXTURE0 + 2);
		glBindTexture(GL_TEXTURE_2D, texturePack.getBackground());
		glActiveTexture(GL_TEXTURE0 + 3);
		glBindTexture(GL_TEXTURE_2D, texturePack.getrTexture());
		glActiveTexture(GL_TEXTURE0 + 4);
		glBindTexture(GL_TEXTURE_2D, texturePack.getgTexture());
		glActiveTexture(GL_TEXTURE0 + 5);
		glBindTexture(GL_TEXTURE_2D, texturePack.getbTexture());

		glUniform1f(uniforms.get("multiplier"), heightMapMultiplier);
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
			Renderer.framebuffer.bindFbo();
			{
				uploadTextures();
				glBindVertexArray(vao);
				{
//					updateMinmax();
					renderProcessBegin();

					// Upload the uniforms
					uploadLighting();
					uploadMatrixes();

//					glUniform1f(uniforms.get("terrainSize"), generator.getSize());

					glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
					glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

					glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
					renderProcessEnd();

				}
				Renderer.framebuffer.unbindFbo();
				glBindVertexArray(0);
			}
			glUseProgram(0);
		}
		// draw normals if necessary
		if (showNormals) {
			normalDrawing.render();
		}
	}

	public boolean isOnTerrain(Vec2 position) {
		return true;
	}

	public float heightAtPosition(Vec2 position) {
		BufferedImage heightMap = generator.getProceduralTerrain().getHeightMap();
		float value = ImageUtils.getFloatValueFromByteGrayImage(heightMap, (int) (position.x + heightMap.getWidth() / 2f), 
				(int) (position.y + heightMap.getHeight() / 2f));
		value = (value * 2f - 1f) * heightMapMultiplier;
		return value;
	}

	public float heightAtPlayerPos() {
		BufferedImage heightMap = generator.getProceduralTerrain().getHeightMap();
		float value = ImageUtils.getFloatValueFromByteGrayImage(heightMap, (int) (heightMap.getWidth() / 2f),
				(int) (heightMap.getHeight() / 2f));
		value = (value * 2f - 1f) * heightMapMultiplier;
		return value;
	}

	int i = 0;

	public void updateHeightMap(float x, float y) {
		generator.getProceduralTerrain().generateHeightMap(x, y);
		heightMapId = generator.getProceduralTerrain().getHeightMapID();
	}

	/**
	 * Getter for the vertices data
	 *
	 * @return The vertices as float array
	 */
	public float[] getVertices() {
		return vertices;
	}

	/**
	 * Getter for the normals data
	 *
	 * @return The normals as float array
	 */
	public float[] getNormals() {
		return normals;
	}

	/**
	 * Getter for the uvs data
	 *
	 * @return The uvs as float array
	 */
	public float[] getUvs() {
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
	 * get the minmax values
	 *
	 * @return minmax values as an float array
	 */
	public float[] getMinmax() {
		return minmax;
	}

	public void setTexturePack(TexturePack texturePack) {
		this.texturePack = texturePack;
	}
	
	public Vec2 getGlobalPosition() {
		return globalPosition;
	}

	/**
	 * set the scale of the model
	 *
	 * @param scale scaling factor
	 */
	public void setScale(float scale) {
		for (int i = 0; i < minmax.length; i++) {
			minmax[i] *= scale;
			startMinmax[i] *= scale;
		}
		this.scale = scale;
		modelMatrix.scale(scale);
	}

	public void translate(Vec3 position) {
		modelMatrix.cleanTranslation();
		modelMatrix.translate(position.x, 0, position.z);

		globalPosition.x = position.x;
		globalPosition.y = position.z;
	}

	@Override
	public void dispose() {

		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);

		if (program != null) {
			program.dispose();
		}

		if (texturePack != null) {
			texturePack.dispose();
		}

		vao = 0;

		if (normalDrawing != null) {
			normalDrawing.dispose();
		}

		init = false;
	}

}
