package main.java.gui;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_GREEN_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RED_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_REFRESH_RATE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
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

import main.java.render.Renderer;
import main.java.utils.Inputs.KeyHandler;
import main.java.utils.Inputs.MouseInputs;
import main.java.utils.loaders.ImageLoader;

public class Engine_Main {

	public static float x, y;
	public static int windowWidth = 800, windowHeight = 800;

	public static MouseInputs mouseHandler;
	public static KeyHandler keyHandler;

	public static Renderer render;

	private static long window = -1;

	private static long currentFrameTime;
	private static long lastFrameTime;
	private static long delta;

	private static double old_time, new_time;

	public static void main(String[] args) throws IOException {

		initWindow();

		initObjects();

		initCallbacks();

		lastFrameTime = System.nanoTime();

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

		window = glfwCreateWindow(mode.width(), mode.height(), "Game", monitor, 0);
		windowWidth = mode.width();
		windowHeight = mode.height();

		makeContextCurrent();
		glfwSwapInterval(1);

		// ####### Disable cursor
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		glfwShowWindow(window);
		createCapabilities();
		

//		try {
//			loadWindowIcon();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
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
		render = new Renderer();
	}

	private static void initCallbacks() {
		glfwSetCursorPosCallback(window, mouseHandler.getCursorPosCallback());
		glfwSetScrollCallback(window, mouseHandler.getScrollCallback());
		glfwSetMouseButtonCallback(window, mouseHandler.getMouseButtonCallback());
	}

	private static void loop() {
		glEnable(GL_DEPTH_TEST);

		while (!glfwWindowShouldClose(window)) {

//			FPScounter.StartCounter();
			
			if (keyHandler.isPressed(GLFW_KEY_ESCAPE)) {
				glfwSetWindowShouldClose(window, true);
				continue;
			}

			// RENDER //
			render.render();

			mouseHandler.reset();

			glfwSwapBuffers(window);
			glfwPollEvents();

			currentFrameTime = System.nanoTime();
			delta = currentFrameTime - lastFrameTime;
			lastFrameTime = currentFrameTime;

//			FPScounter.StopAndPost();
		}
		render.dispose();
	}

	public static long getDelta() {
		return delta;
	}
}