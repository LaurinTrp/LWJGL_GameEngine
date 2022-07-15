package main.java.render.model;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

public class Material {
	private int texture = 0;
	
	private float ambient;
	private float diffuse;
	private float specular;
	
	private float reflectance;
	
	public Material() {
		ambient = 0.3f;
		diffuse = 0.5f;
		specular = 0.2f;
		reflectance = 1.0f;
	}
	
	public Material(int texture) {
		ambient = 0.3f;
		diffuse = 0.5f;
		specular = 0.2f;
		reflectance = 1.0f;
		this.texture = texture;
	}

	
	public int getTexture() {
		return texture;
	}

	public void setTexture(int texture) {
		this.texture = texture;
	}


	public float getAmbient() {
		return ambient;
	}

	public void setAmbient(float ambient) {
		this.ambient = ambient;
	}

	public float getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(float diffuse) {
		this.diffuse = diffuse;
	}

	public float getSpecular() {
		return specular;
	}

	public void setSpecular(float specular) {
		this.specular = specular;
	}

	public float getReflectance() {
		return reflectance;
	}

	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}

	public void dispose() {
		glDeleteTextures(texture);
	}
	
	public boolean hasTexture() {
		return texture != 0;
	}

}
