package main.java.render.model;

import java.util.HashSet;

import main.java.render.renderobject.IRenderObject;

public class ModelObserver {

	public int instanceCounter = 0;
	private HashSet<Model> selectableObjectsMutli = new HashSet<>();

	public IRenderObject getObjectById(int id) {
		for (Model renderObjectMulti : selectableObjectsMutli) {
			if (renderObjectMulti.containsID(id)) {
				return renderObjectMulti;
			}
		}
		return null;
	}

	public void addObjectToSelectables(IRenderObject model) {
		Model object = (Model) model;
		selectableObjectsMutli.add(object);
	}
}
