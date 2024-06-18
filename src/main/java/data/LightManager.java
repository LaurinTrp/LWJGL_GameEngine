package main.java.data;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL31.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;

import glm.vec._3.Vec3;
import main.java.render.Renderer;
import main.java.shader.ShaderProgram;
import main.java.utils.model.ModelUtils;

public class LightManager {
	private Map<String, LightSourceData> lights = new HashMap<>();
	private int bufferLights = -1;
	public Map<ShaderProgram, Boolean> update = new HashMap<>();
	public static final int bindingPoint = 0;
	private int uniformBlockIndex;
	private static final int MAX_LIGHTS = 10;

	private LightData lightData;

	public LightManager() {
	}

	public void addLight(String name, LightSourceData data) {
		lights.put(name, data);
		update.forEach((x, y) -> y = true);
	}

	public void removeLight(String name) {
		lights.remove(name);
		update.forEach((x, y) -> y = true);
	}

	public void update(ShaderProgram program, Map<String, Integer> uniforms) {
		if (!update.containsKey(program)) {
			update.put(program, true);
		}
		if (update.get(program)) {
			convertToData();

			if (program != null && uniforms != null) {
				ModelUtils.createUniform(program, uniforms, "numOfLights");

				ModelUtils.createUniform(program, uniforms, "lights.types");
				ModelUtils.createUniform(program, uniforms, "lights.lightPositions");
				ModelUtils.createUniform(program, uniforms, "lights.lightDirections");
				ModelUtils.createUniform(program, uniforms, "lights.lightAmbients");
				ModelUtils.createUniform(program, uniforms, "lights.lightDiffuses");
				ModelUtils.createUniform(program, uniforms, "lights.lightSpeculars");

				ModelUtils.createUniform(program, uniforms, "lights.constant");
				ModelUtils.createUniform(program, uniforms, "lights.linear");
				ModelUtils.createUniform(program, uniforms, "lights.quadratic");

				ModelUtils.createUniform(program, uniforms, "lights.innerCutoff");
				ModelUtils.createUniform(program, uniforms, "lights.outerCutoff");

				ModelUtils.createUniform(program, uniforms, "sunPosition");
				ModelUtils.createUniform(program, uniforms, "sunColor");
			}

			if (bufferLights != -1) {
				glDeleteBuffers(bufferLights);
			}
			bufferLights = glGenBuffers();

			update.put(program, false);
		}
	}

	public void uploadData(ShaderProgram program, Map<String, Integer> uniforms) {
		glUniform1i(uniforms.get("numOfLights"), lights.size());

		glUniform1iv(uniforms.get("lights.types"), lightData.types);
		glUniform3fv(uniforms.get("lights.lightPositions"), lightData.positions);
		glUniform3fv(uniforms.get("lights.lightDirections"), lightData.directions);
		glUniform3fv(uniforms.get("lights.lightAmbients"), lightData.ambients);
		glUniform3fv(uniforms.get("lights.lightDiffuses"), lightData.diffuses);
		glUniform3fv(uniforms.get("lights.lightSpeculars"), lightData.speculars);

		glUniform1fv(uniforms.get("lights.constant"), lightData.constant);
		glUniform1fv(uniforms.get("lights.linear"), lightData.linear);
		glUniform1fv(uniforms.get("lights.quadratic"), lightData.quadratic);

		glUniform1fv(uniforms.get("lights.innerCutoff"), lightData.innerCutoff);
		glUniform1fv(uniforms.get("lights.outerCutoff"), lightData.outerCutoff);

		glUniform4fv(uniforms.get("sunPosition"), Renderer.sun.getLightPosition().toFA_());
		glUniform4fv(uniforms.get("sunColor"), Renderer.sun.getColor().toFA_());
	}

	private void convertToData() {
		lightData = new LightData(new int[lights.size()], new float[lights.size() * 3], new float[lights.size() * 3],
				new float[lights.size() * 3], new float[lights.size() * 3], new float[lights.size() * 3],
				new float[lights.size()], new float[lights.size()], new float[lights.size()], new float[lights.size()],
				new float[lights.size()]);

		int counterTypes = 0;
		int counter = 0;
		for (String key : lights.keySet()) {
			LightSourceData lsd = lights.get(key);

			System.arraycopy(new int[] { lsd.getType().getIndex() }, 0, lightData.types, counterTypes, 1);

			System.arraycopy(lsd.getPosition().toFa_(), 0, lightData.positions, counter, 3);

			System.arraycopy(lsd.getDirection().toFa_(), 0, lightData.directions, counter, 3);

			System.arraycopy(lsd.getAmbient().toFa_(), 0, lightData.ambients, counter, 3);

			System.arraycopy(lsd.getDiffuse().toFa_(), 0, lightData.diffuses, counter, 3);

			System.arraycopy(lsd.getSpecular().toFa_(), 0, lightData.speculars, counter, 3);

			if (lsd.getType() == LightType.POINT) {
				LightSourceData.PointLightData pld = (LightSourceData.PointLightData) lsd;
				lightData.constant[counterTypes] = pld.getConstant();
				lightData.linear[counterTypes] = pld.getLinear();
				lightData.quadratic[counterTypes] = pld.getQuadatic();
			} else {
				lightData.constant[counterTypes] = 0;
				lightData.linear[counterTypes] = 0;
				lightData.quadratic[counterTypes] = 0;
			}

			if (lsd.getType() == LightType.SPOT) {
				LightSourceData.SpotLight sl = (LightSourceData.SpotLight) lsd;
				lightData.innerCutoff[counterTypes] = sl.getInnerCutoff();
				lightData.outerCutoff[counterTypes] = sl.getOuterCutoff();
			} else {
				lightData.innerCutoff[counterTypes] = 0;
				lightData.outerCutoff[counterTypes] = 0;
			}

			counterTypes++;
			counter += 3;
		}
	}

	public int getBuffer() {
		return bufferLights;
	}

	private record LightData(int[] types, float[] positions, float[] directions, float[] ambients, float[] diffuses,
			float[] speculars, float[] constant, float[] linear, float[] quadratic, float[] innerCutoff,
			float[] outerCutoff) {
	}
}
