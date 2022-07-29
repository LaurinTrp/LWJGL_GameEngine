package main.java.render;

import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;

import java.util.ArrayList;

import glm.glm.vec._4.Vec4;
import main.java.render.passes.Cottage;
import main.java.render.passes.LightSourcePass;
import main.java.render.passes.Player;
import main.java.render.passes.TerrainPass;
import main.java.render.passes.standard.RectanglePass;
import main.java.render.passes.standard.TrianglePass;
import main.java.render.passes.texture.TexturePass;
import main.java.render.passes.transformation.Compass;
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
	
	private Player player;
	

	public Renderer() {
		
		ambientColor = new Vec4(1.0f);


		trianglePass = new TrianglePass();

		rectanglePass = new RectanglePass();
		texturePass = new TexturePass();

		transformationPass = new TransformationPass();

		lightSourcePositions.add(new Vec4(1.2f, 1.0f, 2.0f, 1.0f));
		lightSourcePositions.add(new Vec4(-1.2f, 1.0f, 2.0f, 1.0f));
		lightSourcePositions.add(new Vec4(0f, 10.0f, 0.0f, 1.0f));
		
		lightSourcePass = new LightSourcePass();

		lightSourcePass.setLightsourcePositions(lightSourcePositions);
		

		cottage = new Cottage();
		model = new MyModel();
		
		compass = new Compass();
		
		player = new Player();
		
		
		terrain = new TerrainPass();

		camera = new Camera(player);
		mousePicker = new MousePicker(camera);
	}

	public void render() {


		terrain.render();
		lightSourcePass.render();
		
//		trianglePass.render();
//		rectanglePass.render();

//		metaBall.render();

//		texturePass.render();

//		compass.render();
//		transformationPass.render();

		cottage.render();
		
		player.render();

		camera.moveCamera();
		mousePicker.update();
		
//		model.render();
		
//		compass.render();
//

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
		
		player.dispose();
		
		compass.dispose();
	}

}
