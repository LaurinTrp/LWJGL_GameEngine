package main.java.gui;

import static org.lwjgl.glfw.GLFW.GLFW_BLUE_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_GREEN_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_RED_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_REFRESH_RATE;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.File;
import java.io.IOException;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import lwjgui.LWJGUI;
import lwjgui.LWJGUIApplication;
import lwjgui.LWJGUIUtil;
import lwjgui.scene.Scene;
import lwjgui.scene.Window;
import lwjgui.theme.Theme;
import main.SoundTest;
import main.java.data.light.LightManager;
import main.java.data.sounds.AudioData;
import main.java.data.sounds.SoundListener;
import main.java.data.sounds.SoundManager;
import main.java.data.sounds.SoundSource;
import main.java.render.Renderer;
import main.java.utils.Inputs.KeyHandler;
import main.java.utils.Inputs.MouseInputs;
import resources.ResourceLoader;

public class Engine_Main extends LWJGUIApplication {

	public static float x, y;
	public static int windowWidth = 800, windowHeight = 800;

	public static MouseInputs mouseHandler;
	
	public static KeyHandler keyHandler;
	
	public static Renderer render;
	public static SoundManager soundManager;
	public static LightManager lightManager;

	public static Window lwjguiWindow;

	private static long window = -1;

	public static PaneObserver paneObserver;

	public static void main(String[] args) throws IOException {

		initWindow();

		initObjects();

		updateCallbacks();

		lwjguiWindow.show();
		updatePane();

		loop();

		render.dispose();

		glfwDestroyWindow(window);
		glfwTerminate();
	}

	private static void initWindow() {
		if (!glfwInit()) {
			System.out.println("GLFW not initialized");
			return;
		}

		glfwDefaultWindowHints();

		PointerBuffer monitors = glfwGetMonitors();
		long monitor = 0;
		try {
			monitor = monitors.get(0);
		} catch (IndexOutOfBoundsException e) {
		}
		GLFWVidMode mode = glfwGetVideoMode(monitor);

		glfwWindowHint(GLFW_RED_BITS, mode.redBits());
		glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
		glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
		glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate());

		windowWidth = mode.width();
		windowHeight = mode.height();
		window = LWJGUIUtil.createOpenGLCoreWindow("Game", windowWidth, windowHeight, true, false);

		lwjguiWindow = LWJGUI.initialize(window);
		lwjguiWindow.setResizible(false);
		
		File f = ResourceLoader.getFile("Textures", "flatearth.png");
		lwjguiWindow.setIcon("png", new File[] { f });

	}

	private static void updatePane() {
		Scene scene = new Scene(paneObserver.getCurrentPane());
		updateCallbacks();
		updateCursorVisibility();
		
		if(keyHandler != null) {
			keyHandler.reset();
		}
		if(mouseHandler != null) {
			mouseHandler.reset();
		}
		
		paneObserver.getCurrentPane().setPrefSize(windowWidth, windowHeight);
		
		Theme.setTheme(paneObserver.getCurrentPane().getCurrentTheme());
		
		PaneObserver.paneChanged = false;
		
		lwjguiWindow.setScene(scene);
	}

	public static void makeContextCurrent() {
		glfwMakeContextCurrent(window);
	}

	public static void createCapabilities() {
		GL.createCapabilities();
	}

	private static void initObjects() {

		soundManager = new SoundManager();
		soundManager.init();
		
		lightManager = new LightManager();
		
		keyHandler = new KeyHandler();
		mouseHandler = new MouseInputs();
		render = new Renderer();
		paneObserver = new PaneObserver();
	}
	
	private static void updateCallbacks() {
		boolean isStartPane = paneObserver.getCurrentPane() == PaneObserver.startPane;
		glfwSetCursorPosCallback(window, isStartPane ? null : mouseHandler.getCursorPosCallback());
		glfwSetScrollCallback(window, isStartPane ? null : mouseHandler.getScrollCallback());
	}

	private static void updateCursorVisibility() {
		glfwSetInputMode(window, GLFW_CURSOR, paneObserver.getCurrentPane().isCursorVisible() ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
	}
	
	private static void loop() {
		glEnable(GL_DEPTH_TEST);

		while (!glfwWindowShouldClose(window)) {

			if (PaneObserver.paneChanged) {
				updatePane();
			}

			// RENDER //
			LWJGUI.render();
			
			soundManager.updateListenerPosition(Renderer.camera);

			if(mouseHandler != null) {
				mouseHandler.reset();
			}
		}
	}

	@Override
	public void start(String[] args, Window window) {
		
	}
}