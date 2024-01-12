package main.java.gui.panels;

import org.lwjgl.glfw.GLFW;

import elements.Button;
import inputs.KeyHandler;
import lwjgui.paint.Color;
import pane.StackPane;
import main.java.gui.Engine_Main;
import main.java.gui.PaneObserver;

public class StartPane extends StackPane {
	public StartPane() {
		setPrefSize(Engine_Main.windowWidth, Engine_Main.windowHeight);
		
		setKeyHandler(new KeyHandler() {
			@Override
			protected void onKeyPress() {
				if(isPressed(GLFW.GLFW_KEY_ESCAPE)) {
					Engine_Main.lwjguiWindow.close();
				}
			}

			@Override
			protected void onKeyRelease() {
				
			}
		});
		
		cursorVisible = true;
		
		initializeElements();
	}
	
	private void initializeElements() {
		Button buttonStart = new Button("Start");
		buttonStart.setPrefSize(300, 100);
		buttonStart.setAbsolutePosition(getWidth()/2d - buttonStart.getPrefWidth()/2d, 200);
		buttonStart.setBorderColor(Color.RED);
		buttonStart.setOnAction(event -> {
			Engine_Main.paneObserver.setCurrentPane(PaneObserver.gamePane);
		});
		
		getChildren().add(buttonStart.getInPane());
	}

}
