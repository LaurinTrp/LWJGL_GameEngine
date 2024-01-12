package main.java.gui.panels;

import lwjgui.paint.Color;
import lwjgui.scene.control.Button;
import lwjgui.scene.layout.StackPane;
import main.java.gui.Engine_Main;
import main.java.gui.PaneObserver;

public class StartPane extends StackPane {
	
	public StartPane() {
		setPrefSize(Engine_Main.windowWidth, Engine_Main.windowHeight);
		
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
