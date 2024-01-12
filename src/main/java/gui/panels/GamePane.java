package main.java.gui.panels;

import lwjgui.scene.layout.OpenGLPane;
import lwjgui.theme.Theme;
import lwjgui.theme.ThemeDark;
import main.java.gui.Engine_Main;

public class GamePane extends OpenGLPane {
	public GamePane() {
		super();
		
		Theme.setTheme(new ThemeDark());
		
		setPrefSize(Engine_Main.windowWidth, Engine_Main.windowHeight);
		
		setOnKeyPressed(event -> {
			Engine_Main.keyHandler.keyPressed(event);
		});
		setOnKeyReleased(event -> {
			Engine_Main.keyHandler.keyReleased(event);
		});
		
		setOnMousePressed(event -> {
			System.out.println(event.mouseX);
		});
		
		cursorVisible = false;
		
		initializeElements();
	}
	
	private void initializeElements() {
		this.setRendererCallback(Engine_Main.render);
	}
}
