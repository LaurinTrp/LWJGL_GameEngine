package main.java.render.passes.framebuffers;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH24_STENCIL8;
import static org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;

import main.java.gui.Engine_Main;
import main.java.render.Renderer;
import main.java.render.model.Model;
import main.java.render.renderobject.IRenderObject;
import main.java.shader.ShaderProgram;

public class ObjectPickBuffer implements IFramebuffer {

	private boolean init = false;
	private int vao = 0, vbo = 0, fbo = 0, rbo = 0, texture = 0;
	private ShaderProgram program;
	private int uniformScreenSize = 0;

	private boolean clickReady = true;

	private ByteBuffer colorBuffer;

	public ObjectPickBuffer() {
		colorBuffer = BufferUtils.createByteBuffer(4);
	}

	/**
	 * Initializing the Frame Buffer Object
	 */
	@Override
	public void initFbo() {
		fbo = glGenFramebuffers();
		bindFbo();
	}

	/**
	 * Initializing the Render Buffer Object
	 */
	@Override
	public void initRbo() {
		rbo = glGenRenderbuffers();
		bindRbo();
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, Engine_Main.windowWidth, Engine_Main.windowHeight);
		unbindRbo();
	}

	/**
	 * Initializing the vertex buffer object
	 */
	@Override
	public void initVao() {

		// create vertex array
		float[] vertices = new float[] {

				// Coord // TexCoord

				// Triangle 0
				// TL
				-1f, 1f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,

				// BL
				-1f, -1f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,

				// BR
				1f, -1f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,

				// Triangle 1
				// TL
				-1f, 1f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,

				// BR
				1f, -1f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,

				// TR
				1f, 1f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, };

		// create VAO
		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		glBindVertexArray(vao);
		{
			// upload VBO
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_READ);

			// define Vertex Attributes
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 8 * 4, 0 * 4);

			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 8 * 4, 4 * 4);

		}
		glBindVertexArray(0);
	}

	/**
	 * Initializing the texture
	 */
	@Override
	public void initTexture() {
		texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, Engine_Main.windowWidth, Engine_Main.windowHeight, 0, GL_RGB,
				GL_UNSIGNED_BYTE, NULL);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	}

	/**
	 * Initializing the shader
	 */
	@Override
	public void initShader() {
		program = new ShaderProgram("ObjectPickBuffer");
	}

	/**
	 * bind the fbo
	 */
	@Override
	public void bindFbo() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	}

	/**
	 * unbind the fbo
	 */
	@Override
	public void unbindFbo() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	/**
	 * bind the rbo
	 */
	@Override
	public void bindRbo() {
		glBindRenderbuffer(GL_RENDERBUFFER, rbo);
	}

	/**
	 * unbind the rbo
	 */
	@Override
	public void unbindRbo() {
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
	}

	/**
	 * The main init method to initialize the Framebuffer
	 */
	@Override
	public void init() {
		initFbo();

		initTexture();
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);

		initRbo();

		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo);

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			System.err.println("Framebuffer not complete");
			return;
		}

		unbindFbo();

		initVao();
		initShader();

		init = true;
	}

	/**
	 * Rendering to the fbo
	 */
	@Override
	public void render() {
		if (!init) {
			init();
		}

		if (!init) {
			return;
		}

		{
			bindFbo();
			glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			unbindFbo();
		}
	}

	/**
	 * Render the colors
	 */
	@Override
	public void renderColorAttachments() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0); // back to default
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		{
			glUseProgram(program.getProgramID());
			{
				glBindVertexArray(vao);
				glEnable(GL_DEPTH_TEST);
				glDisable(GL_CULL_FACE);
				glBindTexture(GL_TEXTURE_2D, texture);
				{
					if (Engine_Main.mouseHandler.isLMB_Down()) {
						if (!clickReady) {
							return;
						}
						glDrawArrays(GL_TRIANGLES, 0, 6);

						glReadPixels((int) (Engine_Main.windowWidth / 2f), (int) (Engine_Main.windowHeight / 2f), 1, 1,
								GL_RGB, GL_UNSIGNED_BYTE, colorBuffer);

						int red = colorBuffer.get(0) & 0xFF;
						int green = colorBuffer.get(1) & 0xFF;
						int blue = colorBuffer.get(2) & 0xFF;

						int objectId = (red << 16) | (green << 8) | blue;
						IRenderObject object = Renderer.modelObserver.getObjectById(objectId);
						
						System.out.println(red + ", " + green + ", " + blue);
						
						Model objectMulti = (Model) object;
						if(objectMulti != null) {
							objectMulti.setSelected(objectId, !objectMulti.isSelected(objectId));
						}

						clickReady = false;
					} else {
						clickReady = true;
					}
				}
				glEnable(GL_CULL_FACE);
				glBindVertexArray(0);
			}
		}

		unbindFbo();
	}

//	private void writeImage() {
//		glReadPixels(0, 0, Engine_Main.windowWidth, Engine_Main.windowHeight, GL_RGBA, GL_UNSIGNED_BYTE, colorBuffer);
//
//        // Create a BufferedImage from the pixel data
//        BufferedImage image = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
//        int[] pixels = new int[windowWidth * windowHeight];
//        colorBuffer.asIntBuffer().get(pixels);
//        image.setRGB(0, 0, windowWidth, windowHeight, pixels, 0, windowWidth);
//
//        // Write the BufferedImage to a file
//        try {
//            File outputImage = new File("screenshot.png"); // Specify the file path
//            ImageIO.write(image, "png", outputImage);
//            System.out.println("Screenshot saved to: " + outputImage.getAbsolutePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//	}

	/**
	 * Dispose the fbo and rbo
	 */
	@Override
	public void dispose() {
		glDeleteFramebuffers(fbo);
		glDeleteRenderbuffers(rbo);
	}

}
