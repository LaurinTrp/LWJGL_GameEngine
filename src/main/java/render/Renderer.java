package main.java.render;

import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;

import java.util.ArrayList;

import glm.glm.vec._4.Vec4;
import main.java.render.passes.TerrainPass;
import main.java.render.passes.standard.RectanglePass;
import main.java.render.passes.standard.TrianglePass;
import main.java.render.passes.texture.TexturePass;
import main.java.render.passes.transformation.Compass;
import main.java.render.passes.transformation.Cottage;
import main.java.render.passes.transformation.LightSourcePass;
import main.java.render.passes.transformation.MyModel;
import main.java.render.passes.transformation.TransformationPass;

public class Renderer {
	
	
	public static Vec4 ambientColor;

	private TrianglePass trianglePass;

	private RectanglePass rectanglePass;
	private TexturePass texturePass;

	private TransformationPass transformationPass;

	private LightSourcePass lightSourcePass;

	public static Camera camera;

	public static ArrayList<Vec4> lightSourcePositions = new ArrayList<>();

	private int framebuffer;

	private Cottage cottage;
	private MyModel model;
	
	private Compass compass;

	private MousePicker mousePicker;
	
	private TerrainPass terrain;
	

	public Renderer() {
		
		ambientColor = new Vec4(1.0f);

		camera = new Camera();

		trianglePass = new TrianglePass();

		rectanglePass = new RectanglePass();
		texturePass = new TexturePass();

		transformationPass = new TransformationPass();

		lightSourcePositions.add(new Vec4(1.2f, 1.0f, 2.0f, 1.0f));
		lightSourcePositions.add(new Vec4(-1.2f, 1.0f, 2.0f, 1.0f));
		
		lightSourcePass = new LightSourcePass();

		lightSourcePass.setLightsourcePositions(lightSourcePositions);
		

		cottage = new Cottage();
		model = new MyModel();
		
		compass = new Compass();
		
		mousePicker = new MousePicker(camera);
		
		terrain = new TerrainPass();
	}

	public void render() {

		camera.moveCamera();
		mousePicker.update();
//		lightSourcePositions.get(0).add(0.01f);
		lightSourcePass.setLightsourcePositions(lightSourcePositions);

//		trianglePass.render();
//		rectanglePass.render();

//		metaBall.render();

//		texturePass.render();

//		compass.render();
//		transformationPass.render();

		cottage.render();
//		model.render();
		
//		compass.render();
//
		lightSourcePass.render();
		terrain.render();

	}

	public void dispose() {

		glDeleteFramebuffers(framebuffer);

		trianglePass.dispose();
		rectanglePass.dispose();
		texturePass.dispose();
		transformationPass.dispose();
		lightSourcePass.dispose();

		cottage.dispose();
		model.dispose();
		
		compass.dispose();
	}

}
