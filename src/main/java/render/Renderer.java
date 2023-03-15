package main.java.render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.render.entities.Player;
import main.java.render.entities.Test;
import main.java.render.passes.Cottage;
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

	private IRenderObject terrainModel;

	public static SunPass sun;

	public static Camera camera;

	public static ArrayList<Vec4> lightSourcePositions = new ArrayList<>();

	private MousePicker mousePicker;

	private Skybox skybox;

	private Test test;

	public Renderer() {
		skybox = createSkybox();
		terrains = Collections.synchronizedList(new ArrayList<IRenderObject>());

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

		camera = new Camera(player);
		mousePicker = new MousePicker(camera);

		generateFirstTerrain();

		tree_1 = new Tree_1();

		test = new Test();
//		((Player) player).addIntersector(cottage);
//		((Player) player).addIntersector(tree_1);
	}

	private Skybox createSkybox() {
		String[] paths = new String[] { "skybox/right.png", "skybox/left.png", "skybox/top.png", "skybox/bottom.png",
				"skybox/front.png", "skybox/back.png", };
		return new Skybox(paths);
	}

	private void generateFirstTerrain() {
		TerrainGenerator generator = new TerrainGenerator(64, 1, 0, 0);
		generator.generateProcedural();
		IRenderObject terrainModel = new TerrainModel(generator, TexturePack.DEFAULT_TERRAIN);
		terrains.add(terrainModel);
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

		camera.setFocusPoint(new Vec3(((Player) player).getPosition()));
		camera.moveCamera();
//
		sun.update();

		glDisable(GL_DEPTH_TEST);
		((Framebuffer) framebuffer).renderColorAttachments();

	}

	public void renderScene() {

		glEnable(GL_CULL_FACE);

		for (IRenderObject terrain : terrains) {
			terrain.render();
		}

		test.render();

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

		test.dispose();
	}

}
