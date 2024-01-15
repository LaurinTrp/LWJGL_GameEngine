package main.java.gui.panels;

import org.lwjgl.glfw.GLFW;

import lwjgui.paint.Color;
import lwjgui.scene.control.Button;
import lwjgui.scene.control.Label;
import lwjgui.scene.layout.StackPane;
import lwjgui.theme.ThemeCoral;
import lwjgui.theme.ThemeDark;
import main.java.gui.Engine_Main;
import main.java.gui.PaneObserver;

public class StartPane extends StackPane {
	
	public StartPane() {
		setPrefSize(Engine_Main.windowWidth, Engine_Main.windowHeight);
		
		cursorVisible = true;
		currentTheme = new ThemeDark();
		
		setKeyEvents();
		
		initializeElements();
	}
	
	private void setKeyEvents() {
		setOnKeyPressed(event -> {
			Engine_Main.keyHandler.keyPressed(event);
			if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_ESCAPE)) {
				Engine_Main.lwjguiWindow.close();
			}
		});
		setOnKeyReleased(event -> {
			Engine_Main.keyHandler.keyReleased(event);
		});
	}
	
	private void initializeElements() {
		
		Label title = new Label("~ The Game ~");
		title.setPrefSize(300, 100);
		title.setFontSize(100);
		title.setTextFill(Color.RED);
		title.setAbsolutePosition(getWidth()/2d - title.getPrefWidth()/2d, getHeight() * 0.2 - title.getPrefHeight()/2d);
		getChildren().add(title.getInPane());
		
		Button buttonStart = new Button("Start");
		buttonStart.setPrefSize(300, 100);
		buttonStart.setAbsolutePosition(getWidth()/2d - buttonStart.getPrefWidth()/2d, getHeight() * 0.4 - buttonStart.getPrefHeight()/2d);
		buttonStart.setBorderColor(Color.RED);
		
		buttonStart.setOnAction(event -> {
			Engine_Main.paneObserver.setCurrentPane(PaneObserver.gamePane);
		});
		
		getChildren().add(buttonStart.getInPane());
		

		Button buttonExit = new Button("Exit");
		buttonExit.setPrefSize(300, 100);
		buttonExit.setAbsolutePosition(getWidth()/2d - buttonExit.getPrefWidth()/2d, getHeight() * 0.66 - buttonExit.getPrefHeight()/2d);
		buttonExit.setBorderColor(Color.RED);
		
		buttonExit.setOnAction(event -> {
			Engine_Main.lwjguiWindow.close();
		});
		
		getChildren().add(buttonExit.getInPane());
	}

}
