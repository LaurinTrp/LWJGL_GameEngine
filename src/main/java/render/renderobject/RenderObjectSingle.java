package main.java.render.renderobject;

import java.awt.Color;

import glm.vec._4.Vec4;
import main.java.render.Renderer;

public abstract class RenderObjectSingle implements IRenderObject {
	
	private static int instanceCounter = 0;
	private final int objectId;
	private final Vec4 color;
	
	protected boolean selected;
	
	public RenderObjectSingle() {
		instanceCounter++;
		objectId = instanceCounter;
		
		color = new Vec4((objectId >> 16) & 0xFF, (objectId >> 8) & 0xFF, (objectId >> 0) & 0xFF, (objectId >> 24) & 0xFF);
		
		Renderer.modelObserver.addObjectToSelectables(this);
	}
	
	@Override
	public abstract void init();

	@Override
	public abstract void render();

	@Override
	public abstract void dispose();

	public int getObjectId() {
		return objectId;
	}

	public Vec4 getObjectIdAsColor() {
		return color;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
