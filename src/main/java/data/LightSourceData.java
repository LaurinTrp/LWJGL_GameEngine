package main.java.data;

import org.joml.Math;

import glm.vec._3.Vec3;

public class LightSourceData {

	private LightType type; // 0=directional, 1=point, 2=spotlight
	private Vec3 position;
	private Vec3 direction;

	private Vec3 ambient;
	private Vec3 diffuse;
	private Vec3 specular;

	public LightSourceData(LightType type, Vec3 position, Vec3 direction, Vec3 ambient, Vec3 diffuse, Vec3 specular) {
		this.type = type;
		this.position = position;
		this.direction = direction;
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
	}

	public static int bufferSize() {
		return Integer.BYTES + 3 * Float.BYTES * 5;
	}

	public LightType getType() {
		return type;
	}

	public void setType(LightType type) {
		this.type = type;
	}

	public Vec3 getPosition() {
		return position;
	}

	public void setPosition(Vec3 position) {
		this.position = position;
	}

	public Vec3 getDirection() {
		return direction;
	}

	public void setDirection(Vec3 direction) {
		this.direction = direction;
	}

	public Vec3 getAmbient() {
		return ambient;
	}

	public void setAmbient(Vec3 ambient) {
		this.ambient = ambient;
	}

	public Vec3 getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(Vec3 diffuse) {
		this.diffuse = diffuse;
	}

	public Vec3 getSpecular() {
		return specular;
	}

	public void setSpecular(Vec3 specular) {
		this.specular = specular;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.position);
		return sb.toString();
	}

	public static class PointLightData extends LightSourceData {
		private float constant;
		private float linear;
		private float quadatic;

		public PointLightData(Vec3 position, Vec3 direction, Vec3 ambient, Vec3 diffuse, Vec3 specular, float constant,
				float linear, float quadatic) {
			super(LightType.POINT, position, direction, ambient, diffuse, specular);
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
	}

	public static class SpotLight extends LightSourceData {

		private float innerCutoff;
		private float outerCutoff;
		
		public SpotLight(Vec3 position, Vec3 direction, Vec3 ambient, Vec3 diffuse, Vec3 specular, float innerCutoffAngle, float outerCutoffAngle) {
			super(LightType.SPOT, position, direction, ambient, diffuse, specular);
			
			innerCutoff = (float) Math.cos(Math.toRadians(innerCutoffAngle));
			outerCutoff = (float) Math.cos(Math.toRadians(outerCutoffAngle));
		}

		public float getInnerCutoff() {
			return innerCutoff;
		}
		
		public float getOuterCutoff() {
			return outerCutoff;
		}
		
	}

}
