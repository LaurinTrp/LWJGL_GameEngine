package main.java.render.debug;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import glm.vec._3.Vec3;
import main.java.render.Renderer;
import main.java.render.renderobject.IRenderObject;
import main.java.shader.ShaderProgram;

public class DebugRenderPass implements IRenderObject {

	public List<LineData> lines = new ArrayList<>();
	public List<PointData> points = new ArrayList<>();
	
	private RenderData lineData;
	private RenderData pointData;
	
	private List<RenderData> renderDatas = new ArrayList<>();

	private boolean init = false;
	
	public DebugRenderPass() {
		float[] lineWidthRange = {0.0f, 0.0f};
		glGetFloatv(GL_ALIASED_LINE_WIDTH_RANGE, lineWidthRange);
		// Maximum supported line width is in lineWidthRange[1].
	}

	@Override
	public void init() {

		float[] data = new float[lines.size() * LineData.SIZE];

		for (int i = 0; i < lines.size(); i++) {
			System.arraycopy(lines.get(i).getVertices(), 0, data, i * LineData.SIZE, LineData.SIZE);
		}
		
		lineData = new RenderData(GL_LINES, data, "debug" + File.separator + "DebugLine");


		data = new float[points.size() * PointData.SIZE];

		for (int i = 0; i < points.size(); i++) {
			System.arraycopy(points.get(i).getVertices(), 0, data, i * PointData.SIZE, PointData.SIZE);
		}
		
		pointData = new RenderData(GL_POINTS, data, "debug" + File.separator + "DebugPoint");

		renderDatas.add(lineData);
		renderDatas.add(pointData);
		
		init = true;
	}


	@Override
	public void render() {

		if (!init) {
			init();
		}

		if (!init) {
			return;
		}
		glEnable(GL_PROGRAM_POINT_SIZE);
		glEnable(GL_POINT_SMOOTH);
		glDisable(GL_DEPTH_TEST);
		{
			for (RenderData renderData : renderDatas) {
				glUseProgram(renderData.program.getProgramID());
				{
					glBindVertexArray(renderData.vao);
					{
						glUniformMatrix4fv(renderData.viewID, false, Renderer.camera.getView().toFa_());
						glUniformMatrix4fv(renderData.projID, false, Renderer.camera.getProjectionMatrix().toFa_());

						Renderer.framebuffer.bindFbo();

						glDrawArrays(renderData.type, 0, (int) (lineData.data.length / 8f));
						
						Renderer.framebuffer.unbindFbo();

					}
					glBindVertexArray(0);
				}
				glUseProgram(0);
			}
			glEnable(GL_DEPTH_TEST);
			glDisable(GL_PROGRAM_POINT_SIZE);
			
//			glUseProgram(lineData.program.getProgramID());
//			{
//				glBindVertexArray(lineData.vao);
//				{
//					glUniformMatrix4fv(lineData.viewID, false, Renderer.camera.getView().toFa_());
//					glUniformMatrix4fv(lineData.projID, false, Renderer.camera.getProjectionMatrix().toFa_());
//
//					Renderer.framebuffer.bindFbo();
//
//					glDrawArrays(GL_LINES, 0, (int) (lineData.data.length / 8f));
//
//					Renderer.framebuffer.unbindFbo();
//
//				}
//				glBindVertexArray(0);
//			}
//			glUseProgram(0);
		}
	}

	@Override
	public void dispose() {
		if(lineData != null) {
			lineData.dispose();
		}
		if(pointData != null) {
			pointData.dispose();
		}

		init = false;
	}
	
	public void addLine(Vec3 start, Vec3 end, Color color, int width) {
		lines.add(new LineData(start, end, color, width));
		init = false;
	}
	
	public void addPoint(Vec3 position, Color color, int size) {
		points.add(new PointData(position, color, size));
		init = false;
	}
	
	private class RenderData {
		private int vao = 0, vbo = 0;
		private int viewID = 0;
		private int projID = 0;

		private ShaderProgram program;
		
		private float[] data;
		
		private int type;

		public RenderData(int type, float[] data, String programPath) {
			this.type = type;
			this.data = data;
			
			vao = glGenVertexArrays();
			vbo = glGenBuffers();
			
			glBindVertexArray(vao);
			{
				// upload VBO
				glBindBuffer(GL_ARRAY_BUFFER, vbo);
				glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_READ);

				// define Vertex Attributes
				glEnableVertexAttribArray(0);
				glVertexAttribPointer(0, 4, GL_FLOAT, false, 8 * 4, 0 * 4);

				glEnableVertexAttribArray(1);
				glVertexAttribPointer(1, 4, GL_FLOAT, false, 8 * 4, 4 * 4);
			}
			glBindVertexArray(0);
			
			if(program == null) {
				initShader(programPath);
			}
		}
		
		private void initShader(String programPath) {

			program = new ShaderProgram(programPath);
			viewID = glGetUniformLocation(program.getProgramID(), "viewMatrix");
			projID = glGetUniformLocation(program.getProgramID(), "projectionMatrix");

		}
		
		public void dispose() {
			glDeleteVertexArrays(vao);
			glDeleteBuffers(vbo);

			if (program != null) {
				program.dispose();
			}

			vao = 0;
			vbo = 0;
		}
	}

	private class LineData{
		public static final int SIZE = 16;
		private float[] vertices;
		
		public LineData(Vec3 start, Vec3 end, Color color, int width) {
			
			Vec3 colorVec = new Vec3(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);

			vertices = new float[] { start.x, start.y, start.z, 1.0f, colorVec.x, colorVec.y, colorVec.z, 1.0f, end.x,
					end.y, end.z, 1.0f, colorVec.x, colorVec.y, colorVec.z, 1.0f, };
			
		}

		public float[] getVertices() {
			return vertices;
		}
		
	}
	

	private class PointData {
		public static final int SIZE = 8;
		private float[] vertices;

		public PointData(Vec3 position, Color color, int size) {
			
			Vec3 colorVec = new Vec3(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);

			vertices = new float[] { position.x, position.y, position.z, 1.0f, colorVec.x, colorVec.y, colorVec.z, 1.0f};
		}

		public float[] getVertices() {
			return vertices;
		}
	}

}
