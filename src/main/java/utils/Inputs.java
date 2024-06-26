package main.java.utils;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import lwjgui.event.KeyEvent;

public class Inputs {
	public static class MouseInputs {

		private double lastX = 400, lastY = 400;
		private double xoffset, yoffset;

		@SuppressWarnings("unused")
		private double mouseX, mouseY;

		private float scrollY;

		private boolean LMB_Down = false;
		private boolean LMB_Ready = false;
		private boolean RMB_Down = false;
		private boolean RMB_Ready = false;

		GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				xoffset = Math.min(xpos - lastX, 100);
				yoffset = Math.min(lastY - ypos, 100); // reversed since y-coordinates range from bottom to top
				lastX = xpos;
				lastY = ypos;

				if (Math.abs(xoffset) <= 0.2f) {
					xoffset = 0;
				}
				if (Math.abs(yoffset) <= 0.2f) {
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

		public void reset() {
			xoffset = 0;
			yoffset = 0;
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
		Map<Integer, Boolean> values = new HashMap<>();

		public void keyPressed(KeyEvent event) {
			values.put(event.key, true);
		}

		public void keyReleased(KeyEvent event) {
			values.put(event.key, false);
		}

		public boolean isPressed(int key) {
			if (values.containsKey(key))
				return values.get(key);
			return false;
		}

		public boolean isReleased(int key) {
			if (values.containsKey(key))
				return !values.get(key);
			return false;
		}

		public void reset() {
			values.clear();
		}
	}
}
