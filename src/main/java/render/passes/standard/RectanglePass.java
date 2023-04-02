package main.java.render.passes.standard;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import main.java.render.IRenderObject;
import main.java.render.utilities.terrain.ProceduralTerrain;
import main.java.shader.ShaderProgram;
import main.java.utils.ImageUtils;
import main.java.utils.loaders.ImageLoader;
import main.java.utils.loaders.ImageLoaderTest;
import main.java.utils.math.Noise;

public class RectanglePass implements IRenderObject {

	private boolean init = false;

	private int vao = 0, vbo = 0;
	private int offset;
    int modelID;
    int viewID;
    int projID;
	private ShaderProgram program;
	FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	private int textureId;

	private void initTextures() {
		
//		ProceduralTerrain pt = new ProceduralTerrain(100, 1, 0, 0);
		
		
		textureId = ImageUtils.createTextureFromImage(Noise.createNoiseImage(200, 200, 0, 0));
		
		// Bind the texture
//		glBindTexture(GL_TEXTURE_2D, ImageUtils.createTextureFromImage(image));


	}

	private void initVAOs() {

		// create vertex array
		float[] vertices = new float[] {

				// Coord					// Color 					// TexCoord

				// Triangle 0
				// TL
				-0.5f, 0.5f, 0.0f, 1.0f,	0.0f, 1.0f, 0.0f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f,
                                                                                                 
				// BL                                                                            
				-0.5f, -0.5f, 0.0f, 1.0f,	0.0f, 0.0f, 0.0f, 0.0f,    0.0f, 0.0f, 0.0f, 1.0f,   
                                                                                                 
				//BR                                                                             
				0.5f, -0.5f, 0.0f, 1.0f,	1.0f, 0.0f, 0.0f, 0.0f,    1.0f, 0.0f, 0.0f, 1.0f,   
                                                                                                 
				// Triangle 1                                                                    
				// TL                                                                            
				-0.5f, 0.5f, 0.0f, 1.0f,	0.0f, 1.0f, 0.0f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f,   
                                                                                                 
				// BR                                                                            
				0.5f, -0.5f, 0.0f, 1.0f,	1.0f, 0.0f, 0.0f, 0.0f,    1.0f, 0.0f, 0.0f, 1.0f,   
                                                                                                 
				// TR                                                                            
				0.5f, 0.5f, 0.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 0.0f,    1.0f, 1.0f, 0.0f, 1.0f,   
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
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 12 * 4, 0 * 4);

			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 12 * 4, 4 * 4);

			glEnableVertexAttribArray(2);
			glVertexAttribPointer(2, 4, GL_FLOAT, false, 12 * 4, 8 * 4);
		}
		glBindVertexArray(0);
	}

	@Override
	public void init() {
		initVAOs();

		program = new ShaderProgram("Rectangle");
		offset = glGetUniformLocation(program.getProgramID(), "offset");

		initTextures();
		// compile and upload shader

		init = true;
	}

	float offsetY = 0;
	@Override
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
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, textureId);
				glUniform1i(glGetUniformLocation(program.getProgramID(), "tex"), 0);
				
				glBindVertexArray(vao);
				{
					glDrawArrays(GL_TRIANGLES, 0, 6);
				}
				glBindVertexArray(0);
			}

		}
	}

	@Override
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
