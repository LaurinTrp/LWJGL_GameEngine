package main.java.render.model;


import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;

import main.java.render.IRenderObject;

public class ModelCluster implements IRenderObject {
	
	protected ArrayList<Model> models = new ArrayList<>();
	
	private boolean init = false;
	
	public ModelCluster(ArrayList<Model> models) {
		this.models = models;
	}
	
	@Override
	public void init() {
		afterInit();
		init = true;
	}
	
	public void afterInit() {}
	
	@Override
	public void render() {

		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
//		glFrontFace(GL_CCW); 
		models.forEach(m -> m.render());
		if(!init) {
			init();
		}

		glDisable(GL_CULL_FACE);
	}
	
	@Override
	public void dispose() {
		models.forEach(m -> m.dispose());
	}
	
	
}
