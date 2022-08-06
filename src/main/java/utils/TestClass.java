package main.java.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

import main.java.gui.Engine_Main;
import main.java.utils.loaders.ModelLoader;

public class TestClass extends Thread implements Runnable {

	@Override
	public void run() {
		super.run();

		ModelLoader.loadMultipleModelsFromObj(
				"/media/laurin/Laurin Festplatte/Blender/Models/Cubes2.obj");

	}

}
