package main.java.render;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;

import java.util.ArrayList;
import java.util.Iterator;

import glm.vec._4.Vec4;
import main.java.render.passes.Cottage;
import main.java.render.passes.Cubes;
import main.java.render.passes.Player;
import main.java.render.passes.TerrainPass;
import main.java.render.passes.framebuffers.Framebuffer;
import main.java.render.passes.lighting.LightSourcePass;
import main.java.render.passes.lighting.SunPass;
import main.java.render.passes.standard.RectanglePass;
import main.java.render.passes.standard.TrianglePass;
import main.java.render.passes.texture.TexturePass;
import main.java.render.passes.transformation.Compass;
import main.java.render.passes.transformation.MyModel;
import main.java.render.passes.transformation.TransformationPass;
import main.java.render.passes.trees.Tree_1;
import main.java.render.passes.trees.Trees;
import main.java.utils.ModelUtils;

public class Renderer {
	
	public static Framebuffer framebuffer;
	
	public static Vec4 ambientColor;

	private TrianglePass trianglePass;

	private RectanglePass rectanglePass;
	private TexturePass texturePass;

	private TransformationPass transformationPass;

	private LightSourcePass lightSourcePass;
	public static SunPass sun;

	public static Camera camera;

	public static ArrayList<Vec4> lightSourcePositions = new ArrayList<>();

	private Cottage cottage;
	private MyModel model;
	
	private Compass compass;

	private MousePicker mousePicker;
	
	public static ArrayList<TerrainPass> terrains;
	
	private Player player;
	
	private Trees trees;
	
	private Tree_1 tree_1;
	
	private Cubes cubes;
	

	public Renderer() {
		
		framebuffer = new Framebuffer();
		
		ambientColor = new Vec4(1.0f);


		trianglePass = new TrianglePass();

		rectanglePass = new RectanglePass();
		texturePass = new TexturePass();

		transformationPass = new TransformationPass();

//		lightSourcePositions.add(new Vec4(1.2f, 1.0f, 2.0f, 1.0f));
		lightSourcePositions.add(new Vec4(-1.2f, 1.0f, 2.0f, 1.0f));
//		lightSourcePositions.add(new Vec4(0f, 10.0f, 0.0f, 1.0f));
		
		lightSourcePass = new LightSourcePass();
		lightSourcePass.setLightsourcePositions(lightSourcePositions);
		
		sun = new SunPass();
		

		cottage = new Cottage();
		model = new MyModel();
		
		compass = new Compass();
		
		player = new Player();
		
		
		terrains = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			terrains.add(new TerrainPass(-(i%2*TerrainPass.width), -((i > 1) ? 1: 0)*TerrainPass.height));
		}

		camera = new Camera(player);
		mousePicker = new MousePicker(camera);
		
		trees = new Trees();
		tree_1 = new Tree_1();
		
		cubes = new Cubes();
	}

	public void render() {

		framebuffer.render();

		glEnable(GL_DEPTH_TEST);
		
//		terrain.render();
//		terrain1.render();
		for (TerrainPass terrainPass : terrains) {
			terrainPass.render();
		}
		
		lightSourcePass.render();
		sun.update();
		
		cottage.render();
		
		player.render();

		camera.moveCamera();
//		mousePicker.update();
		
//		model.render();
//		compass.render();
		
		trees.render();
		glDisable(GL_DEPTH_TEST);
		
		framebuffer.renderColorAttachments();

	}

	public void dispose() {

		framebuffer.dispose();
		
		for (TerrainPass terrainPass : terrains) {
			terrainPass.dispose();
		}
//		terrain.dispose();
//		terrain1.dispose();
		
		trianglePass.dispose();
		rectanglePass.dispose();
		texturePass.dispose();
		transformationPass.dispose();
		lightSourcePass.dispose();

		cottage.dispose();
		model.dispose();
		
		player.dispose();
		
		compass.dispose();
		trees.dispose();
		tree_1.dispose();
		
		cubes.dispose();
	}

}
