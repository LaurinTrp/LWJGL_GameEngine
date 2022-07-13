package render.passes.standard;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import gui.Engine_Main;
import shader.ShaderProgram;

public class RectanglePass {

	private boolean init = false;

	private int vao = 0, vbo = 0;
	private int offset;
    int modelID;
    int viewID;
    int projID;
    ByteBuffer texture = null;
	private ShaderProgram program;
	FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	private void initTextures() {
		// Create a new OpenGL texture 
		int textureId = glGenTextures();
		// Bind the texture
		glBindTexture(GL_TEXTURE_2D, textureId);
		
//		texture = Loader.ImageLoader.getImage();
		
	}
	
	private void initVAOs() {

		// create vertex array
		float[] vertices = new float[] {
				
				// Coord					// Color 					// TexCoord
				
				// Triangle 0
				// TL
				-0.5f, 0.5f, 0.0f, 1.0f,	0.0f, 1.0f, 0.0f, 0.0f,		
				
				// BL
				-0.5f, -0.5f, 0.0f, 1.0f,	0.0f, 0.0f, 0.0f, 0.0f,		
				
				//BR
				0.5f, -0.5f, 0.0f, 1.0f,	1.0f, 0.0f, 0.0f, 0.0f,		
				
				// Triangle 1
				// TL
				-0.5f, 0.5f, 0.0f, 1.0f,	0.0f, 1.0f, 0.0f, 0.0f,		
				
				// BR
				0.5f, -0.5f, 0.0f, 1.0f,	1.0f, 0.0f, 0.0f, 0.0f,		
				
				// TR
				0.5f, 0.5f, 0.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 0.0f, 	
		};
		/*
		 * 1.0f, 0.0f, 0.0f, 1.0f, 
                        
                        
		1.0f, 0.0f, 0.0f, 1.0f, 
		                        
		                        
		1.0f, 0.0f, 0.0f, 1.0f, 
		                        
		                        
		                        
		1.0f, 0.0f, 0.0f, 1.0f, 
		                        
		                        
		1.0f, 0.0f, 0.0f, 1.0f, 
		                        
		                        
		1.0f, 0.0f, 0.0f, 1.0f, 
		 */

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
			
//			glEnableVertexAttribArray(2);
//			glVertexAttribPointer(2, 4, GL_FLOAT, false, 12 * 4, 8 * 4);
		}
		glBindVertexArray(0);
	}
	
	private void init() {
		initVAOs();
		
		program = new ShaderProgram("Rectangle");
		offset = glGetUniformLocation(program.getProgramID(), "offset");

		// compile and upload shader

		init = true;
	}
	
	float offsetY = 0;
	public void render() {

		if (!init) {
			init();
		}

		if (!init) {
			return;
		}

		{
			glUseProgram(program.getProgramID());
			{
				glBindVertexArray(vao);
				{
					glDrawArrays(GL_TRIANGLES, 0, 6);
				}
				glBindVertexArray(0);
			}
		}
	}

	public void dispose() {

		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		vao = 0;
		vbo = 0;
		if (program != null) {
			program.dispose();
		}

		init = false;
	}

}
