package main.java.render.renderobject;

import java.util.HashMap;

import glm.mat._4.Mat4;
import glm.vec._4.Vec4;
import main.java.render.Renderer;

public abstract class RenderObjectMulti implements IRenderObject {

	private final HashMap<Integer, Integer> objectIds = new HashMap<>();
	
	private final HashMap<Integer, Vec4> colors = new HashMap<>();
	
	protected HashMap<Integer, Boolean> selected = new HashMap<>();
	
	public RenderObjectMulti(Mat4[] matrices) {
		for (int i = 0; i < matrices.length; i++) {
			int id = Renderer.modelObserver.instanceCounter++;
			
			objectIds.put(i, id);
			
			colors.put(id, new Vec4((id >> 16) & 0xFF, (id >> 8) & 0xFF, (id >> 0) & 0xFF, (id >> 24) & 0xFF));
			
			selected.put(id, false);
		}
		
		Renderer.modelObserver.addObjectToSelectables(this);
	}
	
	@Override
	public abstract void init();

	@Override
	public abstract void render();

	@Override
	public abstract void dispose();

	public int getObjectId(int index) {
		return objectIds.get(index);
	}
	
	public boolean containsID(int id) {
		return objectIds.containsValue(id);
	}

	public Vec4 getObjectIdAsColor(int id) {
		return colors.get(id);
	}
	
	public boolean isSelected(int id) {
		return selected.get(id);
	}
	public void setSelected(int id, boolean selected) {
		this.selected.put(id, selected);
	}
}
