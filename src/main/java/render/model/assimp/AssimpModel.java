package main.java.render.model.assimp;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import glm.mat._4.Mat4;
import glm.vec._4.Vec4;
import jassimp.AiMesh;
import jassimp.AiScene;
import main.java.render.Renderer;
import main.java.render.model.Material;
import main.java.render.renderobject.IRenderObject;
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;

public abstract class AssimpModel implements IRenderObject {

	private AiScene scene;
	private List<Mesh> meshes;
//	private List<Mesh> meshes;

	protected boolean init;

	private ShaderProgram program;
	protected HashMap<String, Integer> uniforms = new HashMap<>();

	protected Mat4 modelMatrix;
	
	protected Map<Mesh, float[]> startMinMax = new HashMap<>();
	
//    public List<Material> materials;
	
	protected Material material;

	public AssimpModel(AiScene scene) {

		this.scene = scene;

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
		int meshCount = scene.getNumMeshes();
		List<AiMesh> sceneMeshes = scene.getMeshes();
		meshes = new ArrayList<>();
		for (int i = 0; i < meshCount; ++i) {
			Mesh mesh = new Mesh(sceneMeshes.get(i));
			meshes.add(mesh);
			startMinMax.put(mesh, sceneMeshes.get(i).getMinmax());
		}
	}
	
	private void loadMaterials() {
//		int materialCount = scene.getNumMaterials();
//		List<AiMaterial> sceneMaterials = scene.getMaterials();
		
	}

	private void initShader() {
		program = new ShaderProgram("MeshTest");
		ModelUtils.createUniform(program, uniforms, "modelMatrix");
		ModelUtils.createUniform(program, uniforms, "viewMatrix");
		ModelUtils.createUniform(program, uniforms, "projectionMatrix");
		ModelUtils.createUniform(program, uniforms, "cameraPos");
	}

	private void uploadMatrixes() {
		glUniformMatrix4fv(uniforms.get("viewMatrix"), false, Renderer.camera.getView().toFa_());
		glUniformMatrix4fv(uniforms.get("projectionMatrix"), false, Renderer.camera.getProjectionMatrix().toFa_());
		glUniformMatrix4fv(uniforms.get("modelMatrix"), false, modelMatrix.toFa_());
		glUniform4fv(uniforms.get("cameraPos"), new Vec4(Renderer.camera.getCameraPosition(), 1.0f).toFA_());
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

		glUseProgram(program.getProgramID());
		Renderer.framebuffer.bindFbo();
		{
			if(material != null) {
				glBindTexture(GL_TEXTURE_2D, material.getTexture());
			}
			for (Mesh mesh : meshes) {
				glBindVertexArray(mesh.vao);
				{
					renderProcessBegin();
					
					uploadMatrixes();
					glDrawElements(GL_TRIANGLES, mesh.elements, GL_UNSIGNED_INT, 0);

					renderProcessEnd();
				}
				glBindVertexArray(0);

			}
		}
		Renderer.framebuffer.unbindFbo();
		glUseProgram(0);
	}

	@Override
	public void dispose() {
		scene = null;
		meshes = null;
	}
}
