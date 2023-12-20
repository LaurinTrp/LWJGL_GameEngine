package main.java.render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import main.java.render.camera.Camera;
import main.java.render.entities.Player;
import main.java.render.entities.Test;
import main.java.render.entities.trees.Tree_1;
import main.java.render.entities.trees.Tree_2;
import main.java.render.model.ModelObserver;
import main.java.render.model.MultiTextureTerrain;
import main.java.render.model.RandomMatrixGenerator;
import main.java.render.passes.Cottage;
import main.java.render.passes.TerrainModel;
import main.java.render.passes.framebuffers.DepthMap;
import main.java.render.passes.framebuffers.Framebuffer;
import main.java.render.passes.framebuffers.IFramebuffer;
import main.java.render.passes.framebuffers.ObjectPickBuffer;
import main.java.render.passes.lighting.LightSourcePass;
import main.java.render.passes.lighting.SunPass;
import main.java.render.passes.skybox.Skybox;
import main.java.render.passes.standard.RectanglePass;
import main.java.render.passes.standard.TrianglePass;
import main.java.render.passes.transformation.Compass;
import main.java.render.renderobject.IRenderObject;
import main.java.render.utils.TexturePack;
import main.java.render.utils.terrain.TerrainGenerator;
import main.java.utils.math.MousePicker;

public class Renderer {

	public static ModelObserver modelObserver;
	
	public static IFramebuffer framebuffer;
	public static IFramebuffer objectPickBuffer;
	public IFramebuffer depthBuffer;

	private IRenderObject trianglePass;

	private IRenderObject rectanglePass;

	private IRenderObject lightSourcePass;

	private IRenderObject cottage;

	private IRenderObject compass;

	private Player player;

	private IRenderObject tree_1;
	private IRenderObject tree_2;

	private MultiTextureTerrain terrainModel;

	public static SunPass sun;

	public static Camera camera;

	public static ArrayList<Vec4> lightSourcePositions = new ArrayList<>();

	private Skybox skybox;

	public Renderer() {
		modelObserver = new ModelObserver();
		
		skybox = createSkybox();

		framebuffer = new Framebuffer();
		objectPickBuffer = new ObjectPickBuffer();
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

		generateFirstTerrain();

		tree_1 = new Tree_1(RandomMatrixGenerator.generateRandomWithHeight(1, new Vec2(-50f, 50f), new Vec2(-50f, 50f),
				new Vec2(3f, 6f)));
		tree_2 = new Tree_2(RandomMatrixGenerator.generateRandomWithHeight(20, new Vec2(-50f, 50f), new Vec2(-50f, 50f),
				new Vec2(3f, 6f)));

		player.addIntersector(cottage);
		player.addIntersector(tree_1);
		
	}

	private Skybox createSkybox() {
		String[] paths = new String[] { "skybox/right.png", "skybox/left.png", "skybox/top.png", "skybox/bottom.png",
				"skybox/front.png", "skybox/back.png", };
		return new Skybox(paths);
	}

	private void generateFirstTerrain() {
		TerrainGenerator generator = new TerrainGenerator(200, 1f, -100, -100);
		generator.generateProcedural();
		terrainModel = new TerrainModel(generator, TexturePack.DEFAULT_TERRAIN);
	}

	/**
	 * Main render method
	 */
	public void render() {
		framebuffer.render();
		objectPickBuffer.render();

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		sun.update();
		skybox.render();
		glEnable(GL_DEPTH_TEST);
		
		renderModels();

		camera.setFocusPoint(new Vec3(player.getPosition()));
		camera.moveCamera();
		
		glDisable(GL_DEPTH_TEST);
//		compass.render();
		objectPickBuffer.renderColorAttachments();
		framebuffer.renderColorAttachments();

		framebuffer.unbindFbo();
		objectPickBuffer.unbindFbo();
	}

	public void renderModels() {
		glEnable(GL_CULL_FACE);

//		test.render();

//		terrainModel.render();
//		lightSourcePass.render();

//		System.out.println("PLAYER POSITION: " + ((Player)player).getPosition());

		if (player.checkMovement()) {
			Vec2 playerPosXZ = new Vec2(player.getPlayerPosXZ());
			terrainModel.updateHeightMap(playerPosXZ.x, playerPosXZ.y);
			player.move();
			terrainModel.translate(new Vec3(playerPosXZ.x, 0, playerPosXZ.y));
		}
		
		terrainModel.render();
		player.gravity(terrainModel);
		player.render();
		
		glDisable(GL_CULL_FACE);

		cottage.render();
		tree_1.render();
	}

	/**
	 * Main method to dispose all OpenGL data
	 */
	public void dispose() {

		framebuffer.dispose();
		objectPickBuffer.dispose();
		skybox.dispose();

		terrainModel.dispose();

		trianglePass.dispose();
		rectanglePass.dispose();
		lightSourcePass.dispose();

		cottage.dispose();

		player.dispose();

		compass.dispose();
		tree_1.dispose();
		tree_2.dispose();
	}

}
