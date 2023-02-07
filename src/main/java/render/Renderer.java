package main.java.render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.render.model.Model;
import main.java.render.passes.Cottage;
import main.java.render.passes.Cubes;
import main.java.render.passes.Player;
import main.java.render.passes.TerrainModel;
import main.java.render.passes.framebuffers.DepthMap;
import main.java.render.passes.framebuffers.Framebuffer;
import main.java.render.passes.framebuffers.IFramebuffer;
import main.java.render.passes.lighting.LightSourcePass;
import main.java.render.passes.lighting.SunPass;
import main.java.render.passes.standard.RectanglePass;
import main.java.render.passes.standard.TrianglePass;
import main.java.render.passes.transformation.Compass;
import main.java.render.passes.trees.Tree_1;
import main.java.render.utilities.TerrainGenerator;
import main.java.utils.Shapes.Cube;
import main.java.utils.math.MousePicker;

public class Renderer {

	public static IFramebuffer framebuffer;
	public IFramebuffer depthBuffer;

	private IRenderObject trianglePass;

	private IRenderObject rectanglePass;

	private IRenderObject lightSourcePass;

	private IRenderObject cottage;

	private IRenderObject compass;

	public static List<IRenderObject> terrains;

	private IRenderObject player;

	private IRenderObject tree_1;

	private IRenderObject cubes;
	private IRenderObject cube;

	private IRenderObject terrainModel;
	
	public static SunPass sun;

	public static Camera camera;

	public static ArrayList<Vec4> lightSourcePositions = new ArrayList<>();

	private MousePicker mousePicker;

	public Renderer() {

		framebuffer = new Framebuffer();
		depthBuffer = new DepthMap();

		trianglePass = new TrianglePass();

		rectanglePass = new RectanglePass();

		lightSourcePositions.add(new Vec4(-1.2f, 1.0f, 2.0f, 1.0f));
		lightSourcePositions.add(new Vec4(1.2f, 1.0f, 2.0f, 1.0f));

		lightSourcePass = new LightSourcePass();
		((LightSourcePass) lightSourcePass).setLightsourcePositions(lightSourcePositions);

		sun = new SunPass();

		cottage = new Cottage();

		compass = new Compass();

		player = new Player();

		terrains = new ArrayList<>();

		terrainModel = new TerrainModel(new TerrainGenerator(10, 10, 1, 0, 0), "Terrain/Terrain.png");
		terrains.add(terrainModel);

		camera = new Camera(player);
		mousePicker = new MousePicker(camera);

		tree_1 = new Tree_1();

		cubes = new Cubes();
		cube = new main.java.render.passes.Cube();
		
		((Player)player).addIntersector((Model)cottage);
		((Player)player).addIntersector((Model)tree_1);
	}

	/**
	 * Main render method
	 */
	public void render() {
		framebuffer.render();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_DEPTH_TEST);
		renderScene();
		
		camera.setFocusPoint(new Vec3(((Player) player).getPosition()).add(((Player) player).getPlayerFront()));
		camera.moveCamera();

		sun.update();
		
		glDisable(GL_DEPTH_TEST);
		((Framebuffer) framebuffer).renderColorAttachments();

	}
	
	
	public void renderScene() {
		glEnable(GL_CULL_FACE);
		terrainModel.render();
		lightSourcePass.render();

//		cottage.render();
//		tree_1.render();
		player.render();
		
		cube.render();
		
		compass.render();
		glDisable(GL_CULL_FACE);
	}

	/**
	 * Main method to dispose all OpenGL data
	 */
	public void dispose() {

		framebuffer.dispose();

		for (IRenderObject terrainPass : terrains) {
			terrainPass.dispose();
		}

		trianglePass.dispose();
		rectanglePass.dispose();
		lightSourcePass.dispose();

		cottage.dispose();

		player.dispose();

		compass.dispose();
			tree_1.dispose();

		cubes.dispose();
	}

}
