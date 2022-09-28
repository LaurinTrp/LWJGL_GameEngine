package main.java.render.passes.framebuffers;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import main.java.gui.Engine_Main;
import main.java.shader.ShaderProgram;

public class Framebuffer {

	private boolean init = false;
	private int vao = 0, vbo = 0, fbo = 0, rbo = 0, texture = 0;
	private ShaderProgram program;
	
	public Framebuffer() {
		init();
	}

	private void initFbo() {
		fbo = glGenFramebuffers();
		bindFbo();
	}
	
	private void initRbo() {
		rbo = glGenRenderbuffers();
		bindRbo();
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, Engine_Main.windowWidth, Engine_Main.windowHeight);
		unbindRbo();
	}
	
	private void initVao() {

		// create vertex array
		float[] vertices = new float[] {
				
				// Coord					// TexCoord
				
				// Triangle 0
				// TL
				-1f, 1f, 0.0f, 1.0f,	0.0f, 1.0f, 0.0f, 0.0f,		
				
				// BL
				-1f, -1f, 0.0f, 1.0f,	0.0f, 0.0f, 0.0f, 0.0f,		
				
				//BR
				1f, -1f, 0.0f, 1.0f,	1.0f, 0.0f, 0.0f, 0.0f,		
				
				// Triangle 1
				// TL
				-1f, 1f, 0.0f, 1.0f,	0.0f, 1.0f, 0.0f, 0.0f,		
				
				// BR
				1f, -1f, 0.0f, 1.0f,	1.0f, 0.0f, 0.0f, 0.0f,		
				
				// TR
				1f, 1f, 0.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 0.0f, 	
		};
	

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
	
	private void initTexture() {
		texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, Engine_Main.windowWidth, Engine_Main.windowHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, NULL);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	}
	
	private void initShader() {
		program = new ShaderProgram("Framebuffer");
	}
	
	public void bindFbo() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	}
	
	public void unbindFbo() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void bindRbo() {
		glBindRenderbuffer(GL_RENDERBUFFER, rbo);
	}
	
	public void unbindRbo() {
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
	}
	
	public void init() {
		initFbo();
		
		initTexture();
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);
		
		initRbo();
		
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo);

		if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			System.err.println("Framebuffer not complete");
			return;
		}
		
		System.out.println("Framebuffer complete");
		
		unbindFbo();
		
		initVao();
		initShader();
		
		init = true;
	}
	
	public void render() {
		if (!init) {
			init();
		}

		if (!init) {
			return;
		}
		
		bindFbo();
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//		glEnable(GL_DEPTH_TEST);
	}
	
	public void renderColorAttachments() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0); // back to default
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f); 
		glClear(GL_COLOR_BUFFER_BIT);

		{
			glUseProgram(program.getProgramID());
			{
				glBindVertexArray(vao);
				glDisable(GL_DEPTH_TEST);
				glBindTexture(GL_TEXTURE_2D, texture);
				{
					glDrawArrays(GL_TRIANGLES, 0, 6);
				}
				glBindVertexArray(0);
			}
		}
		
		unbindFbo();
	}
	
	public void dispose() {
		glDeleteFramebuffers(fbo);
		glDeleteRenderbuffers(rbo);
	}

}

