package main.java.render.model.assimp;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.Arrays;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.render.Renderer;
import main.java.render.model.Material;
import main.java.render.renderobject.IRenderObject;
import main.java.shader.ShaderProgram;
import main.java.utils.ModelUtils;
import main.java.utils.Shapes;
import main.java.utils.loaders.ImageLoader;
import resources.ResourceLoader;

public class Mesh3 implements IRenderObject {

	private float[] positions;
	private float[] texCoords;
	private float[] normals;
	private int[] indices;

	private boolean init;

	private int vao, ebo;

	private Vec3 color;
	
	private Mat4 modelMatrix;

	private ShaderProgram program;
	protected HashMap<String, Integer> uniforms = new HashMap<>();
	
	private Material material;

	public Mesh3(float[] positions, float[] texCoords, float[] normals, int[] indices) {
		this.positions = positions;
		this.texCoords = texCoords;
		this.normals = normals;
		this.indices = indices;
		
		this.modelMatrix = new Mat4(1.0f);
		this.modelMatrix.translate(10, 3, 10);
	}

	@Override
	public void init() {

		initShader();
		bindData();
		
		material = new Material(ImageLoader.loadTextureFromResource("Warn.png"));

		init = true;
	}

	private void bindData() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		int[] vbos = new int[3];
		glGenBuffers(vbos);
		
		ebo = glGenBuffers();

	    glBindBuffer(GL_ARRAY_BUFFER, vbos[0]);
	    glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);
	    glVertexAttribPointer(0, 4, GL_FLOAT, false, 4 * 4, 0);
	    glEnableVertexAttribArray(0);

	    glBindBuffer(GL_ARRAY_BUFFER, vbos[1]);
	    glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
	    glVertexAttribPointer(1, 4, GL_FLOAT, false, 4 * 4, 0);
	    glEnableVertexAttribArray(1);

	    glBindBuffer(GL_ARRAY_BUFFER, vbos[2]);
	    glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
	    glVertexAttribPointer(2, 4, GL_FLOAT, false, 4 * 4, 0);
	    glEnableVertexAttribArray(2);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

		glBindVertexArray(0);
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

//		glDisable(GL_CULL_FACE);
		
		glUseProgram(program.getProgramID());
		Renderer.framebuffer.bindFbo();
		{
//			if(material != null) {
//				glBindTexture(GL_TEXTURE_2D, material.getTexture());
//			}
			glBindVertexArray(vao);
			{
				uploadMatrixes();
				glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

			}
			glBindVertexArray(0);
		}
		Renderer.framebuffer.unbindFbo();
		glUseProgram(0);
		
		glEnable(GL_CULL_FACE);
	}

	@Override
	public void dispose() {
		if(program!=null) {
			program.dispose();
		}
		init = false;
	}

}
