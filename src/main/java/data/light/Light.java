package main.java.data.light;

import java.util.Properties;

import org.joml.Math;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AICamera;
import org.lwjgl.assimp.AILight;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.Renderer;
import main.java.utils.model.ModelUtils;

public class Light {

	public static final int TYPE_DIRECTIONAL = 1;
	public static final int TYPE_POINT = 2;
	public static final int TYPE_SPOT = 3;

	private int type = TYPE_DIRECTIONAL;

	private Vec3 position;
	private Vec3 direction;

	private Vec3 ambient;
	private Vec3 diffuse;
	private Vec3 specular;

	private String name;

	public static Light loadLight(Properties properties) {

		String name = properties.getProperty("name");
		int type = Integer.parseInt(properties.getProperty("type"));

		float locX = Float.parseFloat(properties.getProperty("location.x"));
		float locY = Float.parseFloat(properties.getProperty("location.y"));
		float locZ = Float.parseFloat(properties.getProperty("location.z"));
		Vec3 position = new Vec3(locX, locZ, -locY);

		float rotX = Float.parseFloat(properties.getProperty("rotation.x"));
		float rotY = Float.parseFloat(properties.getProperty("rotation.y"));
		float rotZ = Float.parseFloat(properties.getProperty("rotation.z"));
		Vec3 rotation = new Vec3(rotX, rotY, rotZ);

		float gfvX = Float.parseFloat(properties.getProperty("globalFrontVector.x"));
		float gfvY = Float.parseFloat(properties.getProperty("globalFrontVector.y"));
		float gfvZ = Float.parseFloat(properties.getProperty("globalFrontVector.z"));
		Vec3 direction = new Vec3(gfvX, gfvZ, -gfvY);

		float colorR = Float.parseFloat(properties.getProperty("color.r"));
		float colorG = Float.parseFloat(properties.getProperty("color.g"));
		float colorB = Float.parseFloat(properties.getProperty("color.b"));
		Vec3 color = new Vec3(colorR, colorG, colorB);
		
		Vec3 ambient = new Vec3(0.0f);
		Vec3 diffuse = new Vec3(color);
		Vec3 specular = new Vec3(color);

		switch (type) {
		case Light.TYPE_DIRECTIONAL: {
			return new DirectionalLight(name, type, position, direction, ambient, diffuse, specular);
		}
		case Light.TYPE_POINT: {
			float constant = Float.parseFloat(properties.getProperty("constantAttenuation"));
			float linear = Float.parseFloat(properties.getProperty("linearAttenuation"));
			float quadratic = Float.parseFloat(properties.getProperty("quadraticAttenuation"));

			return new PointLightData(name, position, direction, ambient, diffuse, specular, constant, linear,
					quadratic);
		}
		case Light.TYPE_SPOT: {
			
			float innerCutoff = Float.parseFloat(properties.getProperty("spotSize")) / 2f;
			float outerCutoff = innerCutoff * 2 * Float.parseFloat(properties.getProperty("spotBlend"));

			return new SpotLight(name, position, direction, ambient, diffuse, specular, innerCutoff, outerCutoff);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public Vec3 getPosition() {
		return position;
	}

	public Vec3 getDirection() {
		return direction;
	}

	public Vec3 getAmbient() {
		return ambient;
	}

	public Vec3 getDiffuse() {
		return diffuse;
	}

	public Vec3 getSpecular() {
		return specular;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + name + ":\n");
		sb.append("\tType: " + type + "\n");
		sb.append("\tPosition: " + position.toString() + "\n");
		sb.append("\tDirection: " + direction.toString() + "\n");
		
		return sb.toString();
	}
	
	public static class DirectionalLight extends Light {
		public DirectionalLight(String name, int type, Vec3 position, Vec3 direction, Vec3 ambient, Vec3 diffuse,
				Vec3 specular) {
			super.name = name;
			super.type = type;
			super.position = position;
			super.direction = direction;
			super.ambient = ambient;
			super.diffuse = diffuse;
			super.specular = specular;
		}
	}

	public static class PointLightData extends DirectionalLight {
		private float constant;
		private float linear;
		private float quadatic;

		public PointLightData(String name, Vec3 position, Vec3 direction, Vec3 ambient, Vec3 diffuse, Vec3 specular,
				float constant, float linear, float quadatic) {
			super(name, TYPE_POINT, position, direction, ambient, diffuse, specular);
			this.constant = constant;
			this.linear = linear;
			this.quadatic = quadatic;
		}

		public float getConstant() {
			return constant;
		}

		public float getLinear() {
			return linear;
		}

		public float getQuadatic() {
			return quadatic;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("\tConstant: " + constant + "\n");
			sb.append("\tLinear: " + linear + "\n");
			sb.append("\tQuadratic: " + quadatic + "\n");
			return sb.toString();
		}
	}

	public static class SpotLight extends DirectionalLight {

		private float innerCutoff;
		private float outerCutoff;

		public SpotLight(String name, Vec3 position, Vec3 direction, Vec3 ambient, Vec3 diffuse, Vec3 specular,
				float innerCutoffAngle, float outerCutoffAngle) {
			super(name, TYPE_SPOT, position, direction, ambient, diffuse, specular);

			innerCutoff = (float) Math.cos(innerCutoffAngle);
			outerCutoff = (float) Math.cos(outerCutoffAngle);
		}

		public float getInnerCutoff() {
			return innerCutoff;
		}

		public float getOuterCutoff() {
			return outerCutoff;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("\tInner cutoff: " + innerCutoff + "\n");
			sb.append("\tOuter cutoff: " + outerCutoff + "\n");
			return sb.toString();
		}
	}

}
