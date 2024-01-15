package main.java.gui;

import lwjgui.scene.layout.Pane;
import main.java.gui.panels.GamePane;
import main.java.gui.panels.StartPane;

public class PaneObserver {
	public static final Pane gamePane = new GamePane();
	public static final Pane startPane = new StartPane();
	
	private Pane currentPane = startPane;
	
	public static boolean paneChanged = true;
	
	public PaneObserver() {
		
	}
	
	public void setCurrentPane(Pane currentPane) {
		this.currentPane = currentPane;
		paneChanged = true;
	}
	
	public Pane getCurrentPane() {
		return currentPane;
	}
	
}
