package main.java.render.model;

import java.util.ArrayList;

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
		models.forEach(m -> m.render());
		if(!init) {
			init();
		}
	}
	
	public void dispose() {
		models.forEach(m -> m.dispose());
	}
	
	
}
