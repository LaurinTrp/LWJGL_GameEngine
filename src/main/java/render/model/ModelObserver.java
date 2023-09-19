package main.java.render.model;

import java.util.HashMap;

import main.java.render.renderobject.RenderObjectSingle;

public class ModelObserver {
	private HashMap<Integer, RenderObjectSingle> selectableObjects = new HashMap<>();
	
	public RenderObjectSingle getObjectById(int id) {
		return selectableObjects.get(id);
	}
	
	public void addObjectToSelectables(RenderObjectSingle model) {
		selectableObjects.put(model.getObjectId(), model);
	}
}
