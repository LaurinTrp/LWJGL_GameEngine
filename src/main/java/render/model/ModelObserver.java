package main.java.render.model;

import java.util.HashMap;
import java.util.HashSet;

import main.java.render.renderobject.IRenderObject;
import main.java.render.renderobject.RenderObjectMulti;
import main.java.render.renderobject.RenderObjectSingle;

public class ModelObserver {

	public int instanceCounter = 0;
	private HashMap<Integer, RenderObjectSingle> selectableObjectsSingle = new HashMap<>();
	private HashSet<RenderObjectMulti> selectableObjectsMutli = new HashSet<>();

	public IRenderObject getObjectById(int id) {
		if(selectableObjectsSingle.containsKey(id)) {
			return selectableObjectsSingle.get(id);
		}
		for (RenderObjectMulti renderObjectMulti : selectableObjectsMutli) {
			if(renderObjectMulti.containsID(id)) {
				return renderObjectMulti;
			}
		}
		return null;
	}

	public void addObjectToSelectables(IRenderObject model) {
		if (model instanceof RenderObjectSingle) {
			RenderObjectSingle object = (RenderObjectSingle) model;
			selectableObjectsSingle.put(object.getObjectId(), object);
		}
		if (model instanceof RenderObjectMulti) {
			RenderObjectMulti object = (RenderObjectMulti) model;
			selectableObjectsMutli.add(object);
		}
	}
}
