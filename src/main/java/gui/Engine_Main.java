package main.java.gui;

import static org.lwjgl.glfw.GLFW.GLFW_BLUE_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_GREEN_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
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
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import elements.Button;
import elements.Slider;
import lwjgui.LWJGUI;
import lwjgui.LWJGUIApplication;
import lwjgui.LWJGUIUtil;
import lwjgui.paint.Color;
import lwjgui.scene.Scene;
import lwjgui.scene.Window;
import lwjgui.scene.layout.StackPane;
import lwjgui.scene.layout.floating.FloatingPane;
import lwjgui.theme.Theme;
import lwjgui.theme.ThemeDark;
import main.java.render.Renderer;
import main.java.utils.Inputs.KeyHandler;
import main.java.utils.Inputs.MouseInputs;
import main.java.utils.loaders.ImageLoader;
import openglPane.OpenGLPane;


public class Engine_Main extends LWJGUIApplication {

	public static float x, y;
	public static int windowWidth = 800, windowHeight = 800;

	public static MouseInputs mouseHandler;
	public static KeyHandler keyHandler;

	public static Renderer render;
	
	private static Window lwjguiWindow;

	private static long window = -1;

	public static void main(String[] args) throws IOException {
		
		initWindow();

		initObjects();

		initCallbacks();

		loop();

		render.dispose();

		glfwDestroyWindow(window);
		glfwTerminate();
	}
	
	private static void loadWindowIcon() throws IOException {

		BufferedImage buffImage = ImageIO.read(new File("/media/laurin/Festplatte/Programmieren/Java/3D-Workbench/LWJGL_GameEngineResource/src/resources/Textures/wall.jpg"));
		ByteBuffer imageData = ImageLoader.bufferedImageToByteBuffer(buffImage);
		
		GLFWImage.Buffer icons = GLFWImage.malloc(1);
		GLFWImage icon = GLFWImage.malloc();
		icon.set(buffImage.getWidth(), buffImage.getHeight(), imageData);
		icons.put(icon);
		icons.flip();
		
		glfwSetWindowIcon(window, icons);
		
		icon.free();
		icons.free();
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
		window = LWJGUIUtil.createOpenGLCoreWindow("Hello World", windowWidth, windowHeight, true, false);

		lwjguiWindow = LWJGUI.initialize(window);

		render = new Renderer();
		addComponents(lwjguiWindow.getScene());
		
		lwjguiWindow.show();
		
		lwjguiWindow.setIcon("png", new File[] {
			new File("/media/laurin/Festplatte/Programmieren/Java/3D-Workbench/LWJGL_GameEngineResource/src/resources/Textures/Warn.png")
		});
		
		// ####### Disable cursor
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}


	private static void addComponents(Scene scene) {
		Theme.setTheme(new ThemeDark());
		
		OpenGLPane pane = new OpenGLPane(lwjguiWindow);
		pane.setRendererCallback(render);
		
//		Button button = new Button("Test");
//		button.setAbsolutePosition(100, 100);
//		button.setPrefSize(100, 50);
//		
//		pane.getChildren().add(button.getInPane());
//		
//		Slider slider = new Slider();
//		slider.setPrefSize(300, 100);
//		slider.setAbsolutePosition(windowWidth / 2d - slider.getPrefWidth() / 2d, 0);
		
//		pane.getChildren().add(slider.getInPane());
		
		scene.setRoot(pane);
	}
	
	public static void makeContextCurrent() {
		glfwMakeContextCurrent(window);
	}

	public static void createCapabilities() {
		GL.createCapabilities();
	}

	private static void initObjects() {
		mouseHandler = new MouseInputs();
		keyHandler = new KeyHandler(window);
	}

	private static void initCallbacks() {
		glfwSetCursorPosCallback(window, mouseHandler.getCursorPosCallback());
		glfwSetScrollCallback(window, mouseHandler.getScrollCallback());
		glfwSetMouseButtonCallback(window, mouseHandler.getMouseButtonCallback());
	}

	private static void loop() {
		glEnable(GL_DEPTH_TEST);

		while (!glfwWindowShouldClose(window)) {

			if (keyHandler.isPressed(GLFW_KEY_ESCAPE)) {
				glfwSetWindowShouldClose(window, true);
				continue;
			}

			// RENDER //
			LWJGUI.render();

			mouseHandler.reset();
		}
	}

	@Override
	public void start(String[] args, lwjgui.scene.Window window) {}
}