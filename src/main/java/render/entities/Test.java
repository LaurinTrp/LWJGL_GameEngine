package main.java.render.entities;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import main.java.render.IRenderObject;
import main.java.render.model.Model;
import main.java.utils.ModelUtils;
import main.java.utils.Shapes;
import main.java.utils.loaders.ImageLoader;

public class Test implements IRenderObject{

	private int vao, vbo;
	
	public Test() {
	}

	@Override
	public void init() {
		
	}
	
	private void initVao() {
		
		
		
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
