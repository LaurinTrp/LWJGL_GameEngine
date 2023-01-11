package main.java.render.passes.framebuffers;

public interface IFramebuffer {
	
	/**
	 * Initializing the Frame Buffer Object
	 */
	public void initFbo();

	/**
	 * Initializing the Render Buffer Object
	 */
	public void initRbo();

	/**
	 * Initializing the vertex buffer object
	 */
	public void initVao();

	/**
	 * Initializing the texture
	 */
	public void initTexture();

	/**
	 * Initializing the shader
	 */
	public void initShader();

	/**
	 * bind the fbo
	 */
	public void bindFbo();

	/**
	 * unbind the fbo
	 */
	public void unbindFbo();

	/**
	 * bind the rbo
	 */
	public void bindRbo();

	/**
	 * unbind the rbo
	 */
	public void unbindRbo();

	/**
	 * The main init method to initialize the Framebuffer
	 */
	public void init();

	/**
	 * Rendering to the fbo
	 */
	public void render();

	/**
	 * Render the colors
	 */
	public void renderColorAttachments();

	/**
	 * Dispose the fbo and rbo
	 */
	public void dispose();
}
