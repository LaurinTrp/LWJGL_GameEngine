package main.java.model;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.data.Material;
import main.java.gui.Engine_Main;
import main.java.model.objects.Mesh;
import main.java.render.Renderer;
import main.java.render.passes.framebuffers.IFramebuffer;
import main.java.render.renderobject.IRenderObject;
import main.java.shader.ShaderProgram;
import main.java.utils.model.ModelUtils;

public abstract class AssimpModel implements IRenderObject {

	private AIScene scene;
	private List<Mesh> meshes;

	protected boolean init;
	protected boolean render = true;

	private ShaderProgram program;
	protected Map<String, Integer> uniforms = new HashMap<>();

	protected Mat4[] modelMatrices = new Mat4[] { new Mat4(1.0f) };
	private Map<Mesh, Mat4[]> meshMatrices = new HashMap<>();

	protected Mat4 modelMatrix;
	
	protected Material material;

	protected float[] minmax = new float[6];
	protected float[] startMinmax = new float[6];

	public AssimpModel(AIScene scene, Mat4[] matrices) {
		if (scene == null) {
			System.err.println("Failed to load: " + this);
			return;
		}

		this.scene = scene;

		minmax[0] = Float.MAX_VALUE;
		minmax[1] = -Float.MAX_VALUE;
		minmax[2] = Float.MAX_VALUE;
		minmax[3] = -Float.MAX_VALUE;
		minmax[4] = Float.MAX_VALUE;
		minmax[5] = -Float.MAX_VALUE;

		this.modelMatrices = matrices;
	}

	public AssimpModel(AIScene scene) {
		this(scene, new Mat4[] { new Mat4(1.0f) });
		this.modelMatrix = modelMatrices[0];
	}

	@Override
	public void init() {

		loadMeshes();
		loadMaterials();

		initShader();

		afterInit();

		init = true;
	}

	private void loadMeshes() {
		int meshCount = scene.mNumMeshes();

		PointerBuffer aiMeshes = scene.mMeshes();

		AINode root = scene.mRootNode();
		
		meshes = new ArrayList<>();
		for (int i = 0; i < meshCount; ++i) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));

			AINode parent = ModelUtils.getMeshParentNode(root, aiMesh, 0);
			Mat4 meshMatrix = new Mat4(1.0f);
			if(parent != null) {
				meshMatrix = ModelUtils.assimpMatrixToMat4(parent.mTransformation());
			}
			Mesh mesh = new Mesh(this, aiMesh, meshMatrix);
			meshes.add(mesh);
		}

		updateMeshMatrices();
	}
	
	protected void updateMeshMatrices() {
		if(meshes == null) {
			return;
		}
		for (Mesh mesh : meshes) {
			Mat4[] tempMeshMatrices = new Mat4[modelMatrices.length];
			for(int j = 0; j < modelMatrices.length; j++) {
				tempMeshMatrices[j] = new Mat4(mesh.getModelMatrix()).mul(modelMatrices[j]);
			}
			meshMatrices.put(mesh, tempMeshMatrices);
		}
	}

	private void loadMaterials() {
	}

	private void initShader() {
		program = new ShaderProgram("AssimpModel");
		ModelUtils.createUniform(program, uniforms, "modelMatrix");
		ModelUtils.createUniform(program, uniforms, "viewMatrix");
		ModelUtils.createUniform(program, uniforms, "projectionMatrix");
		ModelUtils.createUniform(program, uniforms, "cameraPos");
		ModelUtils.createUniform(program, uniforms, "selected");

		ModelUtils.createUniform(program, uniforms, "selected");

		ModelUtils.createUniform(program, uniforms, "bufferID");

		ModelUtils.createUniform(program, uniforms, "colorID");
	}

	private void uploadMatrixes(Mat4 meshMatrix) {
		glUniformMatrix4fv(uniforms.get("viewMatrix"), false, Renderer.camera.getView().toFa_());
		glUniformMatrix4fv(uniforms.get("projectionMatrix"), false, Renderer.camera.getProjectionMatrix().toFa_());
		glUniformMatrix4fv(uniforms.get("modelMatrix"), false, meshMatrix.toFa_());
		glUniform4fv(uniforms.get("cameraPos"), new Vec4(Renderer.camera.getCameraPosition(), 1.0f).toFA_());
	}

	protected void uploadLighting() {

		Engine_Main.lightManager.update(program, uniforms);
		Engine_Main.lightManager.uploadData(program, uniforms);

	}

	protected abstract void afterInit();

	protected abstract void renderProcessBegin();

	protected abstract void renderProcessEnd();

	@Override
	public void render() {
		if (!init) {
			init();
		}
		if (!init) {
			return;
		}

		if (!render) {
			return;
		}
		glUseProgram(program.getProgramID());
		if (material != null) {
			glActiveTexture(GL_TEXTURE0 + 0);
			glBindTexture(GL_TEXTURE_2D, material.getTexture());
		}
		for (Mesh mesh : meshes) {
			glBindVertexArray(mesh.vao);

			uploadLighting();
			glUniform3fv(uniforms.get("colorID"),
					new Vec3(ModelUtils.getObjectIdAsColor(mesh.getId())).div(255f).toFa_());
			for (int i = 0; i < modelMatrices.length; i++) {
				uploadMatrixes(meshMatrices.get(mesh)[i]);
				{
					renderToObjectPickBuffer(mesh);
					renderToFramebuffer(mesh);
				}
			}

			glBindVertexArray(0);
		}
		glUseProgram(0);
		
		for (Mesh mesh : meshes) {
			mesh.getBoundingBox().render();
		}
	}

	private void renderToObjectPickBuffer(Mesh mesh) {
		glUniform1i(uniforms.get("bufferID"), IFramebuffer.objectPickBufferID);
		Renderer.objectPickBuffer.bindFbo();

		glDrawElements(GL_TRIANGLES, mesh.elements, GL_UNSIGNED_INT, 0);

		Renderer.objectPickBuffer.unbindFbo();
	}

	private void renderToFramebuffer(Mesh mesh) {
		glUniform1i(uniforms.get("bufferID"), IFramebuffer.framebufferID);
		renderProcessBegin();
		Renderer.framebuffer.bindFbo();

		glUniform1i(uniforms.get("selected"), mesh.isSelected() ? 1 : 0);
		glDrawElements(GL_TRIANGLES, mesh.elements, GL_UNSIGNED_INT, 0);

		Renderer.framebuffer.unbindFbo();
		renderProcessEnd();
	}

	protected void scale(float scale) {
		for (int i = 0; i < modelMatrices.length; i++) {
			modelMatrices[i] = modelMatrices[i].scale(scale);
		}

		for (int i = 0; i < startMinmax.length; i++) {
			startMinmax[i] *= scale;
		}
	}

	public void clicked(Mesh mesh) {
	}

	@Override
	public void dispose() {
		scene = null;
		meshes = null;
	}
	
}
