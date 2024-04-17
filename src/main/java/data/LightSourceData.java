package main.java.data;

import glm.vec._3.Vec3;

public class LightSourceData {
	private Vec3 position;

	private Vec3 ambient;
	private Vec3 diffuse;
	private Vec3 specular;

	public LightSourceData(Vec3 position, Vec3 ambient, Vec3 diffuse, Vec3 specular) {
		this.position = position;
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
	}
	
	public Vec3 getPosition() {
		return position;
	}

	public void setPosition(Vec3 position) {
		this.position = position;
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

}
