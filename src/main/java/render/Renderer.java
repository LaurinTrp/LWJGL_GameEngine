package main.java.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.render.entities.Player;
import main.java.render.passes.Cottage;
import main.java.render.passes.Cube;
import main.java.render.passes.Cubes;
import main.java.render.passes.TerrainModel;
import main.java.render.passes.framebuffers.DepthMap;
import main.java.render.passes.framebuffers.Framebuffer;
import main.java.render.passes.framebuffers.IFramebuffer;
import main.java.render.passes.lighting.LightSourcePass;
import main.java.render.passes.lighting.SunPass;
import main.java.render.passes.skybox.Skybox;
import main.java.render.passes.standard.RectanglePass;
import main.java.render.passes.standard.TrianglePass;
import main.java.render.passes.transformation.Compass;
import main.java.render.passes.trees.Tree_1;
import main.java.render.utilities.TexturePack;
import main.java.render.utilities.terrain.ProceduralTerrain;
import main.java.render.utilities.terrain.TerrainGenerator;
import main.java.utils.math.MousePicker;

import static org.lwjgl.opengl.GL11.*;

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

	private ProceduralTerrain proceduralTerrain;
	
	private IRenderObject terrainModel;
	
	public static SunPass sun;

	public static Camera camera;

	public static ArrayList<Vec4> lightSourcePositions = new ArrayList<>();

	private MousePicker mousePicker;
	
	private Skybox skybox;

	public Renderer() {
		skybox = createSkybox();
		terrains = Collections.synchronizedList(new ArrayList<IRenderObject>());
		
		proceduralTerrain = new ProceduralTerrain(terrains);
		
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

		
		TexturePack tp = new TexturePack("Terrain/BlendMap.png", "Terrain/Grass.png", "Terrain/Rocks.png", "Terrain/Mushroom.png", "Terrain/Flowers.png");
		terrainModel = new TerrainModel(new TerrainGenerator(64, 2, -32, -32), tp);
		terrains.add(terrainModel);

		camera = new Camera(player);
		mousePicker = new MousePicker(camera);

		tree_1 = new Tree_1();

		cubes = new Cubes();
		cube = new Cube();
		
//		((Player) player).addIntersector(cottage);
//		((Player) player).addIntersector(tree_1);
	}

	private Skybox createSkybox() {
		String[] paths = new String[] {
				"skybox/right.png",
				"skybox/left.png",
				"skybox/top.png",
				"skybox/bottom.png",
				"skybox/front.png",
				"skybox/back.png",
		};
		return new Skybox(paths);
	}
	
	/**
	 * Main render method
	 */
	public void render() {
		framebuffer.render();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		skybox.render();
		glEnable(GL_DEPTH_TEST);
		renderScene();
		
		camera.setFocusPoint(new Vec3(((Player) player).getPosition()).add(((Player) player).getPlayerFront()));
		camera.moveCamera();
//
		sun.update();
		
		glDisable(GL_DEPTH_TEST);
		((Framebuffer) framebuffer).renderColorAttachments();

	}
	
	
	public void renderScene() {
		
		glEnable(GL_CULL_FACE);
		
		proceduralTerrain.update(camera);
		
		for (IRenderObject terrain : terrains) {
			terrain.render();
		}
		
//		terrainModel.render();
//		lightSourcePass.render();

//		cottage.render();
//		tree_1.render();
		player.render();
		
//		cube.render();
		
		compass.render();
		glDisable(GL_CULL_FACE);
	}

	/**
	 * Main method to dispose all OpenGL data
	 */
	public void dispose() {

		framebuffer.dispose();
		skybox.dispose();
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
