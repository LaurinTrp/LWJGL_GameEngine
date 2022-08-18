package main.java.render.model;

import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;

public class ModelCluster {
	
	protected ArrayList<Model> models = new ArrayList<>();
	
	private boolean init = false;
	
	public ModelCluster(ArrayList<Model> models) {
		this.models = models;
	}
	
	public void init() {
		afterInit();
		init = true;
	}
	
	public void afterInit() {}
	
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
	
	public void dispose() {
		models.forEach(m -> m.dispose());
	}
	
	
}
