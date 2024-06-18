package main.java.render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;

import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;
import lwjgui.scene.Context;
import main.java.camera.Camera;
import main.java.data.LightSourceData;
import main.java.data.LightType;
import main.java.data.SunData;
import main.java.gui.Engine_Main;
import main.java.model.TerrainModel;
import main.java.render.assimpModels.Cottage;
import main.java.render.assimpModels.Cube;
import main.java.render.assimpModels.Player;
import main.java.render.assimpModels.trees.Tree_1;
import main.java.render.assimpModels.trees.Tree_2;
import main.java.render.passes.Compass;
import main.java.render.passes.MultiTextureTerrain;
import main.java.render.passes.TrianglePass;
import main.java.render.passes.framebuffers.DepthMap;
import main.java.render.passes.framebuffers.Framebuffer;
import main.java.render.passes.framebuffers.IFramebuffer;
import main.java.render.passes.framebuffers.ObjectPickBuffer;
import main.java.render.passes.lighting.Lightsources;
import main.java.render.passes.skybox.Skybox;
import main.java.render.renderobject.IRenderObject;
import main.java.render.utils.TexturePack;
import main.java.utils.math.RandomMatrixGenerator;
import main.java.utils.model.ModelObserver;
import main.java.utils.terrain.TerrainGenerator;
import resources.ResourceLoader;

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

	public static SunData sun;

	public static Camera camera;

	public static ArrayList<Vec3> lightSourcePositions = new ArrayList<>();
	public static ArrayList<Mat4> lightSourcePositionsMats = new ArrayList<>();

	private Skybox skybox;
	
//	private Mesh meshCube;
	
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

		sun = new SunData();

		cottage = new Cottage();

		compass = new Compass();

		player = new Player();

//		camera = new Camera(player);
		camera = player.getCamera();

		generateFirstTerrain();

		tree_1 = new Tree_1(RandomMatrixGenerator.generateRandomWithHeight(10, new Vec2(-50f, 50f), new Vec2(-50f, 50f),
				new Vec2(3f, 6f)));
		tree_2 = new Tree_2(RandomMatrixGenerator.generateRandomWithHeight(20, new Vec2(-50f, 50f), new Vec2(-50f, 50f),
				new Vec2(3f, 6f)));

		player.addIntersector(cottage);
		player.addIntersector(tree_1);
		
		tp = new TrianglePass();
		
		cube = new Cube();
		
//		meshCube = new Mesh(Shapes.SimpleCube.vertices, Shapes.SimpleCube.texCoords, Shapes.SimpleCube.normals, Shapes.SimpleCube.indices);
		
		
		ResourceLoader.clear();
	}

	private void initLightSourcePositions() {
		LightSourceData lightSource = new LightSourceData.SpotLight(new Vec3(0, 4, -2), new Vec3(0, 0, 1), new Vec3(1f), new Vec3(1f), new Vec3(1f), 12.5f, 17.5f);
		Engine_Main.lightManager.addLight("TEST", lightSource);
		
//		LightSourceData lightSource2 = new LightSourceData.PointLightData(new Vec3(-2, 5, 10), new Vec3(-1, 0, 0), new Vec3(0.2f), new Vec3(0.5f), new Vec3(1f), 1.0f, 0.7f, 1.8f);
//		Engine_Main.lightManager.addLight("TEST2", lightSource2);

		lightSourcePositions.add(lightSource.getPosition());
//		lightSourcePositions.add(lightSource2.getPosition());
		for (Vec3 vec3 : lightSourcePositions) {
			lightSourcePositionsMats.add(new Mat4().translate(vec3));
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
		
//		sun.update();
		skybox.render();
		
		glEnable(GL_DEPTH_TEST);
		
		lightSourcePass.render();
		
		renderModels();
//
//		camera.setFocusPoint(new Vec3(player.getFocusPoint()));
		camera.update();

		glDisable(GL_DEPTH_TEST);

		objectPickBuffer.renderColorAttachments();
		framebuffer.renderColorAttachments();
		
		framebuffer.unbindFbo();
		objectPickBuffer.unbindFbo();
	}

	public void renderModels() {
		cube.render();
//		test.render();

//		terrainModel.render();
		lightSourcePass.render();

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
		
//		meshCube.render();
		
		cottage.render();
//		tree_1.render();
		
//		glDisable(GL_CULL_FACE);

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
		
//		meshCube.dispose();
	}

}
