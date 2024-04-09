package main.java.utils.model;

import java.util.HashSet;

import main.java.model.AssimpModel;
import main.java.model.objects.Mesh;
import main.java.render.renderobject.IRenderObject;

public class ModelObserver {

	public int instanceCounter = 0;
	private HashSet<AssimpModel> selectableObjectsMutli = new HashSet<>();
	private HashSet<Mesh> selectableObjects = new HashSet<>();

	public Mesh getObjectById(int id) {
//		for (Model renderObjectMulti : selectableObjectsMutli) {
//			if (renderObjectMulti.containsID(id)) {
//				return renderObjectMulti;
//			}
//		}
//		return null;
		for(Mesh mesh : selectableObjects) {
			if(mesh.getId() == id) {
				return mesh;
			}
		}
		return null;
	}

	public void addObjectToSelectables(IRenderObject model) {
		AssimpModel object = (AssimpModel) model;
		selectableObjectsMutli.add(object);
	}
	

	public void addObjectToSelectables(Mesh mesh) {
		selectableObjects.add(mesh);
		System.out.println("Modelobserver: " + mesh + "\t" + mesh.getId());
	}
}
