package main.java.gui;

import static org.lwjgl.glfw.GLFW.GLFW_BLUE_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_GREEN_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RED_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_REFRESH_RATE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.IOException;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import main.java.render.Renderer;
import main.java.utils.Inputs.KeyHandler;
import main.java.utils.Inputs.MouseInputs;
import main.java.utils.OS_Specifics;
import main.java.utils.TestClass;

public class Engine_Main {

	public static float x, y;
	public static int windowWidth = 800, windowHeight = 800;
	
	public static MouseInputs mouseHandler;
	public static KeyHandler keyHandler;
	
	private static Renderer render;
	
	private static long window = -1;
	
	public static void main(String[] args) throws IOException {

//		new TestClass().start();
		
		initWindow();
		
		initObjects();
		
		initCallbacks();

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
		}catch (IndexOutOfBoundsException e) {}
		GLFWVidMode mode = glfwGetVideoMode(monitor);
		 
		glfwWindowHint(GLFW_RED_BITS, mode.redBits());
		glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
		glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
		glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate());
		 
		window = glfwCreateWindow(mode.width(), mode.height(), "Game", monitor, 0);
		windowWidth = mode.width();
		windowHeight = mode.height();

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);  
		
		glfwShowWindow(window);
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

			if(keyHandler.isPressed(GLFW_KEY_ESCAPE)) {
				glfwSetWindowShouldClose(window, true);
			}

			glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			// RENDER //
			render.render();

			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			glfwSwapBuffers(window);
			glfwPollEvents();
			
		}
	}
}