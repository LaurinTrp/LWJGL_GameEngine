package main.java.render.passes.framebuffers;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGB;
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
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform2fv;
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
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.system.MemoryUtil.NULL;

import glm.Glm;
import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import main.java.gui.Engine_Main;
import main.java.render.IRenderObject;
import main.java.shader.ShaderProgram;

public class DepthMap implements IFramebuffer {

	private boolean init = false;
	private int vao = 0, vbo = 0, fbo = 0, rbo = 0, texture = 0;
	private ShaderProgram program;
	private int uniformLightSpaceMatrix = 0;
	
	public final int SHADOW_WIDTH = 1024, SHADOW_HEIGHT = 1024;
	Mat4 lightSpaceMatrix ;
	@Override
	public void initFbo() {
		fbo = glGenFramebuffers();
	}

	@Override
	public void initRbo() {

	}

	@Override
	public void initVao() {
	}

	@Override
	public void initTexture() {
		texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	}

	@Override
	public void initShader() {
		program = new ShaderProgram("Depthbuffer");
		uniformLightSpaceMatrix = program.getUniformLocation("lightSpaceMatrix");
	}

	@Override
	public void bindFbo() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	}

	@Override
	public void unbindFbo() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void bindTexture() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public void unbindTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	
	@Override
	public void bindRbo() {

	}

	@Override
	public void unbindRbo() {

	}

	@Override
	public void init() {
		initFbo();
		initTexture();
		
		configureShaderAndMatrices();
	}

	@Override
	public void render() {
		if(!init) {
			init();
		}
		glUseProgram(program.getProgramID());
		glUniform4fv(uniformLightSpaceMatrix, lightSpaceMatrix.toFa_());
		
		glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
		bindFbo();
		glClear(GL_DEPTH_BUFFER_BIT);
		Engine_Main.render.renderScene();
		unbindFbo();
		
	}
	private void configureShaderAndMatrices() {
		float near_plane = 1.0f, far_plane = 7.5f;
		Mat4 lightProjection = Glm.ortho_(-10f, 10f, -10f, 10f, near_plane, far_plane);
		Mat4 lightView = Glm.lookAt_(new Vec3(-2f, 4f, -1f), new Vec3(0f, 0f, 0f), 
				new Vec3(0f, 1f, 0f));
		
		lightSpaceMatrix = new Mat4(lightProjection).mul(lightView);
		
		initShader();
	}

	@Override
	public void renderColorAttachments() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, texture, 0);
		glDrawBuffer(GL_NONE);
		glReadBuffer(GL_NONE);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);  
	}

	@Override
	public void dispose() {

	}
}
