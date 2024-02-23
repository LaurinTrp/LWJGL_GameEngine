package main.java.render.model.assimp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.PointerBuffer;

import glm.mat._4.Mat4;
import glm.vec._4.Vec4;
import jassimp.AiMesh;
import jassimp.AiScene;
import main.java.render.Renderer;
import main.java.render.renderobject.IRenderObject;
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class AssimpModel implements IRenderObject {

	public AiScene scene;
	public List<Mesh> meshes;

	private boolean init;

	private ShaderProgram program;
	protected HashMap<String, Integer> uniforms = new HashMap<>();

	protected Mat4 modelMatrix;
//    public List<Material> materials;

	public AssimpModel(AiScene scene) {

		this.scene = scene;

//        int materialCount = scene.mNumMaterials();
//        PointerBuffer materialsBuffer = scene.mMaterials();
//        materials = new ArrayList<>();
//        for (int i = 0; i < materialCount; ++i) {
//            materials.add(new Material(AIMaterial.create(materialsBuffer.get(i))));
//        }
	}

	@Override
	public void init() {
		int meshCount = scene.getNumMeshes();
		List<AiMesh> sceneMeshes = scene.getMeshes();
		meshes = new ArrayList<>();
		for (int i = 0; i < meshCount; ++i) {
			meshes.add(new Mesh(sceneMeshes.get(i)));
		}

		initShader();

		modelMatrix = new Mat4(1.0f);
		modelMatrix.translate(-10, 10, 10);

		init = true;
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
//			if(material != null) {
//				glBindTexture(GL_TEXTURE_2D, material.getTexture());
//			}
			for (Mesh mesh : meshes) {
				glBindVertexArray(mesh.vao);
				{
					uploadMatrixes();
					glDrawElements(GL_TRIANGLES, mesh.elements, GL_UNSIGNED_INT, 0);

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
