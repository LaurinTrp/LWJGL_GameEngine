package main.java.render.debug;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import glm.vec._3.Vec3;
import main.java.model.objects.Mesh;
import main.java.render.Renderer;
import main.java.render.passes.framebuffers.IFramebuffer;
import main.java.render.renderobject.IRenderObject;
import main.java.shader.ShaderProgram;
import main.java.utils.model.ModelUtils;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugRenderPass implements IRenderObject {

	private boolean init = false;
	private int vao, vbo;
	private List<float[]> lines = new ArrayList<>();
	
	private ShaderProgram program;
	private Map<String, Integer> uniforms = new HashMap<>();

	@Override
	public void init() {
		if(program == null) {
			initShader();
		}
		
		int totalSize = 0;
		for (float[] array : lines) {
			totalSize += array.length;
		}

		float[] verticesLines = new float[totalSize];

		int currentIndex = 0;
		for (float[] array : lines) {
			System.arraycopy(array, 0, verticesLines, currentIndex, array.length);
			currentIndex += array.length;
		}

		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		glBindVertexArray(vao);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, verticesLines, GL_STATIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 6, GL_FLOAT, false, 6 * 3, 0);

		glBindVertexArray(0);

		init = true;
	}

	private void initShader() {
		program = new ShaderProgram("Debug");
		ModelUtils.createUniform(program, uniforms, "projection");
		ModelUtils.createUniform(program, uniforms, "view");
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
		glBindVertexArray(vao);

		{
			glUniformMatrix4fv(uniforms.get("view"), false, Renderer.camera.getView().toFa_());
			glUniformMatrix4fv(uniforms.get("projection"), false, Renderer.camera.getProjectionMatrix().toFa_());
			
			renderToFramebuffer();
		}

		glBindVertexArray(0);
		glUseProgram(0);

	}

	private void renderToFramebuffer() {
		Renderer.framebuffer.bindFbo();

		glDrawArrays(GL_LINES, GL_UNSIGNED_INT, 0);

		Renderer.framebuffer.unbindFbo();
	}

	@Override
	public void dispose() {
		program.dispose();
	}

	public void debugLine(Vec3 start, Vec3 end) {
		lines.add(new float[] { start.x, start.y, start.z, end.x, end.y, end.z });
		init = false;
	}
}
