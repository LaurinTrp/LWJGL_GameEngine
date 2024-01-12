package main.java.render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import lwjgui.scene.Context;
import main.java.gui.Engine_Main;
import main.java.render.camera.Camera;
import main.java.render.entities.Player;
import main.java.render.entities.trees.Tree_1;
import main.java.render.entities.trees.Tree_2;
import main.java.render.model.ModelObserver;
import main.java.render.model.MultiTextureTerrain;
import main.java.render.model.RandomMatrixGenerator;
import main.java.render.passes.Cottage;
import main.java.render.passes.Cube;
import main.java.render.passes.TerrainModel;
import main.java.render.passes.TrianglePass;
import main.java.render.passes.framebuffers.DepthMap;
import main.java.render.passes.framebuffers.Framebuffer;
import main.java.render.passes.framebuffers.IFramebuffer;
import main.java.render.passes.framebuffers.ObjectPickBuffer;
import main.java.render.passes.lighting.Lightsources;
import main.java.render.passes.lighting.SunPass;
import main.java.render.passes.skybox.Skybox;
import main.java.render.passes.transformation.Compass;
import main.java.render.renderobject.IRenderObject;
import main.java.render.utils.TexturePack;
import main.java.render.utils.terrain.TerrainGenerator;

public class Renderer implements lwjgui.gl.Renderer {

	public static ModelObserver modelObserver;

	public static IFramebuffer framebuffer;
	public static IFramebuffer objectPickBuffer;
	public IFramebuffer depthBuffer;

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
	public static ArrayList<Mat4> lightSourcePositionsMats = new ArrayList<>();

	private Skybox skybox;
	
	TrianglePass tp;
	
	Cube cube;

	public Renderer() {
		modelObserver = new ModelObserver();

		skybox = createSkybox();

		framebuffer = new Framebuffer();
		objectPickBuffer = new ObjectPickBuffer();
		depthBuffer = new DepthMap();

		initLightSourcePositions();

		lightSourcePass = new Lightsources(lightSourcePositionsMats.toArray(new Mat4[] {}));

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
		
		tp = new TrianglePass();
		
		cube = new Cube();
		
	}

	private void initLightSourcePositions() {

		lightSourcePositions.add(new Vec4(-1.2f, 1.0f, 2.0f, 1.0f));
		lightSourcePositions.add(new Vec4(1.2f, 1.0f, 2.0f, 1.0f));

		for (Vec4 vec4 : lightSourcePositions) {
			Mat4 mat4 = new Mat4(1.0f);
			mat4.translate(vec4.toVec3_());
			lightSourcePositionsMats.add(mat4);
		}
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
	@Override
	public void render(Context context) {
		
		framebuffer.render();
		objectPickBuffer.render();
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		sun.update();
		skybox.render();
		
		glEnable(GL_DEPTH_TEST);
		
//		lightSourcePass.render();
		
		renderModels();
//
		camera.setFocusPoint(new Vec3(player.getPosition()));
		camera.update();

		glDisable(GL_DEPTH_TEST);

		objectPickBuffer.renderColorAttachments();
		framebuffer.renderColorAttachments();

		framebuffer.unbindFbo();
		objectPickBuffer.unbindFbo();
	}

	public void renderModels() {
		glEnable(GL_CULL_FACE);

		cube.render();
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
		
		cottage.render();
		tree_1.render();
		
		glDisable(GL_CULL_FACE);

	}

	/**
	 * Main method to dispose all OpenGL data
	 */
	public void dispose() {

		framebuffer.dispose();
		objectPickBuffer.dispose();
		skybox.dispose();

		terrainModel.dispose();

		lightSourcePass.dispose();

		cottage.dispose();

		player.dispose();

		compass.dispose();
		tree_1.dispose();
		tree_2.dispose();
		
		tp.dispose();
	}

}
