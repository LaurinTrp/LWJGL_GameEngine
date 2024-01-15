package main.java.gui.panels;

import org.lwjgl.glfw.GLFW;

import lwjgui.event.KeyEvent;
import lwjgui.paint.Color;
import lwjgui.scene.control.ProgressBar;
import lwjgui.scene.layout.OpenGLPane;
import lwjgui.scene.layout.floating.FloatingPane;
import lwjgui.theme.Theme;
import lwjgui.theme.ThemeDark;
import lwjgui.theme.ThemeGame;
import main.java.gui.Engine_Main;
import main.java.gui.PaneObserver;

public class GamePane extends OpenGLPane {
	
	public GamePane() {
		super();

		setPrefSize(Engine_Main.windowWidth, Engine_Main.windowHeight);

		setKeyEvents();

		cursorVisible = false;
		currentTheme = new ThemeGame();

		initializeElements();
	}

	private void setKeyEvents() {
		setOnKeyPressed(event -> {
			Engine_Main.keyHandler.keyPressed(event);
			if (Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_ESCAPE)) {
				Engine_Main.paneObserver.setCurrentPane(PaneObserver.startPane);
			}
		});
		setOnKeyReleased(event -> {
			Engine_Main.keyHandler.keyReleased(event);
		});
	}

	private void initializeElements() {
		this.setRendererCallback(Engine_Main.render);

		ProgressBar pb = new ProgressBar();
		pb.setPrefSize(getWidth()/2f, 30);
		
		pb.setLocalPosition(this, getWidth()/2f-pb.getWidth()/2f, getHeight()-100);
		pb.setProgress(0.5f);
		this.getChildren().add(pb.getInPane());
	}
}
