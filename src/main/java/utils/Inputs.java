package main.java.utils;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Inputs {
	public static class MouseInputs {

		private double lastX = 400, lastY = 400;
		private double xoffset, yoffset;
		
		private double mouseX, mouseY;
		
		private float scrollY;
		
		GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				
				xoffset = xpos - lastX;
				yoffset = lastY - ypos; // reversed since y-coordinates range from bottom to top
				lastX = xpos;
				lastY = ypos;


				if(Math.abs(xoffset) <= 2) {
					xoffset = 0;
				}
				if(Math.abs(yoffset) <= 2) {
					yoffset = 0;
				}
				
				float sensitivity = 0.15f;
				xoffset *= sensitivity;
				yoffset *= sensitivity;
				
				mouseX = xpos;
				mouseY = ypos;
				
			}
		};
		
		GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				scrollY = (float) yoffset;
			}
		};
		
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

		public GLFWCursorPosCallback getCursorPosCallback() {
			return cursorPosCallback;
		}

		public GLFWScrollCallback getScrollCallback() {
			return scrollCallback;
		}
		
		public double getMouseX() {
			return mouseX;
		}
		public double getMouseY() {
			return mouseY;
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
	}

}
