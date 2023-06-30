package main.java.render.renderobject;

public abstract class RenderObject implements IRenderObject {
	
	private static int instanceCounter = 0;
	private final int objectId;
	
	public RenderObject() {
		instanceCounter++;
		objectId = instanceCounter;
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
}
