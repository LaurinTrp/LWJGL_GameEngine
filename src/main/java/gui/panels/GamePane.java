package main.java.gui.panels;

import java.awt.event.KeyEvent;

import org.lwjgl.glfw.GLFW;

import inputs.KeyHandler;
import lwjgui.theme.Theme;
import lwjgui.theme.ThemeDark;
import main.java.gui.Engine_Main;
import main.java.gui.PaneObserver;
import pane.OpenGLPane;

public class GamePane extends OpenGLPane {
	
	public GamePane() {
		super(Engine_Main.lwjguiWindow);
		
		Theme.setTheme(new ThemeDark());
		
		setPrefSize(Engine_Main.windowWidth, Engine_Main.windowHeight);


		setKeyHandler(new KeyHandler() {
			@Override
			protected void onKeyPress() {
				if(isPressed(GLFW.GLFW_KEY_ESCAPE)) {
					Engine_Main.paneObserver.setCurrentPane(PaneObserver.startPane);
				}
			}

			@Override
			protected void onKeyRelease() {
			}
		});
		
		cursorVisible = false;
		
		initializeElements();
	}
	
	private void initializeElements() {
		this.setRendererCallback(Engine_Main.render);
	}
}
