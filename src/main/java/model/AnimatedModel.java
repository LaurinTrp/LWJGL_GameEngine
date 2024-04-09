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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.opengl.GL11;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.data.Material;
import main.java.data.animation.AnimationData;
import main.java.model.objects.AnimMesh;
import main.java.model.objects.Mesh;
import main.java.model.objects.Node;
import main.java.render.Renderer;
import main.java.render.renderobject.IRenderObject;
import main.java.render.utils.BoundingBox;
import main.java.shader.ShaderProgram;
import main.java.utils.loaders.AnimationLoader;
import main.java.utils.model.ModelUtils;

public abstract class AnimatedModel implements IRenderObject {

	private AIScene scene;
	private List<AnimMesh> meshes;

	protected boolean init;
	protected boolean render = true;

	private ShaderProgram program;
	protected ShaderProgram programObjectPick;
	protected HashMap<String, Integer> uniforms = new HashMap<>();
	protected HashMap<String, Integer> uniformsObjectPick = new HashMap<>();

	protected Mat4 modelMatrix = new Mat4(1.0f);

	protected Material material;

	protected float[] minmax = new float[6];
	protected float[] startMinmax = new float[6];

	@SuppressWarnings("unused")
	private BoundingBox boundingBox;

	private AnimationLoader animator;
	private AnimationData animationData;

	public AnimatedModel(AIScene scene) {

		this.scene = scene;
		this.animator = new AnimationLoader(scene);

		minmax[0] = Float.MAX_VALUE;
		minmax[1] = -Float.MAX_VALUE;
		minmax[2] = Float.MAX_VALUE;
		minmax[3] = -Float.MAX_VALUE;
		minmax[4] = Float.MAX_VALUE;
		minmax[5] = -Float.MAX_VALUE;

	}

	@Override
	public void init() {

		loadMeshes();

		AINode aiRootNode = scene.mRootNode();
		Node rootNode = processNodesHirarchy(aiRootNode, null);
		Mat4 globalInverseTransformation = ModelUtils.assimpMatrixToMat4(scene.mRootNode().mTransformation()).inverse();
		animator.loadAnimations(meshes.get(0).getBoneList(), rootNode, globalInverseTransformation);

		animationData = new AnimationData(animator.getAnimations().get(0));

		loadMaterials();

		initShader();

		afterInit();

		init = true;
	}

	private void loadMeshes() {
		int meshCount = scene.mNumMeshes();

		PointerBuffer aiMeshes = scene.mMeshes();

		meshes = new ArrayList<>();
		for (int i = 0; i < meshCount; ++i) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
			AnimMesh mesh = new AnimMesh(aiMesh);
			meshes.add(mesh);

			minmax[0] = Math.min(mesh.getMinmax()[0], minmax[0]);
			minmax[1] = Math.max(mesh.getMinmax()[1], minmax[1]);
			minmax[2] = Math.min(mesh.getMinmax()[2], minmax[2]);
			minmax[3] = Math.max(mesh.getMinmax()[3], minmax[3]);
			minmax[4] = Math.min(mesh.getMinmax()[4], minmax[4]);
			minmax[5] = Math.max(mesh.getMinmax()[5], minmax[5]);
		}

		startMinmax = Arrays.copyOf(minmax, minmax.length);

		boundingBox = new BoundingBox(minmax, modelMatrix);
	}

	private Node processNodesHirarchy(AINode aiNode, Node parent) {
		Node node = new Node(aiNode, parent);

		PointerBuffer aiChildren = aiNode.mChildren();
		for (int i = 0; i < aiNode.mNumChildren(); i++) {
			AINode aiChild = AINode.create(aiChildren.get());
			Node childNode = processNodesHirarchy(aiChild, node);
			node.addChild(childNode);
		}

		return node;
	}

	private void loadMaterials() {

	}

	private void initShader() {
		program = new ShaderProgram("AssimpAnimModel");
		ModelUtils.createUniform(program, uniforms, "modelMatrix");
		ModelUtils.createUniform(program, uniforms, "viewMatrix");
		ModelUtils.createUniform(program, uniforms, "projectionMatrix");
		ModelUtils.createUniform(program, uniforms, "cameraPos");
		ModelUtils.createUniform(program, uniforms, "selected");

		// Lighting
		ModelUtils.createUniform(program, uniforms, "lightsources");
		ModelUtils.createUniform(program, uniforms, "numOfLights");

		ModelUtils.createUniform(program, uniforms, "sunPosition");
		ModelUtils.createUniform(program, uniforms, "sunColor");
		ModelUtils.createUniform(program, uniforms, "selected");

		ModelUtils.createUniform(program, uniforms, "bonesMatrices");

		programObjectPick = new ShaderProgram("TransformationObjectPick");
		ModelUtils.createUniform(programObjectPick, uniformsObjectPick, "modelMatrix");
		ModelUtils.createUniform(programObjectPick, uniformsObjectPick, "viewMatrix");
		ModelUtils.createUniform(programObjectPick, uniformsObjectPick, "projectionMatrix");
		ModelUtils.createUniform(programObjectPick, uniformsObjectPick, "cameraPos");
		ModelUtils.createUniform(programObjectPick, uniformsObjectPick, "colorID");
	}

	private void uploadMatrixes() {
		glUniformMatrix4fv(uniforms.get("viewMatrix"), false, Renderer.camera.getView().toFa_());
		glUniformMatrix4fv(uniforms.get("projectionMatrix"), false, Renderer.camera.getProjectionMatrix().toFa_());
		glUniformMatrix4fv(uniforms.get("modelMatrix"), false, modelMatrix.toFa_());
		glUniform4fv(uniforms.get("cameraPos"), new Vec4(Renderer.camera.getCameraPosition(), 1.0f).toFA_());

		Mat4[] matrices = animationData.getCurrentFrame().boneMatrices();
		float[] buffer = new float[matrices.length * 16]; 
		Mat4 identityMatrix = new Mat4(1.0f);
		for (int i = 0; i < matrices.length; i++) {
//			float[] matrixArray = matrices[i].toFa_();
			float[] matrixArray = identityMatrix.toFa_();
			System.arraycopy(matrixArray, 0, buffer, i * 16, 16);
		}
		glUniformMatrix4fv(uniforms.get("bonesMatrices"), false, buffer);
	}

	protected void uploadLighting() {
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

		renderToObjectPickBuffer();
		renderToFramebuffer();
	}

	private void renderToObjectPickBuffer() {
		{
			Renderer.objectPickBuffer.bindFbo();
			glUseProgram(programObjectPick.getProgramID());
			{
				for (Mesh mesh : meshes) {
					glBindVertexArray(mesh.vao);
					{
						uploadMatrixes();
						glUniform3fv(uniformsObjectPick.get("colorID"),
								new Vec3(ModelUtils.getObjectIdAsColor(mesh.getId())).div(255f).toFa_());
						if (render) {
							glDrawElements(GL_TRIANGLES, mesh.elements, GL11.GL_UNSIGNED_INT, 0);
						}
					}
					glBindVertexArray(0);
				}
			}
			Renderer.objectPickBuffer.unbindFbo();
			glUseProgram(0);
		}
	}

	private void renderToFramebuffer() {
		glUseProgram(program.getProgramID());
		Renderer.framebuffer.bindFbo();
		{
			if (material != null) {
				glActiveTexture(GL_TEXTURE0 + 0);
				glBindTexture(GL_TEXTURE_2D, material.getTexture());
			}
			animationData.nextFrame();
			for (Mesh mesh : meshes) {
				glBindVertexArray(mesh.vao);
				{
					renderProcessBegin();
					if (render) {
						uploadMatrixes();
						uploadLighting();
						glUniform1i(uniforms.get("selected"), mesh.isSelected() ? 1 : 0);

						glDrawElements(GL_TRIANGLES, mesh.elements, GL_UNSIGNED_INT, 0);
					}

					renderProcessEnd();
				}
				glBindVertexArray(0);

			}
		}
		Renderer.framebuffer.unbindFbo();
		glUseProgram(0);
	}

	protected void scale(float scale) {
		modelMatrix.scale(scale);

//		for (Mesh mesh : meshes) {
//			for (float value : mesh.getMinmax()) {
//				value *= scale;
//			}
//		}

		for (int i = 0; i < startMinmax.length; i++) {
			startMinmax[i] *= scale;
		}
	}

	public AnimationData getAnimationData() {
		return animationData;
	}

	public void setAnimationData(AnimationData animationData) {
		this.animationData = animationData;
	}

	@Override
	public void dispose() {
		scene = null;
		meshes = null;
	}

}
