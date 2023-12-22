package main.java.utils;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Inputs {
	public static class MouseInputs {

		private double lastX = 400, lastY = 400;
		private double xoffset, yoffset;

		private double mouseX, mouseY;

		private float scrollY;

		private boolean LMB_Down = false;
		private boolean LMB_Ready = false;
		private boolean RMB_Down = false;
		private boolean RMB_Ready = false;

		GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {

				xoffset = xpos - lastX;
				yoffset = lastY - ypos; // reversed since y-coordinates range from bottom to top
				lastX = xpos;
				lastY = ypos;


				if(Math.abs(xoffset) <= 1) {
					xoffset = 0;
				}
				if(Math.abs(yoffset) <= 1) {
					yoffset = 0;
				}

				float sensitivity = 0.2f;
				xoffset *= sensitivity;
				yoffset *= sensitivity;

				mouseX = xpos;
				mouseY = ypos;

			}
		};

		public GLFWCursorPosCallback getCursorPosCallback() {
			return cursorPosCallback;
		}

		GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				scrollY = (float) yoffset;
			}
		};

		public GLFWScrollCallback getScrollCallback() {
			return scrollCallback;
		}

		GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {

			@Override
			public void invoke(long window, int button, int action, int mods) {
				if(button == GLFW.GLFW_MOUSE_BUTTON_1) {
					LMB_Down = action == GLFW.GLFW_PRESS;
				}
				if(button == GLFW.GLFW_MOUSE_BUTTON_2) {
					RMB_Down = action == GLFW.GLFW_PRESS;
				}
			}
		};

		public void reset() {
			xoffset = 0;
			yoffset = 0;
		}

		public GLFWMouseButtonCallback getMouseButtonCallback() {
			return mouseButtonCallback;
		}

		public double getXoffset() {
			return xoffset;
		}

		public double getYoffset() {
			return yoffset;
		}

		public float getScrollY() {
	        float value = scrollY;
	        scrollY = 0.0f;
	        return value;
	    }


		public double getMouseX() {
			return mouseX;
		}
		public double getMouseY() {
			return mouseY;
		}

		public boolean isLMB_Down() {
			return LMB_Down;
		}

		public boolean isRMB_Down() {
			return RMB_Down;
		}
		
		public boolean isLMB_Ready() {
			return LMB_Ready;
		}
		
		public boolean isRMB_Ready() {
			return RMB_Ready;
		}

	}

	public static class KeyHandler {
		private long window;
		public KeyHandler(long window) {
			this.window = window;
		}

		public boolean isPressed(int key) {
			return glfwGetKey(window, key) == GLFW_PRESS;
		}
		
		public boolean isReleased(int key) {
			return glfwGetKey(window, key) == GLFW_RELEASE;
		}
	}

}
