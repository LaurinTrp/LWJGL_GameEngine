package main.java.render.renderobject;

public interface IRenderObject {
	
	/**
	 * Initializing the model
	 */
	public void init();
	
	/**
	 * Render method to render the model
	 */
	public void render();

	/**
	 * Clear the data
	 */
	public void dispose();

}
