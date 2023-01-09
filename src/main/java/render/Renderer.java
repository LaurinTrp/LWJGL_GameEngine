package main.java.render;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
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
import main.java.render.passes.framebuffers.Framebuffer;
import main.java.render.passes.lighting.LightSourcePass;
import main.java.render.passes.lighting.SunPass;
import main.java.render.passes.standard.RectanglePass;
import main.java.render.passes.standard.TrianglePass;
import main.java.render.passes.texture.TexturePass;
import main.java.render.passes.transformation.Compass;
import main.java.render.passes.trees.Tree_1;
import main.java.render.passes.trees.Trees;
import main.java.render.utilities.TerrainGenerator;
import main.java.utils.math.MousePicker;

public class Renderer {

	public static IRenderObject framebuffer;

	private IRenderObject trianglePass;

	private IRenderObject rectanglePass;
	private IRenderObject texturePass;

	private IRenderObject lightSourcePass;

	private IRenderObject cottage;

	private IRenderObject compass;

	public static List<IRenderObject> terrains;

	private IRenderObject player;

	private IRenderObject trees;

	private IRenderObject tree_1;

	private IRenderObject cubes;

	private IRenderObject terrainModel;
	
	public static SunPass sun;

	public static Camera camera;

	public static ArrayList<Vec4> lightSourcePositions = new ArrayList<>();

	private MousePicker mousePicker;

	public Renderer() {

		framebuffer = new Framebuffer();

		trianglePass = new TrianglePass();

		rectanglePass = new RectanglePass();
		texturePass = new TexturePass();

		lightSourcePositions.add(new Vec4(-1.2f, 1.0f, 2.0f, 1.0f));
		lightSourcePositions.add(new Vec4(-1.2f, 1.0f, 2.0f, 1.0f));

		lightSourcePass = new LightSourcePass();
		((LightSourcePass) lightSourcePass).setLightsourcePositions(lightSourcePositions);

		sun = new SunPass();

		cottage = new Cottage();

		compass = new Compass();

		player = new Player();

		terrains = new ArrayList<>();

		terrainModel = new TerrainModel(new TerrainGenerator(100, 100, 1, 0, 0), "Terrain/Terrain.png");
		terrains.add(terrainModel);

		camera = new Camera(player);
		mousePicker = new MousePicker(camera);

		trees = new Trees();
		tree_1 = new Tree_1();

		cubes = new Cubes();
	}

	/**
	 * Main render method
	 */
	public void render() {
		framebuffer.render();

		glEnable(GL_DEPTH_TEST);

		terrainModel.render();

		cottage.render();

		glEnable(GL_CULL_FACE);
		
		player.render();
		
		glDisable(GL_CULL_FACE);
		
		camera.setFocusPoint(new Vec3(((Player) player).getPosition()).add(((Player) player).getPlayerFront()));
		camera.moveCamera();

		glDisable(GL_DEPTH_TEST);

		((Framebuffer) framebuffer).renderColorAttachments();

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
		texturePass.dispose();
		lightSourcePass.dispose();

		cottage.dispose();

		player.dispose();

		compass.dispose();
		trees.dispose();
		tree_1.dispose();

		cubes.dispose();
	}

}
